package com.example.miformacionctma.data.remote

import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Evidencia

/**
 * Interfaz unificada para la integración con servicios web.
 * Define el contrato que el Repositorio utiliza, independientemente de la implementación (Retrofit, Mock, etc.)
 */
interface ActividadRemoteDataSource {
    /**
     * Obtiene la lista de actividades desde el servidor remoto.
     * Lanza excepciones en caso de error para permitir manejo resiliente.
     */
    suspend fun obtenerActividades(): List<ActividadFormativa>

    suspend fun crearActividad(actividad: ActividadFormativa): ActividadFormativa
    
    suspend fun actualizarActividad(actividad: ActividadFormativa)
    
    suspend fun eliminarActividad(id: Long)

    /**
     * Sube una evidencia al servidor remoto.
     */
    suspend fun subirEvidencia(evidencia: Evidencia)
}
