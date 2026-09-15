package com.example.miformacionctma.ui.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.data.repository.ActividadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.miformacionctma.domain.ReglasActividad
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ActividadViewModel(
    private val repository: ActividadRepository
) : ViewModel() {

    val textoBusqueda = MutableStateFlow("")
    val soloUrgentes = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val actividades: StateFlow<List<ActividadFormativa>> =
        textoBusqueda.flatMapLatest { query ->
            if (query.isBlank()) {
                repository.obtenerTodas()
            } else {
                repository.buscarPorTexto(query)
            }
        }.combine(soloUrgentes) { lista, filtrarUrgentes ->
            if (filtrarUrgentes) {
                ReglasActividad.actividadesUrgentes(lista)
            } else {
                lista
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun actualizarBusqueda(query: String) {
        textoBusqueda.value = query
    }

    fun cambiarFiltroUrgentes(urgentes: Boolean) {
        soloUrgentes.value = urgentes
    }

    fun insertar(actividad: ActividadFormativa) {
        viewModelScope.launch {
            repository.insertar(actividad)
        }
    }

    fun actualizar(actividad: ActividadFormativa) {
        viewModelScope.launch {
            repository.actualizar(actividad)
        }
    }

    fun eliminar(actividad: ActividadFormativa) {
        viewModelScope.launch {
            repository.eliminar(actividad)
        }
    }
}