package com.example.miformacionctma.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.miformacionctma.data.local.entity.ActividadFormativa
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Query("SELECT * FROM actividades ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<ActividadFormativa>>

    @Query("SELECT * FROM actividades WHERE id = :id")
    fun obtenerPorId(id: Long): Flow<ActividadFormativa?>

    @Query("SELECT * FROM actividades WHERE titulo LIKE '%' || :query || '%' OR descripcion LIKE '%' || :query || '%' ORDER BY id DESC")
    fun buscarPorTexto(query: String): Flow<List<ActividadFormativa>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(actividad: ActividadFormativa): Long

    @Update
    suspend fun actualizar(actividad: ActividadFormativa)

    @Delete
    suspend fun eliminar(actividad: ActividadFormativa)
}