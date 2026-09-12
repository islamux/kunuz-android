package com.islamux.kunuz.ui.tasbeeh

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.ui.theme.KunuzTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class TasbeehScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

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
    fun `renders header and default dhikr when no treasure is provided`() {
        composeRule.setContent {
            KunuzTheme {
                TasbeehScreen(treasure = null, onClose = {})
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("المسبحة والعداد التفاعلي").assertExists()
        composeRule.onNodeWithText("سبحان الله وبحمده، أستغفر الله وأتوب إليه").assertExists()
    }

    @Test
    fun `tapping the counter increments the count to one`() {
        composeRule.setContent {
            KunuzTheme {
                TasbeehScreen(treasure = null, onClose = {})
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("tasbeeh-tap-area").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("1").assertExists()
    }

    @Test
    fun `reset button clears the count back to zero`() {
        composeRule.setContent {
            KunuzTheme {
                TasbeehScreen(treasure = null, onClose = {})
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("tasbeeh-tap-area").performClick()
        composeRule.onNodeWithTag("tasbeeh-tap-area").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("2").assertExists()
        composeRule.onNodeWithTag("tasbeeh-reset").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("0").assertExists()
    }

    @Test
    fun `shows the treasure id in the summary card when a treasure is provided`() {
        composeRule.setContent {
            KunuzTheme {
                TasbeehScreen(treasure = treasure, onClose = {})
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("#7", substring = true).assertExists()
    }
}
