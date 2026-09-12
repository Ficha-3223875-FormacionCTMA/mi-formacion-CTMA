package com.example.miformacionctma.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
// Representacion de tabla actividades
@Entity(tableName = "actividades")
data class ActividadFormativa(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val titulo: String,

    val descripcion: String? = null,

    val progreso: Int = 0,

    val diasRestantes: Int = 0,

    val prioridad: String = "BAJA"
)