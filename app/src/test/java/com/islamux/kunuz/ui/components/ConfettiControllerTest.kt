package com.islamux.kunuz.ui.components

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Assert.assertEquals
import org.junit.Test

class ConfettiControllerTest {

    @Test
    fun `trigger starts at zero`() {
        val controller = ConfettiController()
        assertEquals(0L, controller.trigger.value)
    }

    @Test
    fun `fire increments the counter`() {
        val controller = ConfettiController()
        controller.fire()
        assertEquals(1L, controller.trigger.value)
        controller.fire()
        assertEquals(2L, controller.trigger.value)
    }

    @Test
    fun `fire is safe to call repeatedly with monotonic consecutive values`() {
        val controller = ConfettiController()
        repeat(50) { controller.fire() }
        assertEquals(50L, controller.trigger.value)
        controller.fire()
        assertEquals(51L, controller.trigger.value)
    }
}