package com.example.miformacionctma.data.repository

import com.example.miformacionctma.domain.Evidencia
import kotlinx.coroutines.flow.Flow

/**
 * Contrato para la gestión de evidencias fotográficas.
 * Desbloquea a JD (UI) y Laverde (Sincronización).
 */
interface EvidenciaRepository {

    /**
     * Obtiene las evidencias asociadas a una actividad.
     */
    fun obtenerPorActividad(actividadId: Long): Flow<List<Evidencia>>

    /**
     * Registra una nueva evidencia localmente.
     */
    suspend fun adjuntar(evidencia: Evidencia): Long

    /**
     * Reemplaza una evidencia existente por una nueva.
     */
    suspend fun reemplazar(evidenciaAnterior: Evidencia, nuevaEvidencia: Evidencia)

    /**
     * Elimina una evidencia tanto local como remotamente.
     */
    suspend fun eliminar(evidencia: Evidencia)

    /**
     * Sincroniza las evidencias pendientes (LOCAL o FALLIDA) con el servidor.
     */
    suspend fun sincronizar()
}
