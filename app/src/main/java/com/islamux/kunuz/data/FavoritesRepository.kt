package com.islamux.kunuz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FavoritesRepository(private val dataStore: DataStore<Preferences>) {

    private val json = Json { ignoreUnknownKeys = true }

    val favorites: Flow<Set<Int>> = dataStore.data.map { prefs ->
        val raw = prefs[FAVORITES_KEY] ?: return@map emptySet()
        runCatching { json.decodeFromString<List<Int>>(raw).toSet() }
            .getOrDefault(emptySet())
    }

    suspend fun toggle(id: Int) {
        dataStore.edit { prefs ->
            val raw = prefs[FAVORITES_KEY]
            val current = if (raw == null) {
                emptySet()
            } else {
                runCatching { json.decodeFromString<List<Int>>(raw).toSet() }
                    .getOrDefault(emptySet())
            }
            val next = if (id in current) current - id else current + id
            prefs[FAVORITES_KEY] = json.encodeToString<List<Int>>(next.sorted())
        }
    }

    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs[FAVORITES_KEY] = json.encodeToString<List<Int>>(emptyList())
        }
    }

    private companion object {
        val FAVORITES_KEY = stringPreferencesKey("sunnah-favorites")
    }
}