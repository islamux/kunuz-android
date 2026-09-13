package com.islamux.kunuz.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.islamux.kunuz.data.model.DEFAULT_DAILY_TASKS
import com.islamux.kunuz.ui.theme.KunuzTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class ChecklistScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun setScreen(
        tasks: Map<String, Boolean> = emptyMap(),
        streak: Int = 1,
        onToggle: (String) -> Unit = {},
        onReset: () -> Unit = {},
        onClose: () -> Unit = {}
    ) {
        composeRule.setContent {
            KunuzTheme {
                ChecklistScreen(
                    tasks = tasks,
                    streak = streak,
                    onToggle = onToggle,
                    onReset = onReset,
                    onClose = onClose
                )
            }
        }
        composeRule.waitForIdle()
    }

    @Test
    fun `renders all eight task titles`() {
        setScreen()

        DEFAULT_DAILY_TASKS.forEach { task ->
            composeRule.onNodeWithText(task.title).assertExists()
        }
    }

    @Test
    fun `clicking a task row invokes onToggle with its id`() {
        var toggledId: String? = null
        setScreen(onToggle = { toggledId = it })

        composeRule.onNodeWithTag("checklist-task-morning-dhikr").performClick()
        composeRule.waitForIdle()

        assertEquals("morning-dhikr", toggledId)
    }

    @Test
    fun `progress text updates when tasks are marked done`() {
        var tasks by mutableStateOf(emptyMap<String, Boolean>())
        composeRule.setContent {
            KunuzTheme {
                ChecklistScreen(
                    tasks = tasks,
                    streak = 1,
                    onToggle = { id -> tasks = tasks + (id to true) },
                    onReset = { tasks = emptyMap() },
                    onClose = {}
                )
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithText("إنجاز اليوم: 0 من 8 سنن").assertExists()

        composeRule.onNodeWithTag("checklist-task-morning-dhikr").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("إنجاز اليوم: 1 من 8 سنن").assertExists()
    }

    @Test
    fun `reset button invokes onReset`() {
        var resetClicked = false
        setScreen(
            tasks = mapOf("morning-dhikr" to true),
            onReset = { resetClicked = true }
        )

        composeRule.onNodeWithTag("checklist-reset").performClick()
        composeRule.waitForIdle()

        assertTrue(resetClicked)
    }

    @Test
    fun `streak chip shows the current streak`() {
        setScreen(streak = 5)

        composeRule.onNodeWithText("حماسة 5 أيام").assertExists()
    }
}
