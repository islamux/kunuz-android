package com.islamux.kunuz.logic

import com.islamux.kunuz.data.TreasuresRepository
import com.islamux.kunuz.data.model.TabId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FiltersTest {

    private val repo = TreasuresRepository(RuntimeEnvironment.getApplication())
    private val all = repo.allTreasures

    private fun baseOpts() = FilterOptions(
        activeTab = TabId.ALL,
        favorites = emptySet(),
        selectedChapter = null,
        selectedTag = null,
        searchQuery = ""
    )

    @Test
    fun `returns all treasures with no filters applied`() {
        assertEquals(all.size, filterTreasures(all, baseOpts()).size)
    }

    @Test
    fun `limits to favorites on the favorites tab`() {
        val favorites = setOf(all[0].id, all[3].id)
        val result = filterTreasures(all, baseOpts().copy(activeTab = TabId.FAVORITES, favorites = favorites))
        assertEquals(favorites, result.map { it.id }.toSet())
    }

    @Test
    fun `filters by selected chapter`() {
        val chapterId = all[0].chapterId
        val result = filterTreasures(all, baseOpts().copy(selectedChapter = chapterId))
        assertTrue(result.isNotEmpty())
        assertTrue(result.all { it.chapterId == chapterId })
    }

    @Test
    fun `returns empty for an unknown tag`() {
        val result = filterTreasures(all, baseOpts().copy(selectedTag = "tag-does-not-exist"))
        assertEquals(0, result.size)
    }

    @Test
    fun `matches a search query found in a title`() {
        val source = all[0]
        val term = source.title.split(" ").first()
        val result = filterTreasures(all, baseOpts().copy(searchQuery = term))
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `returns everything for an empty query`() {
        val result = filterTreasures(all, baseOpts().copy(searchQuery = "  "))
        assertEquals(all.size, result.size)
    }
}