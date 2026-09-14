package com.islamux.kunuz.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArabicTextTest {

    @Test
    fun `removeTashkeel removes fatha damma and shadda`() {
        assertEquals("السلام", ArabicText.removeTashkeel("السَّلَامُ"))
    }

    @Test
    fun `removeTashkeel removes tatweel`() {
        assertEquals("السلام", ArabicText.removeTashkeel("السلامـ"))
    }

    @Test
    fun `removeTashkeel normalizes alef variants to alef`() {
        assertEquals("اااا", ArabicText.removeTashkeel("إأآٱ"))
    }

    @Test
    fun `removeTashkeel normalizes teh marbuta to heh and alef maqsura to yeh`() {
        assertEquals("هي", ArabicText.removeTashkeel("ةى"))
    }

    @Test
    fun `removeTashkeel returns empty string for empty input`() {
        assertEquals("", ArabicText.removeTashkeel(""))
    }

    @Test
    fun `matchesSearch matches content with tashkeel against a plain query`() {
        assertTrue(ArabicText.matchesSearch("السلام", "السَّلَام"))
    }

    @Test
    fun `matchesSearch matches plain content against a query with tashkeel`() {
        assertTrue(ArabicText.matchesSearch("السَّلَام", "السلام"))
    }

    @Test
    fun `matchesSearch returns false for non-matching content`() {
        assertFalse(ArabicText.matchesSearch("الرحمن", "السلام"))
    }

    @Test
    fun `matchesSearch returns true for empty query and whitespace-only query`() {
        assertTrue(ArabicText.matchesSearch("السلام", ""))
        assertTrue(ArabicText.matchesSearch("السلام", "   "))
    }

    @Test
    fun `stripTashkeelForDisplay removes the display tashkeel range but keeps other marks`() {
        assertEquals("سلام", ArabicText.stripTashkeelForDisplay("سَلَام"))
        assertEquals("شدة", ArabicText.stripTashkeelForDisplay("شَدَّة"))
    }

    @Test
    fun `stripTashkeelForDisplay returns empty string for empty input`() {
        assertEquals("", ArabicText.stripTashkeelForDisplay(""))
    }

    @Test
    fun `toArabicDigits converts ascii digits to arabic-indic digits`() {
        assertEquals("٠", ArabicText.toArabicDigits(0))
        assertEquals("١٤٠", ArabicText.toArabicDigits(140))
        assertEquals("٤٢", ArabicText.toArabicDigits(42))
    }
}