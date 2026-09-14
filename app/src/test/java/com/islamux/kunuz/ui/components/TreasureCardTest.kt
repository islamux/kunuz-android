package com.islamux.kunuz.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.ChapterIconName
import com.islamux.kunuz.data.model.FontSize
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.ui.theme.KunuzTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class TreasureCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val chapter = Chapter(
        id = ChapterId.DAILY_DHIKR,
        name = "أذكار وأدعية",
        shortName = "الأذكار",
        icon = ChapterIconName.Sun,
        description = "أذكار وعبادات",
        colorClasses = ""
    )

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
        repeatCount = 3,
        tags = listOf("أذكار الصباح والمساء")
    )

    @Test
    fun `renders the treasure title on screen`() {
        composeRule.setContent {
            KunuzTheme {
                TreasureCard(
                    treasure = treasure,
                    chapter = chapter,
                    isFavorite = false,
                    onToggleFavorite = {},
                    fontSize = FontSize.NORMAL,
                    showTashkeel = true,
                    onOpenShare = {},
                    onOpenTasbeeh = {}
                )
            }
        }
        composeRule.onNodeWithText("فضل الذكر").assertExists()
    }

    @Test
    fun `renders the hadith inside the quote frame`() {
        composeRule.setContent {
            KunuzTheme {
                TreasureCard(
                    treasure = treasure,
                    chapter = chapter,
                    isFavorite = false,
                    onToggleFavorite = {},
                    fontSize = FontSize.NORMAL,
                    showTashkeel = true,
                    onOpenShare = {},
                    onOpenTasbeeh = {}
                )
            }
        }
        composeRule.onNodeWithText("«أفضل الكلام بعد القرآن»").assertExists()
    }

    @Test
    fun `favorite callback fires when the bookmark is tapped`() {
        var toggledId: Int? = null
        composeRule.setContent {
            KunuzTheme {
                TreasureCard(
                    treasure = treasure,
                    chapter = chapter,
                    isFavorite = false,
                    onToggleFavorite = { toggledId = it },
                    fontSize = FontSize.NORMAL,
                    showTashkeel = true,
                    onOpenShare = {},
                    onOpenTasbeeh = {}
                )
            }
        }
        composeRule.onNodeWithTag("fav-btn-${treasure.id}").performClick()
        assertEquals(7, toggledId)
    }

    @Test
    fun `shows the repeat counter block when repeatCount is greater than one`() {
        composeRule.setContent {
            KunuzTheme {
                TreasureCard(
                    treasure = treasure,
                    chapter = chapter,
                    isFavorite = false,
                    onToggleFavorite = {},
                    fontSize = FontSize.NORMAL,
                    showTashkeel = true,
                    onOpenShare = {},
                    onOpenTasbeeh = {}
                )
            }
        }
        composeRule.onNodeWithText("ورد التكرار النبوي: 3 مرة").assertExists()
    }
}