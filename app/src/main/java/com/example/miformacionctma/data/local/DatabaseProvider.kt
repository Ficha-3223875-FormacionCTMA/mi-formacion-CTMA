package com.example.miformacionctma.data.local

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "mi_formacion_ctma.db"
            )
                .setDriver(AndroidSQLiteDriver())
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}