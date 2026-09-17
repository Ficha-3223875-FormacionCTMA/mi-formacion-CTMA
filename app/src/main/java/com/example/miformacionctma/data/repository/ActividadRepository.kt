package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.mapper.*
import com.example.miformacionctma.data.remote.RemoteActividadDataSource
import com.example.miformacionctma.data.remote.dto.ActividadDto
import com.example.miformacionctma.domain.ActividadFormativa
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ActividadRepository(
    private val actividadDao: ActividadDao,
    private val remoteDataSource: RemoteActividadDataSource,
    private val preferenciasRepository: PreferenciasRepository
) {

    fun obtenerTodas(): Flow<List<ActividadFormativa>> {
        return actividadDao.obtenerTodas()
            .map { actividades ->
                actividades.map { it.toDomain() }
            }
    }

    fun obtenerPorId(id: Long): Flow<ActividadFormativa?> {
        return actividadDao.obtenerPorId(id)
            .map { it?.toDomain() }
    }

    fun buscarPorTexto(query: String): Flow<List<ActividadFormativa>> {
        return actividadDao.buscarPorTexto(query)
            .map { actividades ->
                actividades.map { it.toDomain() }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun obtenerActividadesFiltradas(): Flow<List<ActividadFormativa>> {
        return preferenciasRepository.textoBusqueda
            .flatMapLatest { texto ->
                val consulta = texto.trim()

                if (consulta.isBlank()) {
                    actividadDao.obtenerTodas()
                } else {
                    actividadDao.buscarPorTexto(consulta)
                }
            }
            .map { actividades ->
                actividades.map { it.toDomain() }
            }
    }

    suspend fun guardarTextoBusqueda(texto: String) {
        preferenciasRepository.guardarTextoBusqueda(texto)
    }

    suspend fun refreshActividades(): Result<Unit> {
        val result = remoteDataSource.obtenerActividades()
        return if (result.isSuccess) {
            val dtos = result.getOrNull() ?: emptyList()
            // Sincronizar con Room: por simplicidad, insertamos/reemplazamos todo
            dtos.forEach { dto ->
                actividadDao.insertar(dto.toEntity())
            }
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
        }
    }

    suspend fun insertar(actividad: ActividadFormativa): Long {
        val idLocal = actividadDao.insertar(actividad.toEntity())
        // Sincronización remota opcional o según requerimiento
        remoteDataSource.crearActividad(actividad.copy(id = idLocal).toDto())
        return idLocal
    }

    suspend fun actualizar(actividad: ActividadFormativa) {
        actividadDao.actualizar(actividad.toEntity())
        remoteDataSource.actualizarActividad(actividad.id, actividad.toDto())
    }

    suspend fun eliminar(actividad: ActividadFormativa) {
        actividadDao.eliminar(actividad.toEntity())
        remoteDataSource.eliminarActividad(actividad.id)
    }
}
