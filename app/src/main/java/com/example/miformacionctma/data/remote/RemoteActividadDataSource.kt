package com.example.miformacionctma.data.remote

import com.example.miformacionctma.data.remote.api.ActividadApiService
import com.example.miformacionctma.data.remote.dto.ActividadDto
import retrofit2.HttpException
import java.io.IOException

class RemoteActividadDataSource(
    private val apiService: ActividadApiService
) {

    suspend fun obtenerActividades(): Result<List<ActividadDto>> {
        return try {
            val response = apiService.obtenerActividades()
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. Revisa tu internet."))
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Sesión expirada. Inicia sesión de nuevo."))
                404 -> Result.failure(Exception("Recurso no encontrado."))
                else -> Result.failure(Exception("Error del servidor: ${e.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearActividad(actividad: ActividadDto): Result<ActividadDto> {
        return try {
            val response = apiService.crearActividad(actividad)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarActividad(id: Long, actividad: ActividadDto): Result<ActividadDto> {
        return try {
            val response = apiService.actualizarActividad(id, actividad)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarActividad(id: Long): Result<Unit> {
        return try {
            apiService.eliminarActividad(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
