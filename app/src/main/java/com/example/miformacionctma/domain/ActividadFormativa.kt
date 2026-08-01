package com.example.miformacionctma.domain
enum class Prioridad { BAJA, MEDIA, ALTA }

data class ActividadFormativa(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val progreso: Int,
    val diasRestantes: Int,
    val prioridad: Prioridad
)