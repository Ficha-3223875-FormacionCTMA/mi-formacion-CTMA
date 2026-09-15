package com.example.miformacionctma.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferenciasRepository(private val context: Context) {

    private object PreferencesKeys {
        val FILTRO_CATEGORIA = stringPreferencesKey("filtro_categoria")
    }

    val filtroCategoria: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FILTRO_CATEGORIA] ?: "TODAS"
        }

    suspend fun guardarFiltroCategoria(categoria: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FILTRO_CATEGORIA] = categoria
        }
    }
}