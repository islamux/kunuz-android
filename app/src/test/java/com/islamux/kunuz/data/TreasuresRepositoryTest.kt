package com.islamux.kunuz.data

import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.ChapterIconName
import com.islamux.kunuz.data.model.Treasure
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.time.LocalDate
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class TreasuresRepositoryTest {

    private val repo = TreasuresRepository(RuntimeEnvironment.getApplication())

    @Test
    fun `allTreasures contains exactly 140 with ids 1 to 140 and no duplicates`() {
        assertEquals(140, repo.allTreasures.size)
        assertEquals((1..140).toList(), repo.allTreasures.map { it.id }.sorted())
    }

    @Test
    fun `every treasure has all required fields non-empty`() {
        for (treasure in repo.allTreasures) {
            for (field in REQUIRED_FIELDS) {
                assertTrue(
                    "$field must be non-empty on treasure ${treasure.id}",
                    fieldValue(treasure, field).isNotBlank()
                )
            }
        }
    }

    @Test
    fun `every treasure has a valid chapterId`() {
        for (treasure in repo.allTreasures) {
            assertTrue("treasure ${treasure.id}", VALID_CHAPTER_IDS.contains(treasure.chapterId))
        }
    }

    @Test
    fun `chapterCounts sums to total`() {
        assertEquals(140, repo.total)
        assertEquals(repo.total, repo.chapterCounts.values.sum())
    }

    @Test
    fun `chapterCounts has an entry for every valid chapter id`() {
        for (id in VALID_CHAPTER_IDS) {
            assertTrue("missing count for $id", repo.chapterCounts.containsKey(id))
        }
    }

    @Test
    fun `chapters each have non-empty colorClasses and a valid icon`() {
        for (chapter in repo.chapters) {
            assertTrue("chapter ${chapter.id}", chapter.colorClasses.isNotBlank())
            assertTrue("chapter ${chapter.id}", VALID_ICON_NAMES.contains(chapter.icon))
        }
    }

    @Test
    fun `getDailyTreasure is deterministic within a day`() {
        val date = LocalDate.of(2026, 9, 11)
        assertEquals(repo.getDailyTreasure(date).id, repo.getDailyTreasure(date).id)

        val sample = (0L..30L).map { repo.getDailyTreasure(LocalDate.of(2026, 9, 1).plusDays(it)).id }.distinct()
        assertTrue("ids over 31 consecutive dates must not all be equal", sample.size > 1)
    }

    @Test
    fun `getDailyTreasure returns a member of allTreasures`() {
        val daily = repo.getDailyTreasure(LocalDate.of(2026, 9, 11))
        assertTrue(repo.allTreasures.any { it.id == daily.id })
    }

    @Test
    fun `getRandomTreasure returns a member of allTreasures`() {
        val random = repo.getRandomTreasure(Random(42))
        assertTrue(repo.allTreasures.any { it.id == random.id })

        val rng = Random(7)
        val ids = (1..100).map { repo.getRandomTreasure(rng).id }.distinct()
        assertTrue("expected more than 1 distinct id over 100 draws", ids.size > 1)
    }

    private fun fieldValue(treasure: Treasure, field: String): String = when (field) {
        "title" -> treasure.title
        "hadith" -> treasure.hadith
        "narrator" -> treasure.narrator
        "source" -> treasure.source
        "grade" -> treasure.grade
        "explanation" -> treasure.explanation
        "action" -> treasure.action
        else -> error("Unknown field $field")
    }

    private companion object {
        val VALID_CHAPTER_IDS = setOf(
            ChapterId.DAILY_DHIKR,
            ChapterId.PRAYERS_MOSQUES,
            ChapterId.EXPIATION_REPENTANCE,
            ChapterId.RELIEF_RUQYAH,
            ChapterId.QURAN_VIRTUES,
            ChapterId.MORALS_RELATIONS,
            ChapterId.CHARITY_ONGOING,
            ChapterId.FASTING_SEASONS,
            ChapterId.MANNERS_SUNAN
        )
        val VALID_ICON_NAMES = setOf(
            ChapterIconName.Sun,
            ChapterIconName.Compass,
            ChapterIconName.Sparkles,
            ChapterIconName.ShieldCheck,
            ChapterIconName.BookOpen,
            ChapterIconName.HeartHandshake,
            ChapterIconName.Coins,
            ChapterIconName.Moon,
            ChapterIconName.ScrollText
        )
        val REQUIRED_FIELDS = listOf("title", "hadith", "narrator", "source", "grade", "explanation", "action")
    }
}