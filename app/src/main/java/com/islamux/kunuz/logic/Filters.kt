package com.islamux.kunuz.logic

import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.TabId
import com.islamux.kunuz.data.model.Treasure

data class FilterOptions(
    val activeTab: TabId,
    val favorites: Set<Int>,
    val selectedChapter: ChapterId?,
    val selectedTag: String?,
    val searchQuery: String
)

fun filterTreasures(treasures: List<Treasure>, opts: FilterOptions): List<Treasure> {
    return treasures.filter { treasure ->
        if (opts.activeTab == TabId.FAVORITES && !opts.favorites.contains(treasure.id)) return@filter false

        if (opts.selectedChapter != null && treasure.chapterId != opts.selectedChapter) return@filter false

        if (opts.selectedTag != null && !treasure.tags.contains(opts.selectedTag)) return@filter false

        if (opts.searchQuery.trim().isNotEmpty()) {
            val fullContent = listOf(
                treasure.title, treasure.hadith, treasure.narrator, treasure.source,
                treasure.explanation, treasure.action, treasure.tags.joinToString(" ")
            ).joinToString(" ")
            if (!ArabicText.matchesSearch(fullContent, opts.searchQuery)) return@filter false
        }

        true
    }
}