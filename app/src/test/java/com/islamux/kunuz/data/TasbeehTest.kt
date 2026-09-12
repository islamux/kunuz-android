package com.islamux.kunuz.data

import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.Treasure
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TasbeehTest {

    private val treasure = Treasure(
        id = 7,
        title = "فضل الذكر",
        chapterId = ChapterId.DAILY_DHIKR,
        hadith = "أفضل الكلام بعد القرآن",
        narrator = "أبو هريرة",
        source = "متفق عليه",
        grade = "صحيح",
        explanation = "بيان فضل الذكر",
        action = "أكثر من الذكر في يومك",
        repeatCount = 33,
        tags = emptyList()
    )

    @Test
    fun `sound fires when sound enabled`() {
        val event = Tasbeeh.eventAfterIncrement(newCount = 1, target = 33, soundEnabled = true)
        assertTrue(event.sound)
    }

    @Test
    fun `sound suppressed when sound disabled`() {
        val event = Tasbeeh.eventAfterIncrement(newCount = 1, target = 33, soundEnabled = false)
        assertFalse(event.sound)
    }

    @Test
    fun `haptic always fires`() {
        val event = Tasbeeh.eventAfterIncrement(newCount = 1, target = 33, soundEnabled = false)
        assertTrue(event.haptic)
    }

    @Test
    fun `confetti fires when count reaches target`() {
        val event = Tasbeeh.eventAfterIncrement(newCount = 33, target = 33, soundEnabled = true)
        assertTrue(event.confetti)
    }

    @Test
    fun `confetti does not fire below target`() {
        val event = Tasbeeh.eventAfterIncrement(newCount = 32, target = 33, soundEnabled = true)
        assertFalse(event.confetti)
    }

    @Test
    fun `default target falls back to thirty three with no repeat count`() {
        assertEquals(33, Tasbeeh.defaultTarget(null))
        assertEquals(33, Tasbeeh.defaultTarget(treasure.copy(repeatCount = null)))
    }

    @Test
    fun `default target uses the treasure repeat count when present`() {
        assertEquals(33, Tasbeeh.defaultTarget(treasure))
    }
}