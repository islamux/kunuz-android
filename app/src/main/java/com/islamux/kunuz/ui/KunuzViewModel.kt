package com.islamux.kunuz.ui

import androidx.lifecycle.ViewModel
import com.islamux.kunuz.data.DailyTasksRepository
import com.islamux.kunuz.data.FavoritesRepository
import com.islamux.kunuz.data.SettingsRepository
import com.islamux.kunuz.data.TreasuresRepository
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.FontSize
import com.islamux.kunuz.data.model.TabId
import com.islamux.kunuz.data.model.Treasure
import com.islamux.kunuz.logic.FilterOptions
import com.islamux.kunuz.logic.filterTreasures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

data class KunuzUiState(
    val tab: TabId = TabId.ALL,
    val selectedChapter: ChapterId? = null,
    val selectedTag: String? = null,
    val searchQuery: String = "",
    val fontSize: FontSize = FontSize.NORMAL,
    val showTashkeel: Boolean = true,
    val favorites: Set<Int> = emptySet(),
    val dailyTreasure: Treasure,
    val showDailyModal: Boolean = false,
    val showAboutModal: Boolean = false,
    val showChecklist: Boolean = false,
    val shareTreasure: Treasure? = null,
    val tasbeehTreasure: Treasure? = null
)

class KunuzViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val dailyTasksRepository: DailyTasksRepository,
    private val settingsRepository: SettingsRepository,
    private val treasuresRepository: TreasuresRepository,
    private val scope: CoroutineScope
) : ViewModel() {

    val dailyTreasure: Treasure = treasuresRepository.getDailyTreasure()

    val chapters: List<Chapter> = treasuresRepository.chapters
    val chapterCounts: Map<ChapterId, Int> = treasuresRepository.chapterCounts
    val totalTreasures: Int = treasuresRepository.total
    val chapterById: Map<ChapterId, Chapter> = chapters.associateBy { it.id }

    private val initialState = KunuzUiState(dailyTreasure = dailyTreasure)

    private val _uiState = MutableStateFlow(initialState)
    private val _filteredTreasures = MutableStateFlow(filteredOf(initialState))

    val uiState: StateFlow<KunuzUiState> = _uiState.asStateFlow()
    val filteredTreasures: StateFlow<List<Treasure>> = _filteredTreasures.asStateFlow()

    init {
        seedFromRepositories()
        observeRepositories()
    }

    private fun seedFromRepositories() {
        scope.launch {
            val favorites = favoritesRepository.favorites.first()
            val fontSize = settingsRepository.fontSize.first()
            val showTashkeel = settingsRepository.showTashkeel.first()
            mutate { it.copy(favorites = favorites, fontSize = fontSize, showTashkeel = showTashkeel) }
        }
    }

    private fun observeRepositories() {
        scope.launch {
            favoritesRepository.favorites.drop(1).collect { favs -> mutate { it.copy(favorites = favs) } }
        }
        scope.launch {
            settingsRepository.fontSize.drop(1).collect { font -> mutate { it.copy(fontSize = font) } }
        }
        scope.launch {
            settingsRepository.showTashkeel.drop(1).collect { tash -> mutate { it.copy(showTashkeel = tash) } }
        }
    }

    private fun filteredOf(state: KunuzUiState): List<Treasure> = filterTreasures(
        treasuresRepository.allTreasures,
        FilterOptions(
            activeTab = state.tab,
            favorites = state.favorites,
            selectedChapter = state.selectedChapter,
            selectedTag = state.selectedTag,
            searchQuery = state.searchQuery
        )
    )

    private fun mutate(transform: (KunuzUiState) -> KunuzUiState) {
        _uiState.update { current ->
            val next = transform(current)
            _filteredTreasures.value = filteredOf(next)
            next
        }
    }

    fun selectTab(tab: TabId) {
        when (tab) {
            TabId.CHECKLIST -> mutate { it.copy(showChecklist = true) }
            TabId.TASBEEH -> mutate { it.copy(tasbeehTreasure = null) }
            else -> mutate { it.copy(tab = tab) }
        }
    }

    fun selectChapter(chapterId: ChapterId?, forceAllTab: Boolean = true) {
        if (forceAllTab) {
            mutate { it.copy(selectedChapter = chapterId, tab = TabId.ALL) }
        } else {
            mutate { it.copy(selectedChapter = chapterId) }
        }
    }

    fun selectTag(tag: String?) {
        mutate { it.copy(selectedTag = tag, tab = TabId.ALL) }
    }

    fun setSearchQuery(query: String) {
        mutate { it.copy(searchQuery = query) }
    }

    fun clearFilters() {
        mutate { it.copy(searchQuery = "", selectedChapter = null, selectedTag = null) }
    }

    fun toggleFavorite(id: Int) {
        mutate { state ->
            val has = id in state.favorites
            state.copy(favorites = if (has) state.favorites - id else state.favorites + id)
        }
        scope.launch { favoritesRepository.toggle(id) }
    }

    fun cycleFontSize() {
        val next = _uiState.value.fontSize.next()
        mutate { it.copy(fontSize = next) }
        scope.launch { settingsRepository.setFontSize(next) }
    }

    fun setShowTashkeel(show: Boolean) {
        mutate { it.copy(showTashkeel = show) }
        scope.launch { settingsRepository.setShowTashkeel(show) }
    }

    fun clearFavorites() {
        mutate { it.copy(favorites = emptySet()) }
        scope.launch { favoritesRepository.clear() }
    }

    fun openDailyModal() = mutate { it.copy(showDailyModal = true) }
    fun closeDailyModal() = mutate { it.copy(showDailyModal = false) }
    fun openAboutModal() = mutate { it.copy(showAboutModal = true) }
    fun closeAboutModal() = mutate { it.copy(showAboutModal = false) }
    fun openChecklist() = mutate { it.copy(showChecklist = true) }
    fun closeChecklist() = mutate { it.copy(showChecklist = false) }
    fun openShare(treasure: Treasure) = mutate { it.copy(shareTreasure = treasure) }
    fun closeShare() = mutate { it.copy(shareTreasure = null) }
    fun openTasbeeh(treasure: Treasure) = mutate { it.copy(tasbeehTreasure = treasure) }
    fun closeTasbeeh() = mutate { it.copy(tasbeehTreasure = null) }
}

private fun FontSize.next(): FontSize {
    val entries = FontSize.entries
    return entries[(entries.indexOf(this) + 1) % entries.size]
}