package com.example.miformacionctma.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvidenciaDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("actividad_id")
    val actividadId: Long,
    @SerialName("uri")
    val uri: String,
    @SerialName("tipo_mime")
    val tipoMime: String,
    @SerialName("tamanio")
    val tamanio: Long,
    @SerialName("estado")
    val estado: String
)
