package com.example.miformacionctma.data.mapper

import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import com.example.miformacionctma.data.remote.dto.EvidenciaDto
import com.example.miformacionctma.domain.EstadoEvidencia
import com.example.miformacionctma.domain.Evidencia

fun EvidenciaEntity.toDomain(): Evidencia {
    val estadoEnum = runCatching { EstadoEvidencia.valueOf(this.estado) }
        .getOrDefault(EstadoEvidencia.LOCAL)
        
    return Evidencia(
        id = this.id,
        actividadId = this.actividadId,
        uri = this.uri,
        tipoMime = this.tipoMime,
        tamanio = this.tamanio,
        estado = estadoEnum
    )
}

fun Evidencia.toEntity(): EvidenciaEntity {
    return EvidenciaEntity(
        id = this.id,
        actividadId = this.actividadId,
        uri = this.uri,
        tipoMime = this.tipoMime,
        tamanio = this.tamanio,
        estado = this.estado.name
    )
}

fun EvidenciaDto.toEntity(): EvidenciaEntity {
    return EvidenciaEntity(
        id = this.id ?: 0L,
        actividadId = this.actividadId,
        uri = this.uri,
        tipoMime = this.tipoMime,
        tamanio = this.tamanio,
        estado = this.estado
    )
}

fun EvidenciaDto.toDomain(): Evidencia {
    val estadoEnum = runCatching { EstadoEvidencia.valueOf(this.estado) }
        .getOrDefault(EstadoEvidencia.LOCAL)
        
    return Evidencia(
        id = this.id ?: 0L,
        actividadId = this.actividadId,
        uri = this.uri,
        tipoMime = this.tipoMime,
        tamanio = this.tamanio,
        estado = estadoEnum
    )
}

fun Evidencia.toDto(): EvidenciaDto {
    return EvidenciaDto(
        id = if (this.id == 0L) null else this.id,
        actividadId = this.actividadId,
        uri = this.uri,
        tipoMime = this.tipoMime,
        tamanio = this.tamanio,
        estado = this.estado.name
    )
}
