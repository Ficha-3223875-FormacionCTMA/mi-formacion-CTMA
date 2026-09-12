package com.example.miformacionctma.ui.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.data.repository.ActividadRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActividadViewModel(
    private val repository: ActividadRepository
) : ViewModel() {

    val actividades: StateFlow<List<ActividadFormativa>> =
        repository.obtenerTodas()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

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