package com.example.miformacionctma.domain

/* Creamos objeto Reglas Actividad para funciones de estado y logica del proyecto */
object ReglasActividad{

//Primero se evalua si la actividad es valida y devuelve los errores en caso que no
//  Primero Se recibe un objeto de tipo ActividadFormativa
    fun validarActividad(actividad: ActividadFormativa): List<String> {
        //Variable para almacenar los errores (De tipo lista mutable)
        val errores = mutableListOf<String>()
        // si el titulo de la actividad esta vacio
        if (actividad.titulo.isBlank()) {
            errores.add("El título es obligatorio.")
        }
        //Si el progreso es diferente de 0 a 100
        if (actividad.progreso !in 0..100) {
            errores.add("El progreso debe estar entre 0 y 100.")
        }
        //Si los dias restantes son menores que 0
        if (actividad.diasRestantes < 0) {
            errores.add("Los días restantes no pueden ser negativos.")
        }
        //Devuelve una lista con todos los errores
        return errores
    }
//  Usa el when como si fuera un switch para definir el estado de la actividad en base a variables
    fun estadoActividad(actividad: ActividadFormativa): String = when {//El When actua como un switch eligiendo el caso
        actividad.progreso == 100 -> "Completada"//Si se alcanzo el 100% esta completada
        actividad.progreso > 0 -> "En proceso"//Si es mayor a 0 exceptuando el 100 esta en proceso
        actividad.diasRestantes < 0 -> "Vencida"//Si los dias llegaron a 0 se vencio
        else -> "Pendiente"//Si no esta pendiente
    }
    //Funcion para definir la urgencia de las actividades devolviendolas dentro de la misma variable
    fun actividadesUrgentes(
        actividades: List<ActividadFormativa>): List<ActividadFormativa> {//con ":" le indicamos que la almacene al ejecutar la funcion dentro del mismo objeto
        return actividades.filter {//Le decimos que retorne las actividades filtradas
            it.progreso < 100 &&//con el "it" le decimos que revise el elemento actual en este caso (que revise uno por uno el progreso < 100
                    it.diasRestantes <= 3 // Ademas tambien que revise el elemento actual de diasRestantes cuando sean menores o iguales a 2
        }
    }
    //Funcion para calcular el promedio del progreso total de todas las actividades
    fun promedioProgreso(
        actividades: List<ActividadFormativa>
    ): Double { // le indicamos que devuelva un valor de tipo Double

        //Si las actividadades estan vacias que devuelvan un promedio de 0.0
        if (actividades.isEmpty()) return 0.0
        // de lo contrario que haga un mapeo (Convierte todos los datos contenidos en progreso(20,50,40,..) y les saca el promedio con average
        return actividades
            .map { it.progreso }
            .average()
    }

    //funcion para buscar por titulo
    fun buscarPorTitulo(
        actividades: List<ActividadFormativa>,
        texto: String
    ): List<ActividadFormativa> {
        val busqueda = texto.trim() //Variable estatica para almacenar el texto(Lo almacena sin espacios en blanco por trim() )
        return actividades.filter {//le decimos que devuelva las actividades filtrandola por cada objeto que contenga la variable con el titulo
            it.titulo.contains(busqueda, ignoreCase = true)//contains nos permite hacer busqueda de elementos parciales
            //ejemplo si el titulo es "Proyecto de grado" pero solo se busca "Proyecto" devolvera todos los elementos incluido este ignorando mayusculas
        }
    }
}