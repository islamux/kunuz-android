package com.islamux.kunuz.speech

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val ARABIC_SA = Locale.forLanguageTag("ar-SA")
private const val SPEECH_RATE = 0.92f

/**
 * Plays Arabic hadith text through the platform TTS and mirrors the web
 * behaviour from `src/utils/speech.ts`: ar-SA locale, rate 0.92, punctuation
 * removed before speaking, cancel-before-speak, and is-speaking flipped false
 * on completion/stop.
 */
internal interface TtsEngine {
    val isReady: Boolean

    fun setCallbacks(
        onReady: () -> Unit,
        onUtteranceStart: () -> Unit,
        onUtteranceDone: () -> Unit,
        onUtteranceError: () -> Unit,
    )

    fun speak(text: String, queueMode: Int, params: Bundle, utteranceId: String): Int
    fun stop(): Int
    fun shutdown()
}

internal class AndroidTtsEngine(context: Context) : TtsEngine {

    private val textToSpeech: TextToSpeech = TextToSpeech(context) { status ->
        ready = status == TextToSpeech.SUCCESS
        if (ready) {
            textToSpeech.setLanguage(ARABIC_SA)
            textToSpeech.setSpeechRate(SPEECH_RATE)
            onReady?.invoke()
        }
    }

    @Volatile
    private var ready = false

    private var onReady: (() -> Unit)? = null
    private var onUtteranceStart: (() -> Unit)? = null
    private var onUtteranceDone: (() -> Unit)? = null
    private var onUtteranceError: (() -> Unit)? = null

    /** Test access seam — the real [TextToSpeech] the engine drives. */
    internal val textToSpeechForTesting: TextToSpeech get() = textToSpeech

    init {
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                onUtteranceStart?.invoke()
            }

            override fun onDone(utteranceId: String?) {
                onUtteranceDone?.invoke()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                onUtteranceError?.invoke()
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                onUtteranceError?.invoke()
            }
        })
    }

    override val isReady: Boolean get() = ready

    override fun setCallbacks(
        onReady: () -> Unit,
        onUtteranceStart: () -> Unit,
        onUtteranceDone: () -> Unit,
        onUtteranceError: () -> Unit,
    ) {
        this.onReady = onReady
        this.onUtteranceStart = onUtteranceStart
        this.onUtteranceDone = onUtteranceDone
        this.onUtteranceError = onUtteranceError
    }

    override fun speak(text: String, queueMode: Int, params: Bundle, utteranceId: String): Int =
        textToSpeech.speak(text, queueMode, params, utteranceId)

    override fun stop(): Int = textToSpeech.stop()

    override fun shutdown() {
        ready = false
        textToSpeech.shutdown()
    }
}

/**
 * Arabic text-to-speech controller backed by the platform TTS. Keeps an
 * [isSpeaking] [StateFlow] so UI can toggle the listen affordance.
 *
 * [speak] mirrors the web's cancel-before-speak: it stops any current
 * utterance, dispatches with [TextToSpeech.QUEUE_FLUSH], marks the utterance
 * with a unique id, and reports success synchronously (as the web's
 * `notify(true)` after `speak`). Completion/error/stop each reset the flag.
 */
class ArabicTtsController internal constructor(
    context: Context,
    private val engine: TtsEngine,
) {
    constructor(context: Context) : this(context, AndroidTtsEngine(context))

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val utteranceSequence = AtomicLong(0)

    init {
        engine.setCallbacks(
            onReady = { _isSpeaking.value = false },
            onUtteranceStart = { _isSpeaking.value = true },
            onUtteranceDone = { _isSpeaking.value = false },
            onUtteranceError = { _isSpeaking.value = false },
        )
    }

    /**
     * Speaks [text]. Returns false when the TTS engine is not yet initialized
     * or the cleaned text is empty; otherwise the dispatch succeeded.
     */
    fun speak(text: String): Boolean {
        if (!engine.isReady) return false
        val cleanText = cleanForSpeech(text)
        if (cleanText.isEmpty()) return false
        engine.stop()
        val utteranceId = "kunuz-" + utteranceSequence.getAndIncrement()
        val result = engine.speak(cleanText, TextToSpeech.QUEUE_FLUSH, Bundle(), utteranceId)
        if (result != TextToSpeech.SUCCESS) {
            _isSpeaking.value = false
            return false
        }
        _isSpeaking.value = true
        return true
    }

    fun stop() {
        engine.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        engine.shutdown()
        _isSpeaking.value = false
    }
}