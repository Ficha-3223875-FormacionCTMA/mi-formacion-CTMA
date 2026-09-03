package com.example.miformacionctma.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ValidacionFormularioTest {

    @Test
    fun `CP-VAL-01 titulo vacio muestra error`() {
        assertEquals("Escribe un título", validarTitulo("", mostrarVacio = true))
    }

    @Test
    fun `CP-VAL-02 titulo de 3 caracteres o menos es muy corto`() {
        val resultado = validarTitulo("AB", mostrarVacio = true)
        assertEquals("Usa al menos 3 caracteres", resultado)
    }
    @Test
    fun `CP-VAL-03 titulo de 3 caracteres es valido (limite minimo)`() {
        assertEquals(null, validarTitulo("Abc", mostrarVacio = true))
    }

    @Test
    fun `CP-VAL-04 titulo de 80 caracteres es valido (limite maximo)`() {
        val titulo80 = "A".repeat(80)
        assertEquals(null, validarTitulo(titulo80, mostrarVacio = true))
    }

    @Test
    fun `CP-VAL-05 titulo de 81 caracteres es muy largo`() {
        val titulo81 = "A".repeat(81)
        assertEquals("Usa máximo 80 caracteres", validarTitulo(titulo81, mostrarVacio = true))
    }
}