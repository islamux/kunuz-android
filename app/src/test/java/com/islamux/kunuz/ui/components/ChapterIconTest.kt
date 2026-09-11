package com.islamux.kunuz.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.islamux.kunuz.data.model.ChapterIconName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class ChapterIconTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `every chapter icon renders`() {
        var currentIcon by mutableStateOf(ChapterIconName.Sun)
        composeRule.setContent {
            ChapterIcon(icon = currentIcon)
        }
        ChapterIconName.entries.forEach { icon ->
            currentIcon = icon
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("chapterIcon").assertExists()
        }
    }
}