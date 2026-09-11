package com.islamux.kunuz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.islamux.kunuz.logic.effectiveStreak
import com.islamux.kunuz.logic.localDateKey
import com.islamux.kunuz.logic.nextStreak
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Clock
import java.time.LocalDate

class DailyTasksRepository(
    private val dataStore: DataStore<Preferences>,
    private val clock: Clock = Clock.systemDefaultZone(),
) {

    private val json = Json { ignoreUnknownKeys = true }

    val todayTasks: Flow<Map<String, Boolean>> = dataStore.data.map { prefs ->
        val raw = prefs[tasksKey(todayKey())] ?: return@map emptyMap()
        runCatching { json.decodeFromString<Map<String, Boolean>>(raw) }
            .getOrDefault(emptyMap())
    }

    val streak: Flow<Int> = dataStore.data.map { prefs ->
        val today = todayKey()
        val yesterday = yesterdayKey()
        val last = prefs[STREAK_LAST_KEY]
        val stored = prefs[STREAK_KEY] ?: 0
        effectiveStreak(last, stored, today, yesterday)
    }

    suspend fun setAll(tasks: Map<String, Boolean>) {
        dataStore.edit { prefs ->
            prefs[tasksKey(todayKey())] = json.encodeToString<Map<String, Boolean>>(tasks)
        }
    }

    suspend fun recordStreakOnCompletion(
        taskIds: List<String>,
        completed: Map<String, Boolean>,
    ) {
        if (taskIds.isEmpty()) return
        if (!taskIds.all { completed[it] == true }) return
        val today = todayKey()
        val yesterday = yesterdayKey()
        dataStore.edit { prefs ->
            val last = prefs[STREAK_LAST_KEY]
            val current = prefs[STREAK_KEY] ?: 0
            val next = nextStreak(last, current, today, yesterday) ?: return@edit
            prefs[STREAK_KEY] = next.first
            prefs[STREAK_LAST_KEY] = next.second
        }
    }

    private fun todayKey(): String = localDateKey(LocalDate.now(clock))

    private fun yesterdayKey(): String = localDateKey(LocalDate.now(clock).minusDays(1))

    private fun tasksKey(day: String): Preferences.Key<String> = stringPreferencesKey(TASKS_KEY_PREFIX + day)

    private companion object {
        val STREAK_KEY = intPreferencesKey("sunnah-streak")
        val STREAK_LAST_KEY = stringPreferencesKey("sunnah-streak-last")
        const val TASKS_KEY_PREFIX = "sunnah-tasks-"
    }
}