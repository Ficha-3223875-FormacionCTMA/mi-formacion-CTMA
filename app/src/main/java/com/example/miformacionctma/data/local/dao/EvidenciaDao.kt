package com.example.miformacionctma.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvidenciaDao {

    @Query("SELECT * FROM evidencias WHERE actividadId = :actividadId")
    fun obtenerPorActividad(actividadId: Long): Flow<List<EvidenciaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(evidencia: EvidenciaEntity): Long

    @Update
    suspend fun actualizar(evidencia: EvidenciaEntity)

    @Delete
    suspend fun eliminar(evidencia: EvidenciaEntity)
    
    @Query("SELECT * FROM evidencias WHERE estado = 'LOCAL' OR estado = 'FALLIDA'")
    suspend fun obtenerPendientesSincronizacion(): List<EvidenciaEntity>

    @Query("UPDATE evidencias SET estado = :nuevoEstado WHERE id = :id")
    suspend fun actualizarEstado(id: Long, nuevoEstado: String)
}
