package com.islamux.kunuz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.islamux.kunuz.data.model.FontSize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    val fontSize: Flow<FontSize> = dataStore.data.map { prefs ->
        val raw = prefs[FONT_SIZE_KEY]
        if (raw == null) FontSize.NORMAL
        else runCatching { FontSize.valueOf(raw) }.getOrDefault(FontSize.NORMAL)
    }

    val showTashkeel: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[SHOW_TASHKEEL_KEY] ?: true
    }

    suspend fun setFontSize(value: FontSize) {
        dataStore.edit { prefs ->
            prefs[FONT_SIZE_KEY] = value.name
        }
    }

    suspend fun setShowTashkeel(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[SHOW_TASHKEEL_KEY] = value
        }
    }

    private companion object {
        val FONT_SIZE_KEY = stringPreferencesKey("sunnah-settings-font-size")
        val SHOW_TASHKEEL_KEY = booleanPreferencesKey("sunnah-settings-show-tashkeel")
    }
}