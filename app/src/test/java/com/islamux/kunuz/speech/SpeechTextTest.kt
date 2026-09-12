package com.islamux.kunuz.speech

import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechTextTest {

    @Test
    fun `removes guillemets and replaces them with spaces`() {
        assertEquals("قال رسول الله ﷺ", cleanForSpeech("«قال رسول الله ﷺ»"))
    }

    @Test
    fun `removes round and square brackets`() {
        assertEquals(
            "قل اللهم ربنا رواه مسلم",
            cleanForSpeech("(قل اللهم ربنا) [رواه مسلم]")
        )
    }

    @Test
    fun `leaves normal arabic and tashkeel intact`() {
        assertEquals("السَّلَامُ عَلَيْكُمْ", cleanForSpeech("السَّلَامُ عَلَيْكُمْ"))
    }

    @Test
    fun `collapses whitespace runs and trims`() {
        assertEquals(
            "قال رسول الله ﷺ",
            cleanForSpeech("  قال   رسول    الله   ﷺ  ")
        )
    }

    @Test
    fun `returns empty for empty or whitespace-only input`() {
        assertEquals("", cleanForSpeech(""))
        assertEquals("", cleanForSpeech("   "))
    }

    @Test
    fun `returns empty when input is only punctuation`() {
        assertEquals("", cleanForSpeech("«»"))
        assertEquals("", cleanForSpeech("( ) [ ]"))
    }
}