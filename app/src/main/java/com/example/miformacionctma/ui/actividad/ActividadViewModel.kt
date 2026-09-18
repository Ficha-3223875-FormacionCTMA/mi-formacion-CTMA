package com.example.miformacionctma.ui.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.EvidenciaRepository
import com.example.miformacionctma.data.repository.PreferenciasRepository
import com.example.miformacionctma.domain.Evidencia
import com.example.miformacionctma.domain.EstadoEvidencia
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.miformacionctma.domain.ReglasActividad
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ActividadViewModel(
    private val repository: ActividadRepository,
    private val evidenciaRepository: EvidenciaRepository,
    private val preferencias: PreferenciasRepository
) : ViewModel() {

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda = _textoBusqueda.asStateFlow()

    private val _soloUrgentes = MutableStateFlow(false)
    val soloUrgentes = _soloUrgentes.asStateFlow()

    private val _operacionState = MutableStateFlow<OperacionUiState>(OperacionUiState.Inactiva)
    val operacionState = _operacionState.asStateFlow()

    private val _errorSincronizacion = MutableStateFlow<String?>(null)
    val errorSincronizacion = _errorSincronizacion.asStateFlow()

    private val _estaSincronizando = MutableStateFlow(false)
    val estaSincronizando = _estaSincronizando.asStateFlow()

    private val _idActividadSeleccionada = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val evidencias: StateFlow<List<Evidencia>> =
        _idActividadSeleccionada.flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else evidenciaRepository.obtenerPorActividad(id)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private var refreshJob: Job? = null

    init {
        refrescar()
    }

    /**
     * Intenta sincronizar los datos con el servidor.
     * Implementa manejo de concurrencia y cancelación (CA-08).
     */
    fun refrescar() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _estaSincronizando.value = true
            _errorSincronizacion.value = null
            try {
                repository.sincronizar()
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorSincronizacion.value = e.message ?: "Error de red"
                
                // Manejo específico de 401: Cerrar sesión (Requerimiento Laverde)
                if (e.message?.contains("401") == true || e.message?.contains("Sesión expirada") == true) {
                    com.example.miformacionctma.data.remote.auth.TokenProvider.clearToken()
                }
            } finally {
                _estaSincronizando.value = false
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ListadoUiState> =
        combine(_textoBusqueda, _soloUrgentes, _errorSincronizacion) { query, urgentes, error ->
            Triple(query, urgentes, error)
        }.flatMapLatest { (query, urgentes, error) ->
            if (query.isBlank()) {
                repository.obtenerTodas()
            } else {
                repository.buscarPorTexto(query)
            }.map { lista ->
                val actividades = if (urgentes) {
                    ReglasActividad.actividadesUrgentes(lista)
                } else {
                    lista
                }

                when {
                    actividades.isNotEmpty() -> ListadoUiState.Contenido(actividades)
                    error != null -> ListadoUiState.Error(error) // Fallo red + No caché
                    else -> ListadoUiState.Vacio // Sin datos pero sin error de red (o cargando)
                }
            }
        }.onStart {
            emit(ListadoUiState.Cargando)
        }.catch { e ->
            emit(ListadoUiState.Error(e.message ?: "Error desconocido"))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListadoUiState.Cargando
        )

    fun actualizarBusqueda(query: String) {
        _textoBusqueda.value = query
    }

    fun cambiarFiltroUrgentes(urgentes: Boolean) {
        _soloUrgentes.value = urgentes
    }

    fun insertar(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                repository.insertar(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                _operacionState.value = OperacionUiState.Fallida(e.message ?: "Error al insertar")
            }
        }
    }

    fun actualizar(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                // Validación de transición (Laboratorio 2)
                val antigua = repository.obtenerPorId(actividad.id).firstOrNull()
                if (antigua != null) {
                    val errorTransicion = ReglasActividad.validarTransicion(antigua, actividad)
                    if (errorTransicion != null) {
                        _operacionState.value = OperacionUiState.Fallida(errorTransicion)
                        return@launch
                    }
                }

                // Validación de campos obligatorios (Evidencia ausente)
                val errores = ReglasActividad.validarActividad(actividad)
                if (errores.isNotEmpty()) {
                    _operacionState.value = OperacionUiState.Fallida(errores.first())
                    return@launch
                }

                repository.actualizar(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                _operacionState.value = OperacionUiState.Fallida(e.message ?: "Error al actualizar")
            }
        }
    }

    fun eliminar(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                repository.eliminar(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                _operacionState.value = OperacionUiState.Fallida(e.message ?: "Error al eliminar")
            }
        }
    }
    
    fun resetearEstadoOperacion() {
        _operacionState.value = OperacionUiState.Inactiva
    }

    fun seleccionarActividad(id: Long?) {
        _idActividadSeleccionada.value = id
    }

    fun adjuntarEvidencia(uri: String, tipoMime: String, tamanio: Long) {
        val actividadId = _idActividadSeleccionada.value ?: return
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                val evidencia = Evidencia(
                    actividadId = actividadId,
                    uri = uri,
                    tipoMime = tipoMime,
                    tamanio = tamanio
                )
                evidenciaRepository.adjuntar(evidencia)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                _operacionState.value = OperacionUiState.Fallida(e.message ?: "Error al adjuntar evidencia")
            }
        }
    }

    fun eliminarEvidencia(evidencia: Evidencia) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                evidenciaRepository.eliminar(evidencia)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                _operacionState.value = OperacionUiState.Fallida(e.message ?: "Error al eliminar evidencia")
            }
        }
    }
}
