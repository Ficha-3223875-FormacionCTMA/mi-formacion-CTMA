package com.example.miformacionctma.data.remote

import com.example.miformacionctma.domain.ActividadFormativa

/**
 * Interfaz mínima para la integración con servicios web.
 * Punto de integración para Miguel (Semana 8).
 */
interface ActividadRemoteDataSource {
    /**
     * Obtiene la lista de actividades desde el servidor remoto.
     */
    suspend fun obtenerActividades(): List<ActividadFormativa>
}
