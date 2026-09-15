package com.example.miformacionctma.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.local.entity.ActividadFormativa
//Establece conexion con la bases de datos
@Database(
    entities = [ActividadFormativa::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao

    companion object {
        val MIGRATION_1_2 = object : androidx.room3.migration.Migration(1, 2) {
            override suspend fun migrate(connection: androidx.sqlite.SQLiteConnection) {
                val statement = connection.prepare("ALTER TABLE actividades ADD COLUMN resuelto INTEGER NOT NULL DEFAULT 0")
                try {
                    statement.step()
                } finally {
                    statement.close()
                }
            }
        }
    }
}