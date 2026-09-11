package com.islamux.kunuz.data

import android.content.Context
import com.islamux.kunuz.data.model.Chapter
import com.islamux.kunuz.data.model.ChapterId
import com.islamux.kunuz.data.model.Treasure
import kotlinx.serialization.json.Json
import java.time.LocalDate
import kotlin.math.abs
import kotlin.random.Random

class TreasuresRepository(context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    val allTreasures: List<Treasure> = parseData(context, "data/treasures.json")
    val chapters: List<Chapter> = parseData(context, "data/chapters.json")
    val chapterCounts: Map<ChapterId, Int> = allTreasures.groupingBy { it.chapterId }.eachCount()
    val total: Int = allTreasures.size

    fun getTreasureById(id: Int): Treasure? = allTreasures.find { it.id == id }

    fun getDailyTreasure(today: LocalDate = LocalDate.now()): Treasure =
        allTreasures[abs(today.dayOfYear) % allTreasures.size]

    fun getRandomTreasure(random: Random = Random.Default): Treasure =
        allTreasures[random.nextInt(allTreasures.size)]

    private inline fun <reified T> parseData(context: Context, assetPath: String): T {
        val text = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        return json.decodeFromString(text)
    }
}