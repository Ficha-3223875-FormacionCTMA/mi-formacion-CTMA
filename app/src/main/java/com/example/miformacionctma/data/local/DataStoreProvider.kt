package com.example.miformacionctma.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = "preferencias_ctma"
)

object DataStoreProvider {

    fun getDataStore(context: Context): androidx.datastore.core.DataStore<Preferences> {
        return context.applicationContext.dataStore
    }
}

