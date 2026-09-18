package com.example.miformacionctma.data.remote

import android.content.ContentResolver
import android.net.Uri
import com.example.miformacionctma.data.mapper.toDomain
import com.example.miformacionctma.data.mapper.toDto
import com.example.miformacionctma.data.remote.api.ActividadApiService
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Evidencia
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Implementación de Retrofit para la fuente de datos remota.
 * Integra la lógica de Miguel con el contrato unificado.
 */
class RemoteActividadDataSource(
    private val apiService: ActividadApiService,
    private val contentResolver: ContentResolver
) : ActividadRemoteDataSource {

    override suspend fun obtenerActividades(): List<ActividadFormativa> {
        return try {
            val response = apiService.obtenerActividades()
            response.map { it.toDomain() }
        } catch (e: SocketTimeoutException) {
            throw Exception("El servidor tardó demasiado en responder. Reintenta en un momento.")
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

    override suspend fun subirEvidencia(evidencia: Evidencia) {
        try {
            val uri = Uri.parse(evidencia.uri)
            val inputStream = contentResolver.openInputStream(uri)
                ?: throw Exception("No se pudo leer el archivo de la evidencia")
            
            val bytes = inputStream.use { it.readBytes() }
            val requestFile = bytes.toRequestBody(evidencia.tipoMime.toMediaTypeOrNull())
            
            val body = MultipartBody.Part.createFormData(
                "imagen",
                "evidencia_${evidencia.actividadId}_${System.currentTimeMillis()}",
                requestFile
            )
            
            val actividadIdBody = evidencia.actividadId.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())

            apiService.subirEvidencia(actividadIdBody, body)
        } catch (e: Exception) {
            // Re-lanzar para que el repositorio maneje el estado FALLIDA
            throw e
        }
    }
}
