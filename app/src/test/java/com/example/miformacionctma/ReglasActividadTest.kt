package com.example.miformacionctma
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import com.example.miformacionctma.domain.ReglasActividad
import kotlin.test.assertTrue
import org.junit.Assert.*
import org.junit.Test

class ReglasActividadTest {

    @Test
    fun probarValidacion() {

        val actividad =

            ActividadFormativa(
                id = 1,
                titulo = "",
                descripcion = null,
                progreso = 120,
                diasRestantes = -1,
                prioridad = Prioridad.ALTA
            )

        val errores = ReglasActividad.validarActividad(actividad)

        println(errores)

        assertTrue(errores.isNotEmpty())
    }
}