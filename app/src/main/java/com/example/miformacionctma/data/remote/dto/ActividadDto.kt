package com.example.miformacionctma.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActividadDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("titulo")
    val titulo: String,
    @SerialName("descripcion")
    val descripcion: String? = null,
    @SerialName("progreso")
    val progreso: Int = 0,
    @SerialName("dias_restantes")
    val diasRestantes: Int = 0,
    @SerialName("prioridad")
    val prioridad: String = "BAJA",
    @SerialName("resuelto")
    val resuelto: Boolean = false
)
