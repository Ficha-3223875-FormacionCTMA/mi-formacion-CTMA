package com.example.miformacionctma.ui.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    private val preferencias: PreferenciasRepository
) : ViewModel() {

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda = _textoBusqueda.asStateFlow()

    private val _soloUrgentes = MutableStateFlow(false)
    val soloUrgentes = _soloUrgentes.asStateFlow()

    private val _operacionState = MutableStateFlow<OperacionUiState>(OperacionUiState.Inactiva)
    val operacionState = _operacionState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ListadoUiState> =
        combine(_textoBusqueda, _soloUrgentes) { query, urgentes ->
            query to urgentes
        }.flatMapLatest { (query, urgentes) ->
            if (query.isBlank()) {
                repository.obtenerTodas()
            } else {
                repository.buscarPorTexto(query)
            }.map { lista ->
                if (urgentes) {
                    ReglasActividad.actividadesUrgentes(lista)
                } else {
                    lista
                }
            }
        }.map { lista ->
            if (lista.isEmpty()) ListadoUiState.Vacio else ListadoUiState.Contenido(lista)
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
}