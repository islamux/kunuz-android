package com.islamux.kunuz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.islamux.kunuz.data.model.FontSize
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SettingsRepositoryTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(testDispatcher),
            produceFile = { tmpFolder.newFile("preferences.preferences_pb") }
        )

    @Test
    fun `defaults to NORMAL and showTashkeel true on first`() = runTest {
        val dataStore = createDataStore()
        val repo = SettingsRepository(dataStore)
        assertEquals(FontSize.NORMAL, repo.fontSize.first())
        assertEquals(true, repo.showTashkeel.first())
    }

    @Test
    fun `setFontSize round-trips`() = runTest {
        val dataStore = createDataStore()
        val repo = SettingsRepository(dataStore)
        repo.setFontSize(FontSize.LARGE)
        assertEquals(FontSize.LARGE, repo.fontSize.first())
    }

    @Test
    fun `setShowTashkeel round-trips`() = runTest {
        val dataStore = createDataStore()
        val repo = SettingsRepository(dataStore)
        repo.setShowTashkeel(false)
        assertEquals(false, repo.showTashkeel.first())
    }

    @Test
    fun `invalid stored fontSize falls back to default`() = runTest {
        val dataStore = createDataStore()
        // Write a garbage value directly
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey("sunnah-settings-font-size")] = "GARBAGE_VALUE"
        }
        val repo = SettingsRepository(dataStore)
        assertEquals(FontSize.NORMAL, repo.fontSize.first())
    }

    @Test
    fun `missing keys emit defaults`() = runTest {
        val dataStore = createDataStore()
        val repo = SettingsRepository(dataStore)
        // Nothing stored — verify defaults
        assertEquals(FontSize.NORMAL, repo.fontSize.first())
        assertEquals(true, repo.showTashkeel.first())
    }

    @Test
    fun `setFontSize XLARGE round-trips`() = runTest {
        val dataStore = createDataStore()
        val repo = SettingsRepository(dataStore)
        repo.setFontSize(FontSize.XLARGE)
        assertEquals(FontSize.XLARGE, repo.fontSize.first())
    }

    @Test
    fun `changes persist across repository instances`() = runTest {
        val dataStore = createDataStore()
        val repo1 = SettingsRepository(dataStore)
        repo1.setFontSize(FontSize.LARGE)
        repo1.setShowTashkeel(false)
        val repo2 = SettingsRepository(dataStore)
        assertEquals(FontSize.LARGE, repo2.fontSize.first())
        assertEquals(false, repo2.showTashkeel.first())
    }
}
