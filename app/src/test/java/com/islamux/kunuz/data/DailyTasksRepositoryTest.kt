package com.islamux.kunuz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DailyTasksRepositoryTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()

    // Fixed clock: 2026-03-16 12:00:00 UTC
    private val fixedClock: Clock = Clock.fixed(
        Instant.parse("2026-03-16T12:00:00Z"),
        ZoneOffset.UTC
    )

    private fun createDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(testDispatcher),
            produceFile = { tmpFolder.newFile("preferences.preferences_pb") }
        )

    private suspend fun DataStore<Preferences>.seed(streak: Int, last: String) {
        edit { prefs ->
            prefs[intPreferencesKey("sunnah-streak")] = streak
            prefs[stringPreferencesKey("sunnah-streak-last")] = last
        }
    }

    @Test
    fun `todayTasks emits empty map by default`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        assertEquals(emptyMap<String, Boolean>(), repo.todayTasks.first())
    }

    @Test
    fun `setAll persists today's map`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        val tasks = mapOf("task1" to true, "task2" to false)
        repo.setAll(tasks)
        assertEquals(tasks, repo.todayTasks.first())
    }

    @Test
    fun `setAll overwrites previous value`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.setAll(mapOf("task1" to true))
        repo.setAll(mapOf("task3" to true, "task4" to false))
        assertEquals(mapOf("task3" to true, "task4" to false), repo.todayTasks.first())
    }

    @Test
    fun `streak emits 0 by default`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        assertEquals(0, repo.streak.first())
    }

    @Test
    fun `recordStreakOnCompletion does NOT fire when not all taskIds true`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.recordStreakOnCompletion(
            listOf("task1", "task2"),
            mapOf("task1" to true, "task2" to false)
        )
        assertEquals(0, repo.streak.first())
    }

    @Test
    fun `recordStreakOnCompletion does NOT fire when taskIds empty`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.recordStreakOnCompletion(emptyList(), emptyMap())
        assertEquals(0, repo.streak.first())
    }

    @Test
    fun `recordStreakOnCompletion DOES fire when all true (first record = streak 1)`() = runTest {
        val dataStore = createDataStore()
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.recordStreakOnCompletion(
            listOf("task1", "task2"),
            mapOf("task1" to true, "task2" to true)
        )
        assertEquals(1, repo.streak.first())
    }

    @Test
    fun `recordStreakOnCompletion increments when last was yesterday`() = runTest {
        val dataStore = createDataStore()
        dataStore.seed(streak = 1, last = "2026-03-15")
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.recordStreakOnCompletion(
            listOf("task1"),
            mapOf("task1" to true)
        )
        assertEquals(2, repo.streak.first())
    }

    @Test
    fun `recordStreakOnCompletion no-op when already recorded today`() = runTest {
        val dataStore = createDataStore()
        dataStore.seed(streak = 5, last = "2026-03-16")
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.recordStreakOnCompletion(
            listOf("task1"),
            mapOf("task1" to true)
        )
        assertEquals(5, repo.streak.first())
    }

    @Test
    fun `recordStreakOnCompletion resets to 1 when last record is stale`() = runTest {
        val dataStore = createDataStore()
        dataStore.seed(streak = 7, last = "2026-03-13")
        val repo = DailyTasksRepository(dataStore, fixedClock)
        repo.recordStreakOnCompletion(
            listOf("task1"),
            mapOf("task1" to true)
        )
        assertEquals(1, repo.streak.first())
    }

    @Test
    fun `streak flow emits stored value when last is yesterday`() = runTest {
        val dataStore = createDataStore()
        dataStore.seed(streak = 3, last = "2026-03-15")
        val repo = DailyTasksRepository(dataStore, fixedClock)
        assertEquals(3, repo.streak.first())
    }

    @Test
    fun `streak flow emits stored value when last is today`() = runTest {
        val dataStore = createDataStore()
        dataStore.seed(streak = 5, last = "2026-03-16")
        val repo = DailyTasksRepository(dataStore, fixedClock)
        assertEquals(5, repo.streak.first())
    }

    @Test
    fun `streak flow emits 0 when last is stale`() = runTest {
        val dataStore = createDataStore()
        dataStore.seed(streak = 7, last = "2026-03-13")
        val repo = DailyTasksRepository(dataStore, fixedClock)
        assertEquals(0, repo.streak.first())
    }
}