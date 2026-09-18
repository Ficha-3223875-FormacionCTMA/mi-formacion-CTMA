package com.example.miformacionctma.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.local.dao.EvidenciaDao
import com.example.miformacionctma.data.local.entity.ActividadFormativa
import com.example.miformacionctma.data.local.entity.EvidenciaEntity

//Establece conexion con la bases de datos
@Database(
    entities = [
        ActividadFormativa::class,
        EvidenciaEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao
    abstract fun evidenciaDao(): EvidenciaDao

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

        val MIGRATION_2_3 = object : androidx.room3.migration.Migration(2, 3) {
            override suspend fun migrate(connection: androidx.sqlite.SQLiteConnection) {
                // Crear la tabla de evidencias
                val createTable = """
                    CREATE TABLE IF NOT EXISTS `evidencias` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `actividadId` INTEGER NOT NULL, 
                        `uri` TEXT NOT NULL, 
                        `tipoMime` TEXT NOT NULL, 
                        `tamanio` INTEGER NOT NULL, 
                        `estado` TEXT NOT NULL, 
                        FOREIGN KEY(`actividadId`) REFERENCES `actividades`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """.trimIndent()
                
                val createIndex = "CREATE INDEX IF NOT EXISTS `index_evidencias_actividadId` ON `evidencias` (`actividadId`)"
                
                connection.prepare(createTable).use { it.step() }
                connection.prepare(createIndex).use { it.step() }
            }
        }
    }
}
