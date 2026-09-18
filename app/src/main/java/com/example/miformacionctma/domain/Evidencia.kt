package com.example.miformacionctma.domain

/**
 * Representa los posibles estados de una evidencia en el flujo de sincronización.
 */
enum class EstadoEvidencia {
    LOCAL,        // Guardada solo en el dispositivo
    SUBIENDO,     // En proceso de envío al servidor
    SINCRONIZADA, // Confirmada por el servidor
    FALLIDA       // Error en el último intento de subida
}

/**
 * Modelo de dominio para la evidencia fotográfica adjunta a una actividad.
 */
data class Evidencia(
    val id: Long = 0L,
    val actividadId: Long,
    val uri: String,
    val tipoMime: String,
    val tamanio: Long,
    val estado: EstadoEvidencia = EstadoEvidencia.LOCAL
)
