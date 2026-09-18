package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.mapper.toDomain
import com.example.miformacionctma.data.mapper.toEntity
import com.example.miformacionctma.data.remote.ActividadRemoteDataSource
import com.example.miformacionctma.domain.ActividadFormativa
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class ActividadRepository(
    private val actividadDao: ActividadDao,
    private val preferenciasRepository: PreferenciasRepository,
    private val remoteDataSource: ActividadRemoteDataSource
) {

    /**
     * Sincroniza los datos locales con el servidor remoto.
     * Si la red falla, la excepción se propaga al ViewModel para manejo resiliente.
     */
    suspend fun sincronizar() {
        val actividadesRemotas = remoteDataSource.obtenerActividades()
        // Sincronizar con Room: reemplazamos/actualizamos datos locales con los remotos.
        actividadesRemotas.forEach { actividad ->
            actividadDao.insertar(actividad.toEntity())
        }
    }

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

    suspend fun insertar(actividad: ActividadFormativa): Long {
        val idLocal = actividadDao.insertar(actividad.toEntity())
        try {
            remoteDataSource.crearActividad(actividad.copy(id = idLocal))
        } catch (e: Exception) {
            // Log error o manejar según política de sincronización
        }
        return idLocal
    }

    suspend fun actualizar(actividad: ActividadFormativa) {
        actividadDao.actualizar(actividad.toEntity())
        try {
            remoteDataSource.actualizarActividad(actividad)
        } catch (e: Exception) {
            // Log error
        }
    }

    suspend fun eliminar(actividad: ActividadFormativa) {
        actividadDao.eliminar(actividad.toEntity())
        try {
            remoteDataSource.eliminarActividad(actividad.id)
        } catch (e: Exception) {
            // Log error
        }
    }
}
