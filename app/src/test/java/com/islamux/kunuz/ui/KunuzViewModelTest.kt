package com.islamux.kunuz.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.islamux.kunuz.data.DailyTasksRepository
import com.islamux.kunuz.data.FavoritesRepository
import com.islamux.kunuz.data.SettingsRepository
import com.islamux.kunuz.data.TreasuresRepository
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.FontSize
import com.islamux.kunuz.data.model.TabId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class KunuzViewModelTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val treasuresRepository = TreasuresRepository(RuntimeEnvironment.getApplication())

    private fun createDataStore(dispatcher: CoroutineDispatcher = testDispatcher): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(dispatcher),
            produceFile = { tmpFolder.newFile("preferences.preferences_pb") }
        )

    private fun createViewModel(
        store: DataStore<Preferences>,
        dispatcher: CoroutineDispatcher = testDispatcher
    ): KunuzViewModel =
        KunuzViewModel(
            favoritesRepository = FavoritesRepository(store),
            dailyTasksRepository = DailyTasksRepository(store),
            settingsRepository = SettingsRepository(store),
            treasuresRepository = treasuresRepository,
            scope = CoroutineScope(dispatcher)
        )

    @Test
    fun `initial state has web defaults`() {
        val vm = createViewModel(createDataStore())

        val state = vm.uiState.value

        assertEquals(TabId.ALL, state.tab)
        assertEquals("", state.searchQuery)
        assertNull(state.selectedChapter)
        assertNull(state.selectedTag)
        assertTrue(state.favorites.isEmpty())
        assertEquals(FontSize.NORMAL, state.fontSize)
        assertTrue(state.showTashkeel)
    }

    @Test
    fun `setSearchQuery narrows filteredTreasures`() {
        val vm = createViewModel(createDataStore())
        val total = vm.filteredTreasures.value.size

        vm.setSearchQuery("أذكار الصباح والمساء")

        assertTrue(vm.filteredTreasures.value.isNotEmpty())
        assertTrue("expected results to shrink below $total", vm.filteredTreasures.value.size < total)
        assertEquals("أذكار الصباح والمساء", vm.uiState.value.searchQuery)
    }

    @Test
    fun `setSearchQuery with no match yields empty list`() {
        val vm = createViewModel(createDataStore())

        vm.setSearchQuery("zzzqxnonexistent")

        assertEquals(0, vm.filteredTreasures.value.size)
    }

    @Test
    fun `selectTag sets tag and switches tab to all and narrows results`() {
        val vm = createViewModel(createDataStore())
        vm.selectTab(TabId.FAVORITES)

        vm.selectTag("أذكار الصباح والمساء")

        val state = vm.uiState.value
        assertEquals("أذكار الصباح والمساء", state.selectedTag)
        assertEquals(TabId.ALL, state.tab)
        assertTrue(vm.filteredTreasures.value.isNotEmpty())
        assertTrue(vm.filteredTreasures.value.all { it.tags.contains("أذكار الصباح والمساء") })
    }

    @Test
    fun `toggleFavorite persists and is reversible`() = runTest {
        val store = createDataStore()
        val favoritesRepository = FavoritesRepository(store)
        val vm = createViewModel(store)

        vm.toggleFavorite(7)

        assertTrue(vm.uiState.value.favorites.contains(7))
        assertTrue(favoritesRepository.favorites.first().contains(7))

        vm.toggleFavorite(7)

        assertTrue(vm.uiState.value.favorites.isEmpty())
        assertTrue(favoritesRepository.favorites.first().isEmpty())
    }

    @Test
    fun `cycleFontSize cycles NORMAL to LARGE to XLARGE and back`() = runTest {
        val store = createDataStore()
        val settingsRepository = SettingsRepository(store)
        val vm = createViewModel(store)

        vm.cycleFontSize()
        assertEquals(FontSize.LARGE, vm.uiState.value.fontSize)
        assertEquals(FontSize.LARGE, settingsRepository.fontSize.first())

        vm.cycleFontSize()
        assertEquals(FontSize.XLARGE, vm.uiState.value.fontSize)
        assertEquals(FontSize.XLARGE, settingsRepository.fontSize.first())

        vm.cycleFontSize()
        assertEquals(FontSize.NORMAL, vm.uiState.value.fontSize)
        assertEquals(FontSize.NORMAL, settingsRepository.fontSize.first())
    }

    @Test
    fun `setShowTashkeel persists through settings repository`() = runTest {
        val store = createDataStore()
        val settingsRepository = SettingsRepository(store)
        val vm = createViewModel(store)
        assertTrue(vm.uiState.value.showTashkeel)

        vm.setShowTashkeel(false)

        assertEquals(false, vm.uiState.value.showTashkeel)
        assertEquals(false, settingsRepository.showTashkeel.first())
    }

    @Test
    fun `clearFilters resets search chapter and tag while keeping favorites`() {
        val vm = createViewModel(createDataStore())
        vm.setSearchQuery("ذكر")
        vm.selectChapter(ChapterId.DAILY_DHIKR)
        vm.selectTag("أذكار الصباح والمساء")

        vm.clearFilters()

        val state = vm.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.selectedChapter)
        assertNull(state.selectedTag)
        assertEquals(TabId.ALL, state.tab)
        assertEquals(treasuresRepository.allTreasures.size, vm.filteredTreasures.value.size)
    }

    @Test
    fun `dailyTreasure is non-null and stable across reads`() {
        val vm = createViewModel(createDataStore())
        assertNotNull(vm.dailyTreasure)
        assertEquals(vm.dailyTreasure, vm.dailyTreasure)
        assertTrue(vm.filteredTreasures.value.contains(vm.dailyTreasure))
    }

    @Test
    fun `selectTab CHECKLIST opens the checklist modal`() {
        val vm = createViewModel(createDataStore())
        assertEquals(false, vm.uiState.value.showChecklist)

        vm.selectTab(TabId.CHECKLIST)

        assertTrue(vm.uiState.value.showChecklist)
        assertNotEquals(TabId.CHECKLIST, vm.uiState.value.tab)
    }

    @Test
    fun `selectTab TASBEEH clears tasbeeh treasure without switching tab`() {
        val vm = createViewModel(createDataStore())
        vm.openTasbeeh(vm.dailyTreasure)
        vm.selectTab(TabId.ALL)

        vm.selectTab(TabId.TASBEEH)

        assertNull(vm.uiState.value.tasbeehTreasure)
        assertEquals(TabId.ALL, vm.uiState.value.tab)
    }

    @Test
    fun `selectChapter sets chapter and switches tab to all`() {
        val vm = createViewModel(createDataStore())
        vm.selectTab(TabId.CHAPTERS)

        vm.selectChapter(ChapterId.DAILY_DHIKR)

        assertEquals(ChapterId.DAILY_DHIKR, vm.uiState.value.selectedChapter)
        assertEquals(TabId.ALL, vm.uiState.value.tab)
    }

    @Test
    fun `selectChapter with forceAllTab false keeps the current tab`() {
        val vm = createViewModel(createDataStore())
        vm.selectTab(TabId.FAVORITES)

        vm.selectChapter(ChapterId.DAILY_DHIKR, forceAllTab = false)

        assertEquals(ChapterId.DAILY_DHIKR, vm.uiState.value.selectedChapter)
        assertEquals(TabId.FAVORITES, vm.uiState.value.tab)
    }

    @Test
    fun `favorites tab shows only favorited treasures`() {
        val vm = createViewModel(createDataStore())
        vm.toggleFavorite(1)
        vm.toggleFavorite(2)

        vm.selectTab(TabId.FAVORITES)

        val shown = vm.filteredTreasures.value
        assertEquals(setOf(1, 2), shown.map { it.id }.toSet())
    }

    @Test
    fun `seeded favorites from datastore populate initial state`() = runTest {
        val store = createDataStore()
        val favoritesRepository = FavoritesRepository(store)
        favoritesRepository.toggle(5)

        val vm = createViewModel(store)

        assertTrue(vm.uiState.value.favorites.contains(5))
    }

    @Test
    fun `toggleTask persists and updates the daily tasks flows`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val store = createDataStore(dispatcher)
        val dailyTasksRepository = DailyTasksRepository(store)
        val vm = createViewModel(store, dispatcher)

        vm.toggleTask("morning-dhikr")
        testScheduler.advanceUntilIdle()

        assertTrue(vm.dailyTasks.value["morning-dhikr"] == true)
        assertTrue(dailyTasksRepository.todayTasks.first()["morning-dhikr"] == true)

        vm.toggleTask("morning-dhikr")
        testScheduler.advanceUntilIdle()

        assertFalse(vm.dailyTasks.value["morning-dhikr"] == true)
        assertTrue(dailyTasksRepository.todayTasks.first() == mapOf("morning-dhikr" to false))
    }

    @Test
    fun `toggling the final missing task records the streak completion`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val store = createDataStore(dispatcher)
        val dailyTasksRepository = DailyTasksRepository(store)
        val vm = createViewModel(store, dispatcher)

        vm.toggleTask("morning-dhikr")
        vm.toggleTask("evening-dhikr")
        vm.toggleTask("salat-duha")
        vm.toggleTask("ayat-kursi")
        vm.toggleTask("istighfar-100")
        vm.toggleTask("salawat-nabi")
        vm.toggleTask("daily-charity")
        testScheduler.advanceUntilIdle()

        assertEquals(0, vm.checklistStreak.value)

        vm.toggleTask("salat-witr")
        testScheduler.advanceUntilIdle()

        assertEquals(1, dailyTasksRepository.streak.first())
        assertEquals(1, vm.checklistStreak.value)
    }

    @Test
    fun `resetTodayTasks clears the completed tasks`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val store = createDataStore(dispatcher)
        val dailyTasksRepository = DailyTasksRepository(store)
        val vm = createViewModel(store, dispatcher)
        vm.toggleTask("morning-dhikr")
        vm.toggleTask("salat-witr")
        testScheduler.advanceUntilIdle()
        assertTrue(vm.dailyTasks.value.isNotEmpty())

        vm.resetTodayTasks()
        testScheduler.advanceUntilIdle()

        assertTrue(vm.dailyTasks.value.isEmpty())
        assertTrue(dailyTasksRepository.todayTasks.first().isEmpty())
    }

    @Test
    fun `seeded daily tasks and streak from datastore populate initial state`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val store = createDataStore(dispatcher)
        val dailyTasksRepository = DailyTasksRepository(store)
        val allIds = listOf(
            "morning-dhikr", "evening-dhikr", "salat-duha", "ayat-kursi",
            "istighfar-100", "salawat-nabi", "daily-charity", "salat-witr"
        )
        val allDone = allIds.associateWith { true }
        dailyTasksRepository.setAll(mapOf("morning-dhikr" to true))
        dailyTasksRepository.recordStreakOnCompletion(allIds, allDone)
        testScheduler.advanceUntilIdle()

        val vm = createViewModel(store, dispatcher)
        testScheduler.advanceUntilIdle()

        assertEquals(mapOf("morning-dhikr" to true), vm.dailyTasks.value)
        assertEquals(1, vm.checklistStreak.value)
    }
}
