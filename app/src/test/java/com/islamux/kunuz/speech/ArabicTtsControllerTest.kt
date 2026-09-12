package com.islamux.kunuz.speech

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.speech.tts.TextToSpeech
import java.util.Locale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowTextToSpeech

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ArabicTtsControllerTest {

    private val context: Context get() = RuntimeEnvironment.getApplication()

    private fun newController(): Pair<ArabicTtsController, ShadowTextToSpeech> {
        val engine = AndroidTtsEngine(context)
        val controller = ArabicTtsController(context, engine)
        return controller to shadowOf(engine.textToSpeechForTesting)
    }

    private fun makeReady(shadowTts: ShadowTextToSpeech) {
        ShadowTextToSpeech.addLanguageAvailability(Locale.forLanguageTag("ar-SA"))
        shadowTts.getOnInitListener()?.onInit(TextToSpeech.SUCCESS)
    }

    private fun idleMainLooper() {
        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun `fresh controller reports not speaking`() {
        val (controller, _) = newController()

        assertFalse(controller.isSpeaking.value)
    }

    @Test
    fun `speak before TTS initialization returns false and stays silent`() {
        val (controller, _) = newController()

        assertFalse(controller.speak("حديث"))
        assertFalse(controller.isSpeaking.value)
    }

    @Test
    fun `speak when ready returns true sets isSpeaking and completion clears it`() {
        val (controller, shadowTts) = newController()
        makeReady(shadowTts)

        assertTrue(controller.speak("«حديث نبوي»"))

        assertTrue(controller.isSpeaking.value)
        assertEquals("حديث نبوي", shadowTts.getLastSpokenText())
        assertEquals(TextToSpeech.QUEUE_FLUSH, shadowTts.getQueueMode())
        assertEquals(Locale.forLanguageTag("ar-SA"), shadowTts.getCurrentLanguage())

        idleMainLooper()

        assertFalse(controller.isSpeaking.value)
    }

    @Test
    fun `speak with blank text is a no-op`() {
        val (controller, shadowTts) = newController()
        makeReady(shadowTts)

        assertFalse(controller.speak(""))
        assertFalse(controller.speak("   "))

        assertFalse(controller.isSpeaking.value)
        assertEquals(null, shadowTts.getLastSpokenText())
    }

    @Test
    fun `utterance error clears isSpeaking through the registered listener`() {
        val (controller, shadowTts) = newController()
        makeReady(shadowTts)
        assertTrue(controller.speak("حديث"))

        shadowTts.getUtteranceProgressListener()?.onError("kunuz-0", TextToSpeech.ERROR)

        assertFalse(controller.isSpeaking.value)
    }

    @Test
    fun `stop cancels the engine and clears isSpeaking`() {
        val (controller, shadowTts) = newController()
        makeReady(shadowTts)
        controller.speak("حديث")
        assertTrue(controller.isSpeaking.value)

        controller.stop()

        assertFalse(controller.isSpeaking.value)
        assertTrue(shadowTts.isStopped)
    }

    @Test
    fun `shutdown shuts down the engine and blocks further speech`() {
        val (controller, shadowTts) = newController()
        controller.speak("حديث")

        controller.shutdown()

        assertTrue(shadowTts.isShutdown)
        assertFalse(controller.isSpeaking.value)
        assertFalse(controller.speak("حديث"))
    }

    @Test
    fun `isSpeaking is readable through flow collection`() = runTest {
        val (controller, shadowTts) = newController()
        makeReady(shadowTts)

        assertTrue(controller.speak("الحديث النبوي"))
        assertTrue(controller.isSpeaking.first())

        idleMainLooper()

        assertFalse(controller.isSpeaking.first())
    }

    @Test
    fun `speak when engine returns error returns false and stays silent`() {
        val stubEngine = object : TtsEngine {
            override val isReady: Boolean = true

            override fun setCallbacks(
                onReady: () -> Unit,
                onUtteranceStart: () -> Unit,
                onUtteranceDone: () -> Unit,
                onUtteranceError: () -> Unit,
            ) = Unit

            override fun speak(
                text: String,
                queueMode: Int,
                params: Bundle,
                utteranceId: String,
            ): Int = TextToSpeech.ERROR

            override fun stop(): Int = TextToSpeech.SUCCESS
            override fun shutdown() = Unit
        }
        val controller = ArabicTtsController(context, stubEngine)

        assertFalse(controller.speak("حديث"))
        assertFalse(controller.isSpeaking.value)
    }
}