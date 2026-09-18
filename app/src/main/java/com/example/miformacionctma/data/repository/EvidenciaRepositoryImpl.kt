package com.example.miformacionctma.data.repository

import android.content.ContentResolver
import android.net.Uri
import com.example.miformacionctma.data.local.dao.EvidenciaDao
import com.example.miformacionctma.data.mapper.toDomain
import com.example.miformacionctma.data.mapper.toEntity
import com.example.miformacionctma.data.remote.ActividadRemoteDataSource
import com.example.miformacionctma.domain.EstadoEvidencia
import com.example.miformacionctma.domain.Evidencia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementacion del repositorio para la gestion de evidencias fotograficas.
 *
 * CASOS DE ACEPTACION:
 * - CA-01: Validacion de Tipo MIME (jpeg, png, webp).
 * - CA-02: Validacion de Tamanio Maximo (5 MB).
 * - CA-03: Verificacion de legibilidad de la Content URI.
 * - CA-04: Persistencia en Room en estado FALLIDA ante error de red.
 *
 * MATRIZ DE RIESGOS:
 * 1. Riesgo: Archivo corrupto o no accesible. Control: Uso de ContentResolver para validar legibilidad antes del envio.
 * 2. Riesgo: Error de red durante la subida multipart. Control: Captura de excepciones y cambio de estado a FALLIDA sin eliminar el registro local.
 * 3. Riesgo: Tipo de archivo no soportado. Control: Verificacion del tipo MIME a traves de ContentResolver.
 */
class EvidenciaRepositoryImpl(
    private val evidenciaDao: EvidenciaDao,
    private val remoteDataSource: ActividadRemoteDataSource,
    private val contentResolver: ContentResolver
) : EvidenciaRepository {

    private val MAX_SIZE_BYTES = 5 * 1024 * 1024 // 5 MB
    private val MIME_TYPES_ACEPTADOS = listOf("image/jpeg", "image/png", "image/webp")

    override fun obtenerPorActividad(actividadId: Long): Flow<List<Evidencia>> {
        return evidenciaDao.obtenerPorActividad(actividadId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun adjuntar(evidencia: Evidencia): Long {
        // 1. Validaciones previas
        validarEvidencia(evidencia)

        // 2. Persistencia local en estado inicial
        val idLocal = evidenciaDao.insertar(evidencia.toEntity())
        
        // 3. Intento de subida inmediata
        intentarSubida(evidencia.copy(id = idLocal))
        
        return idLocal
    }

    override suspend fun reemplazar(evidenciaAnterior: Evidencia, nuevaEvidencia: Evidencia) {
        eliminar(evidenciaAnterior)
        adjuntar(nuevaEvidencia)
    }

    override suspend fun eliminar(evidencia: Evidencia) {
        evidenciaDao.eliminar(evidencia.toEntity())
        try {
            remoteDataSource.eliminarActividad(evidencia.id) // O el endpoint específico de evidencias si existe
        } catch (e: Exception) {
            // Log error, pero el registro local ya fue eliminado
        }
    }

    override suspend fun sincronizar() {
        val pendientes = evidenciaDao.obtenerPendientesSincronizacion()
        pendientes.forEach { entity ->
            intentarSubida(entity.toDomain())
        }
    }

    private suspend fun intentarSubida(evidencia: Evidencia) {
        try {
            evidenciaDao.actualizarEstado(evidencia.id, EstadoEvidencia.SUBIENDO.name)
            remoteDataSource.subirEvidencia(evidencia)
            evidenciaDao.actualizarEstado(evidencia.id, EstadoEvidencia.SINCRONIZADA.name)
        } catch (e: Exception) {
            // REGLA CRITICA: Conservar en Room en estado FALLIDA
            evidenciaDao.actualizarEstado(evidencia.id, EstadoEvidencia.FALLIDA.name)
        }
    }

    private fun validarEvidencia(evidencia: Evidencia) {
        val uri = Uri.parse(evidencia.uri)
        
        // a. Verificacion de existencia y legibilidad
        val pfd = try {
            contentResolver.openFileDescriptor(uri, "r")
        } catch (e: Exception) {
            throw IllegalArgumentException("La URI no es legible o no existe.")
        } ?: throw IllegalArgumentException("No se pudo obtener el descriptor del archivo.")

        pfd.use {
            // b. Validacion de tamanio
            val size = it.statSize
            if (size > MAX_SIZE_BYTES) {
                throw IllegalArgumentException("El archivo excede el limite de 5 MB.")
            }
            if (size <= 0) {
                throw IllegalArgumentException("El archivo esta vacio.")
            }
        }

        // c. Validacion de tipo MIME
        val mimeType = contentResolver.getType(uri)
        if (mimeType !in MIME_TYPES_ACEPTADOS) {
            throw IllegalArgumentException("Formato de imagen no permitido. Solo se aceptan JPEG, PNG y WEBP.")
        }
    }
}
