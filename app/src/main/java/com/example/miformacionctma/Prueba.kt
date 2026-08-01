package com.example.miformacionctma
import com.example.miformacionctma.domain.*
import com.example.miformacionctma.domain.ReglasActividad.estadoActividad
import com.example.miformacionctma.domain.ReglasActividad.validarActividad

fun main() {

    val actividad1 = ActividadFormativa(
        id = 1,
        titulo = "Taller Kotlin",
        descripcion = "Practicar funciones",
        progreso = 40,
        diasRestantes = 2,
        prioridad = Prioridad.ALTA
    )

    println(validarActividad(actividad1))
    println(estadoActividad(actividad1))
}

