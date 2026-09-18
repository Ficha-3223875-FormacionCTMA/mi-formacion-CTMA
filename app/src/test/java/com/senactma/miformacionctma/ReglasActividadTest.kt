package com.senactma.miformacionctma

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ReglasActividadTest {

    // --- 1. Tabla de decisión parametrizada para estadoActividad ---

    @ParameterizedTest(name = "{index} => progreso={0}, diasRestantes={1}, esperado={2}")
    @CsvSource(
        "100, 5, Completada",   // CP-CTMA-13
        "100, -2, Completada",
        "50, 3, En proceso",
        "0, 4, Pendiente",
        "20, -1, Vencida",     // CP-CTMA-14
        "0, -5, Vencida"
    )
    @DisplayName("Pruebas de estadoActividad con tabla de decisión")
    fun testEstadoActividad(progreso: Int, diasRestantes: Int, esperado: String) {
        // Arrange & Act
        val resultado = ReglasActividad.estadoActividad(progreso, diasRestantes)
        
        // Assert
        assertEquals(esperado, resultado)
    }

    // --- 2. Casos Límite para validarActividad (CA-03.1) ---

    @Test
    @DisplayName("validarActividad: título en límite inferior (3 caracteres) devuelve verdadero")
    fun validarActividad_tituloEnLimiteInferior3Caracteres_devuelveVerdadero() {
        // Arrange
        val titulo = "ABC"
        
        // Act
        val resultado = ReglasActividad.validarActividad(titulo)
        
        // Assert
        assertTrue(resultado)
    }

    @Test
    @DisplayName("validarActividad: título en límite superior (80 caracteres) devuelve verdadero")
    fun validarActividad_tituloEnLimiteSuperior80Caracteres_devuelveVerdadero() {
        // Arrange
        val titulo = "A".repeat(80)
        
        // Act
        val resultado = ReglasActividad.validarActividad(titulo)
        
        // Assert
        assertTrue(resultado)
    }

    @Test
    @DisplayName("validarActividad: título excede 80 caracteres (81) devuelve falso")
    fun validarActividad_tituloExcede81Caracteres_devuelveFalso() {
        // Arrange
        val titulo = "A".repeat(81)
        
        // Act
        val resultado = ReglasActividad.validarActividad(titulo)
        
        // Assert
        assertFalse(resultado)
    }

    // --- 3. Pruebas complementarias ---

    @Test
    @DisplayName("actividadesUrgentes: devuelve verdadero si queda 2 días o menos y no está completada")
    fun actividadesUrgentes_diasRestantesDosYNoCompletada_devuelveVerdadero() {
        // Arrange
        val dias = 2
        val progreso = 50
        
        // Act
        val resultado = ReglasActividad.actividadesUrgentes(dias, progreso)
        
        // Assert
        assertTrue(resultado)
    }

    @Test
    @DisplayName("actividadesUrgentes: devuelve falso si está completada aunque queden pocos días")
    fun actividadesUrgentes_actividadCompletada_devuelveFalso() {
        // Arrange
        val dias = 1
        val progreso = 100
        
        // Act
        val resultado = ReglasActividad.actividadesUrgentes(dias, progreso)
        
        // Assert
        assertFalse(resultado)
    }

    @Test
    @DisplayName("promedioProgreso: calcula el promedio correctamente")
    fun promedioProgreso_listaValores_calculaPromedio() {
        // Arrange
        val lista = listOf(10, 20, 30, 40)
        
        // Act
        val resultado = ReglasActividad.promedioProgreso(lista)
        
        // Assert
        assertEquals(25.0, resultado)
    }

    @Test
    @DisplayName("promedioProgreso: devuelve 0.0 si la lista está vacía")
    fun promedioProgreso_listaVacia_devuelveCero() {
        // Arrange
        val lista = emptyList<Int>()
        
        // Act
        val resultado = ReglasActividad.promedioProgreso(lista)
        
        // Assert
        assertEquals(0.0, resultado)
    }

    // --- 4. Prueba TDD para BUG-CTMA-01 ---

    @Test
    @DisplayName("validarProgreso: no permite reducir progreso si ya está al 100% (BUG-CTMA-01)")
    fun validarProgreso_reducirProgresoEnActividadCompletada_devuelveFalso() {
        // Arrange
        val progresoAnterior = 100
        val progresoNuevo = 90
        
        // Act
        val resultado = ReglasActividad.validarProgreso(progresoAnterior, progresoNuevo)
        
        // Assert
        assertFalse(resultado, "El sistema no debe permitir reducir el progreso si ya está al 100%")
    }
}
