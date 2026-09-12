package com.islamux.kunuz.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.islamux.kunuz.data.DailyTasksRepository
import com.islamux.kunuz.data.FavoritesRepository
import com.islamux.kunuz.data.SettingsRepository
import com.islamux.kunuz.data.TreasuresRepository
import com.islamux.kunuz.ui.theme.KunuzTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35])
class KunuzAppTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val treasuresRepository = TreasuresRepository(RuntimeEnvironment.getApplication())

    private fun createStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(UnconfinedTestDispatcher()),
            produceFile = { tmpFolder.newFile("preferences.preferences_pb") }
        )

    private fun createViewModel(store: DataStore<Preferences>): KunuzViewModel =
        KunuzViewModel(
            favoritesRepository = FavoritesRepository(store),
            dailyTasksRepository = DailyTasksRepository(store),
            settingsRepository = SettingsRepository(store),
            treasuresRepository = treasuresRepository,
            scope = CoroutineScope(UnconfinedTestDispatcher())
        )

    private fun setApp(store: DataStore<Preferences>): KunuzViewModel {
        val viewModel = createViewModel(store)
        composeRule.setContent {
            KunuzTheme {
                KunuzApp(viewModel = viewModel)
            }
        }
        composeRule.waitForIdle()
        return viewModel
    }

    @Test
    fun `initial all tab shows hero banner and chapter chips`() {
        setApp(createStore())

        composeRule.onNodeWithText("الموسوعة الشاملة لكنوز السنة النبوية").assertExists()
        composeRule.onNodeWithTag("chapter-chips").assertExists()
    }

    @Test
    fun `clicking chapters tab swaps the hero for the chapters grid`() {
        setApp(createStore())
        composeRule.onNodeWithText("الموسوعة الشاملة لكنوز السنة النبوية").assertExists()

        composeRule.onNodeWithTag("nav-CHAPTERS").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("فهرس الأبواب والموضوعات").assertExists()
        composeRule.onNodeWithText("الموسوعة الشاملة لكنوز السنة النبوية").assertDoesNotExist()

        composeRule.onNodeWithTag("nav-ALL").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("الموسوعة الشاملة لكنوز السنة النبوية").assertExists()
    }

    @Test
    fun `typing into search narrows the treasures and shows active filters`() {
        setApp(createStore())
        composeRule.onNodeWithTag("ActiveFiltersBar").assertDoesNotExist()

        composeRule.onNodeWithTag("global-search-input").performTextInput("أذكار الصباح والمساء")
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("ActiveFiltersBar").assertExists()
        composeRule.onNodeWithText("(وجدنا 5 كنزاً)").assertExists()
        composeRule.onNodeWithText("الموسوعة الشاملة لكنوز السنة النبوية").assertDoesNotExist()
        composeRule.onNodeWithText("كنز النوم العظيم وغفران الذنوب ولو كانت مثل زبد البحر").assertDoesNotExist()
    }

    @Test
    fun `daily treasure modal opens and closes from the top bar`() {
        val viewModel = setApp(createStore())

        composeRule.onNodeWithText("كنز اليوم").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("كنز اليوم النبوي المختار").assertExists()
        composeRule.onNodeWithText(viewModel.dailyTreasure.title).assertExists()

        composeRule.onNodeWithContentDescription("إغلاق").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("كنز اليوم النبوي المختار").assertDoesNotExist()
    }

    @Test
    fun `about modal opens and closes`() {
        setApp(createStore())

        composeRule.onNodeWithTag("about-modal-btn").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("عن تطبيق كنوز من السنة النبوية").assertExists()

        composeRule.onNodeWithContentDescription("إغلاق").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("عن تطبيق كنوز من السنة النبوية").assertDoesNotExist()
    }
}