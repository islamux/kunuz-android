package com.islamux.kunuz.logic

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class StreakTest {

    @Test
    fun `localDateKey formats a date as zero-padded yyyy-MM-dd`() {
        assertEquals("2026-01-05", localDateKey(LocalDate.of(2026, 1, 5)))
        assertEquals("2026-12-31", localDateKey(LocalDate.of(2026, 12, 31)))
    }

    @Test
    fun `countCompleted counts only true values`() {
        assertEquals(1, countCompleted(mapOf("a" to true, "b" to false)))
        assertEquals(0, countCompleted(emptyMap()))
        assertEquals(2, countCompleted(mapOf("a" to true, "b" to true)))
    }

    // --- nextStreak ---

    @Test
    fun `nextStreak increments the streak when last was yesterday`() {
        assertEquals(
            Pair(2, "2026-03-16"),
            nextStreak("2026-03-15", 1, "2026-03-16", "2026-03-15")
        )
    }

    @Test
    fun `nextStreak is a no-op when the streak was already recorded today`() {
        assertNull(nextStreak("2026-03-16", 5, "2026-03-16", "2026-03-15"))
    }

    @Test
    fun `nextStreak resets the streak to 1 when the last record is stale`() {
        assertEquals(
            Pair(1, "2026-03-16"),
            nextStreak("2026-03-13", 5, "2026-03-16", "2026-03-15")
        )
    }

    @Test
    fun `nextStreak returns 1 and today when there was no previous record`() {
        assertEquals(
            Pair(1, "2026-03-16"),
            nextStreak(null, 0, "2026-03-16", "2026-03-15")
        )
    }

    // --- effectiveStreak ---

    @Test
    fun `effectiveStreak returns stored value when last is today`() {
        assertEquals(5, effectiveStreak("2026-03-16", 5, "2026-03-16", "2026-03-15"))
    }

    @Test
    fun `effectiveStreak returns stored value when last is yesterday`() {
        assertEquals(3, effectiveStreak("2026-03-15", 3, "2026-03-16", "2026-03-15"))
    }

    @Test
    fun `effectiveStreak returns 0 when last is stale`() {
        assertEquals(0, effectiveStreak("2026-03-13", 7, "2026-03-16", "2026-03-15"))
    }

    @Test
    fun `effectiveStreak returns 0 when there is no streak`() {
        assertEquals(0, effectiveStreak(null, 0, "2026-03-16", "2026-03-15"))
    }
}
