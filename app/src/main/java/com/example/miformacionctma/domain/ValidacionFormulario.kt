package com.example.miformacionctma.domain

fun validarTitulo(valor: String, mostrarVacio: Boolean): String? {
    val limpio = valor.trim()
    return when {
        limpio.isEmpty() && mostrarVacio -> "Escribe un título"
        limpio.isNotEmpty() && limpio.length < 3 -> "Usa al menos 3 caracteres" // Revertido a 3 caracteres
        limpio.length > 80 -> "Usa máximo 80 caracteres"
        else -> null
    }
}