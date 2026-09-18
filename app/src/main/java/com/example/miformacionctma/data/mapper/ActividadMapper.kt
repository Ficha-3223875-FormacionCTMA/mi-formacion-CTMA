package com.example.miformacionctma.data.mapper

import com.example.miformacionctma.data.local.entity.ActividadFormativa as ActividadEntity
import com.example.miformacionctma.data.remote.dto.ActividadDto
import com.example.miformacionctma.domain.ActividadFormativa as ActividadDomain
import com.example.miformacionctma.domain.Prioridad

fun ActividadEntity.toDomain(): ActividadDomain {
    val prioridadEnum = runCatching { Prioridad.valueOf(this.prioridad) }.getOrDefault(Prioridad.BAJA)
    return ActividadDomain(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        progreso = this.progreso,
        diasRestantes = this.diasRestantes,
        prioridad = prioridadEnum,
        resuelto = this.resuelto
    )
}

fun ActividadDomain.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        progreso = this.progreso,
        diasRestantes = this.diasRestantes,
        prioridad = this.prioridad.name,
        resuelto = this.resuelto
    )
}

fun ActividadDto.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id ?: 0L,
        titulo = this.titulo,
        descripcion = this.descripcion,
        progreso = this.progreso,
        diasRestantes = this.diasRestantes,
        prioridad = this.prioridad,
        resuelto = this.resuelto
    )
}

fun ActividadDto.toDomain(): ActividadDomain {
    val prioridadEnum = runCatching { Prioridad.valueOf(this.prioridad) }.getOrDefault(Prioridad.BAJA)
    return ActividadDomain(
        id = this.id ?: 0L,
        titulo = this.titulo,
        descripcion = this.descripcion,
        progreso = this.progreso,
        diasRestantes = this.diasRestantes,
        prioridad = prioridadEnum,
        resuelto = this.resuelto
    )
}

fun ActividadDomain.toDto(): ActividadDto {
    return ActividadDto(
        id = if (this.id == 0L) null else this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        progreso = this.progreso,
        diasRestantes = this.diasRestantes,
        prioridad = this.prioridad.name,
        resuelto = this.resuelto
    )
}
