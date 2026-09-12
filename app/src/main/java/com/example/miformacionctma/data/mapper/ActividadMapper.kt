package com.example.miformacionctma.data.mapper

import com.example.miformacionctma.data.local.entity.ActividadFormativa as ActividadEntity
import com.example.miformacionctma.domain.ActividadFormativa as ActividadDomain
import com.example.miformacionctma.domain.Prioridad

fun ActividadDomain.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        progreso = progreso,
        diasRestantes = diasRestantes,
        prioridad = prioridad.name
    )
}

fun ActividadEntity.toDomain(): ActividadDomain {
    return ActividadDomain(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        progreso = progreso,
        diasRestantes = diasRestantes,
        prioridad = try {
            Prioridad.valueOf(prioridad)
        } catch (e: IllegalArgumentException) {
            Prioridad.BAJA
        }
    )
}