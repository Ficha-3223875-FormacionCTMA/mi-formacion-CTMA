package com.example.miformacionctma.data.remote

import com.example.miformacionctma.data.mapper.toDomain
import com.example.miformacionctma.data.mapper.toDto
import com.example.miformacionctma.data.remote.api.ActividadApiService
import com.example.miformacionctma.domain.ActividadFormativa
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementación de Retrofit para la fuente de datos remota.
 * Integra la lógica de Miguel con el contrato unificado.
 */
class RemoteActividadDataSource(
    private val apiService: ActividadApiService
) : ActividadRemoteDataSource {

    override suspend fun obtenerActividades(): List<ActividadFormativa> {
        return try {
            val response = apiService.obtenerActividades()
            response.map { it.toDomain() }
        } catch (e: IOException) {
            throw Exception("Error de conexión. Revisa tu internet.")
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                401 -> "Sesión expirada. Inicia sesión de nuevo."
                404 -> "Recurso no encontrado."
                else -> "Error del servidor: ${e.code()}"
            }
            throw Exception(mensaje)
        }
    }

    override suspend fun crearActividad(actividad: ActividadFormativa): ActividadFormativa {
        return apiService.crearActividad(actividad.toDto()).toDomain()
    }

    override suspend fun actualizarActividad(actividad: ActividadFormativa) {
        apiService.actualizarActividad(actividad.id, actividad.toDto())
    }

    override suspend fun eliminarActividad(id: Long) {
        apiService.eliminarActividad(id)
    }
}
