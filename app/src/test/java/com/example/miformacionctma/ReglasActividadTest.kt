package com.example.miformacionctma.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasActividadTest {

    // Función auxiliar para construir objetos válidos usando tu data class
    private fun crearActividad(progreso: Int, diasRestantes: Int): ActividadFormativa {
        return ActividadFormativa(
            id = 1L,
            titulo = "Guía 1",
            descripcion = "Descripción de prueba",
            progreso = progreso,
            diasRestantes = diasRestantes,
            prioridad = Prioridad.MEDIA
        )
    }

    // CP-CTMA-07 [Negativa]: progreso = -1
    @Test
    fun cpCtma07_progresoMenorAZero_retornaError() {
        val actividad = crearActividad(progreso = -1, diasRestantes = 5)
        val errores = ReglasActividad.validarActividad(actividad)

        assertTrue(errores.contains("El progreso debe estar entre 0 y 100."))
    }

    // CP-CTMA-08 [Positiva]: progreso = 0 (límite mínimo)
    @Test
    fun cpCtma08_progresoCero_estadoPendiente() {
        val actividad = crearActividad(progreso = 0, diasRestantes = 5)
        val estado = ReglasActividad.estadoActividad(actividad)

        assertEquals("Pendiente", estado)
    }

    // CP-CTMA-09 [Positiva]: progreso = 50 (partición de equivalencia)
    @Test
    fun cpCtma09_progresoCincuenta_estadoEnProceso() {
        val actividad = crearActividad(progreso = 50, diasRestantes = 5)
        val estado = ReglasActividad.estadoActividad(actividad)

        assertEquals("En proceso", estado)
    }

    // CP-CTMA-10 [Positiva]: progreso = 100 (límite máximo)
    @Test
    fun cpCtma10_progresoCien_estadoCompletada() {
        val actividad = crearActividad(progreso = 100, diasRestantes = 5)
        val estado = ReglasActividad.estadoActividad(actividad)

        assertEquals("Completada", estado)
    }

    // CP-CTMA-11 [Negativa]: progreso = 101
    @Test
    fun cpCtma11_progresoMayorACien_retornaError() {
        val actividad = crearActividad(progreso = 101, diasRestantes = 5)
        val errores = ReglasActividad.validarActividad(actividad)

        assertTrue(errores.contains("El progreso debe estar entre 0 y 100."))
    }

    // CP-CTMA-12 [Negativa]: combinación (progreso = 100, diasRestantes = -3)
    @Test
    fun cpCtma12_diasRestantesNegativos_retornaErrorDias() {
        val actividad = crearActividad(progreso = 100, diasRestantes = -3)
        val errores = ReglasActividad.validarActividad(actividad)

        assertEquals("Los días restantes no pueden ser negativos.", errores.first())
    }
}