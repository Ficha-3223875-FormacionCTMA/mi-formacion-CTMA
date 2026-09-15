package com.example.miformacionctma.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.miformacionctma.data.local.DataStoreProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenciasRepository(
    context: Context
) {

    private val dataStore = DataStoreProvider.getDataStore(context)

    private object Keys {
        val TEXTO_BUSQUEDA: Preferences.Key<String> =
            stringPreferencesKey("texto_busqueda")
    }

    val textoBusqueda: Flow<String> =
        dataStore.data.map { preferences ->
            preferences[Keys.TEXTO_BUSQUEDA] ?: ""
        }

    suspend fun guardarTextoBusqueda(texto: String) {
        dataStore.edit { preferences ->
            preferences[Keys.TEXTO_BUSQUEDA] = texto
        }
    }
}

