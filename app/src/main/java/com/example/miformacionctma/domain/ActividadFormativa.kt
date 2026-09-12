package com.example.miformacionctma.domain
enum class Prioridad { BAJA, MEDIA, ALTA }

data class ActividadFormativa(
    val id: Long = 0L,
    val titulo: String,
    val descripcion: String? = null,
    val progreso: Int = 0,
    val diasRestantes: Int = 0,
    val prioridad: Prioridad = Prioridad.BAJA
)