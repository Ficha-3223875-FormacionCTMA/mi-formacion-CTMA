package com.senactma.miformacionctma

object ReglasActividad {

    /**
     * Define el estado de la actividad según el progreso y los días restantes.
     */
    fun estadoActividad(progreso: Int, diasRestantes: Int): String = when {
        progreso == 100 -> "Completada"
        progreso > 0 && diasRestantes >= 0 -> "En proceso"
        diasRestantes < 0 && progreso < 100 -> "Vencida"
        else -> "Pendiente"
    }

    /**
     * Valida que el título tenga una longitud permitida.
     */
    fun validarActividad(titulo: String): Boolean {
        return titulo.length in 3..80
    }

    /**
     * Determina si una actividad es urgente.
     */
    fun actividadesUrgentes(diasRestantes: Int, progreso: Int): Boolean {
        return diasRestantes <= 2 && progreso < 100
    }

    /**
     * Calcula el promedio de progreso de una lista de valores.
     */
    fun promedioProgreso(listaProgresos: List<Int>): Double {
        if (listaProgresos.isEmpty()) return 0.0
        return listaProgresos.average()
    }

    /**
     * Valida la actualización del progreso, aplicando reglas de negocio y corrección de bugs.
     * BUG-CTMA-01: No permitir reducir el progreso si ya está al 100%.
     */
    fun validarProgreso(progresoAnterior: Int, progresoNuevo: Int): Boolean {
        if (progresoNuevo !in 0..100) return false
        if (progresoAnterior == 100 && progresoNuevo < 100) return false
        return true
    }
}
