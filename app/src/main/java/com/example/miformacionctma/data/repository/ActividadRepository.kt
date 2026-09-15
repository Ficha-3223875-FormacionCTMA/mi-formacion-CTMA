package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.mapper.toDomain
import com.example.miformacionctma.data.mapper.toEntity
import com.example.miformacionctma.domain.ActividadFormativa
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ActividadRepository(
    private val actividadDao: ActividadDao,
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

    suspend fun insertar(actividad: ActividadFormativa): Long {
        return actividadDao.insertar(actividad.toEntity())
    }

    suspend fun actualizar(actividad: ActividadFormativa) {
        actividadDao.actualizar(actividad.toEntity())
    }

    suspend fun eliminar(actividad: ActividadFormativa) {
        actividadDao.eliminar(actividad.toEntity())
    }
}

