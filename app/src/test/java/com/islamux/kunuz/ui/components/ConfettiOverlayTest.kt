package com.islamux.kunuz.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class ConfettiOverlayTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `fresh overlay renders and is idle when trigger is zero`() {
        val controller = ConfettiController()
        composeRule.setContent {
            ConfettiOverlay(controller = controller)
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("confettiOverlay").assertExists()
        composeRule.onNodeWithTag("confettiCanvas").assertExists()
    }

    @Test
    fun `overlay still renders and recomposes after fire without crashing`() {
        val controller = ConfettiController()
        composeRule.setContent {
            ConfettiOverlay(controller = controller)
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("confettiOverlay").assertExists()

        controller.fire()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("confettiOverlay").assertExists()
        composeRule.onNodeWithTag("confettiCanvas").assertExists()
    }
}