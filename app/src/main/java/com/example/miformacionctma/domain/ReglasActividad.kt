package com.example.miformacionctma.domain

/* Creamos objeto Reglas Actividad para funciones de estado y logica del proyecto */
object ReglasActividad {

    // Primero se evalua si la actividad es valida y devuelve los errores en caso que no
    // Primero Se recibe un objeto de tipo ActividadFormativa
    fun validarActividad(actividad: ActividadFormativa): List<String> {
        // Variable para almacenar los errores (De tipo lista mutable)
        val errores = mutableListOf<String>()

        // si el titulo de la actividad esta vacio
        if (actividad.titulo.isBlank()) {
            errores.add("El título es obligatorio.")
        }

        // Si los dias restantes son menores que 0 (Se evalúa primero para responder a CP-CTMA-12)
        if (actividad.diasRestantes < 0) {
            errores.add("Los días restantes no pueden ser negativos.")
        }

        // Si el progreso es diferente de 0 a 100 (CP-CTMA-07 y CP-CTMA-11)
        if (actividad.progreso !in 0..100) {
            errores.add("El progreso debe estar entre 0 y 100.")
        }

        // Devuelve una lista con todos los errores
        return errores
    }

    // Usa el when para definir el estado de la actividad según la tabla de la HU-CTMA-02
    fun estadoActividad(actividad: ActividadFormativa): String = when {
        actividad.progreso == 100 -> "Completada"  // CP-CTMA-10
        actividad.progreso == 0 -> "Pendiente"     // CP-CTMA-08
        actividad.progreso in 1..99 -> "En proceso" // CP-CTMA-09
        actividad.diasRestantes < 0 -> "Vencida"
        else -> "Indefinido"
    }

    // Funcion para definir la urgencia de las actividades devolviendolas dentro de la misma variable
    fun actividadesUrgentes(
        actividades: List<ActividadFormativa>
    ): List<ActividadFormativa> {
        return actividades.filter {
            it.progreso < 100 && it.diasRestantes <= 3
        }
    }

    // Funcion para calcular el promedio del progreso total de todas las actividades
    fun promedioProgreso(
        actividades: List<ActividadFormativa>
    ): Double {
        if (actividades.isEmpty()) return 0.0
        return actividades
            .map { it.progreso }
            .average()
    }

    // funcion para buscar por titulo
    fun buscarPorTitulo(
        actividades: List<ActividadFormativa>,
        texto: String
    ): List<ActividadFormativa> {
        val busqueda = texto.trim()
        return actividades.filter {
            it.titulo.contains(busqueda, ignoreCase = true)
        }
    }
}