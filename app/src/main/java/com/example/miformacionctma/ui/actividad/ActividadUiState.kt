package com.example.miformacionctma.ui.actividad

import com.example.miformacionctma.domain.ActividadFormativa

/**
 * Representa los diferentes estados de la pantalla de listado de actividades.
 */
sealed interface ListadoUiState {
    object Cargando : ListadoUiState
    
    data class Contenido(
        val actividades: List<ActividadFormativa>
    ) : ListadoUiState
    
    object Vacio : ListadoUiState
    
    data class Error(
        val mensaje: String
    ) : ListadoUiState
}

/**
 * Representa el estado de una operación de escritura (Crear, Editar, Eliminar).
 */
sealed interface OperacionUiState {
    object Inactiva : OperacionUiState
    object EnCurso : OperacionUiState
    object Exitosa : OperacionUiState
    
    data class Fallida(
        val mensaje: String
    ) : OperacionUiState
}
