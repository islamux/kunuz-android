package com.islamux.kunuz.sound

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator

fun playClick(context: Context, enabled: Boolean) {
    if (!enabled) return
    runCatching {
        val generator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        try {
            generator.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
        } finally {
            runCatching { generator.release() }
        }
    }
}