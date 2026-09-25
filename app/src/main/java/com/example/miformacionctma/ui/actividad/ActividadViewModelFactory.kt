package com.example.miformacionctma.ui.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.EvidenciaRepository
import com.example.miformacionctma.data.repository.PreferenciasRepository

class ActividadViewModelFactory(
    private val repository: ActividadRepository,
    private val evidenciaRepository: EvidenciaRepository,
    private val preferencias: PreferenciasRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ActividadViewModel::class.java)) {
            return ActividadViewModel(repository, evidenciaRepository, preferencias) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}