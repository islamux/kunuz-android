package com.islamux.kunuz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FavoritesRepositoryTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(testDispatcher),
            produceFile = { tmpFolder.newFile("preferences.preferences_pb") }
        )

    @Test
    fun `default emits emptySet when no key stored`() = runTest {
        val dataStore = createDataStore()
        val repo = FavoritesRepository(dataStore)
        assertEquals(emptySet<Int>(), repo.favorites.first())
    }

    @Test
    fun `toggle adds new id`() = runTest {
        val dataStore = createDataStore()
        val repo = FavoritesRepository(dataStore)
        repo.toggle(42)
        assertEquals(setOf(42), repo.favorites.first())
    }

    @Test
    fun `toggle removes existing id`() = runTest {
        val dataStore = createDataStore()
        val repo = FavoritesRepository(dataStore)
        repo.toggle(5)
        assertEquals(setOf(5), repo.favorites.first())
        repo.toggle(5)
        assertEquals(emptySet<Int>(), repo.favorites.first())
    }

    @Test
    fun `toggle twice removes`() = runTest {
        val dataStore = createDataStore()
        val repo = FavoritesRepository(dataStore)
        repo.toggle(7)
        repo.toggle(7)
        assertEquals(emptySet<Int>(), repo.favorites.first())
    }

    @Test
    fun `clear empties`() = runTest {
        val dataStore = createDataStore()
        val repo = FavoritesRepository(dataStore)
        repo.toggle(1)
        repo.toggle(2)
        repo.toggle(3)
        assertEquals(setOf(1, 2, 3), repo.favorites.first())
        repo.clear()
        assertEquals(emptySet<Int>(), repo.favorites.first())
    }

    @Test
    fun `toggle multiple ids accumulates`() = runTest {
        val dataStore = createDataStore()
        val repo = FavoritesRepository(dataStore)
        repo.toggle(10)
        repo.toggle(20)
        repo.toggle(30)
        assertEquals(setOf(10, 20, 30), repo.favorites.first())
    }

    @Test
    fun `persists across DataStore reads`() = runTest {
        val dataStore = createDataStore()
        val repo1 = FavoritesRepository(dataStore)
        repo1.toggle(100)
        repo1.toggle(200)
        val repo2 = FavoritesRepository(dataStore)
        assertEquals(setOf(100, 200), repo2.favorites.first())
    }
}
