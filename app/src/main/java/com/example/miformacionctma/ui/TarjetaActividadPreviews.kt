package com.example.miformacionctma.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

// Datos de muestra locales y deterministas (sin red, sin base de datos)
private val actividadPendiente = ActividadFormativa(
    id = 1L,
    titulo = "Diseñar wireframes de la pantalla principal",
    descripcion = "Bocetar las pantallas de aprendiz e instructor.",
    progreso = 0,
    diasRestantes = 5,
    prioridad = Prioridad.MEDIA
)

private val actividadEnProceso = ActividadFormativa(
    id = 2L,
    titulo = "Implementar TarjetaActividad",
    descripcion = "Componente reutilizable con Card y Modifier.",
    progreso = 60,
    diasRestantes = 2,
    prioridad = Prioridad.ALTA
)

private val actividadCompletada = ActividadFormativa(
    id = 3L,
    titulo = "Configurar tema Material 3",
    descripcion = "Definir colorScheme y tipografía institucional.",
    progreso = 100,
    diasRestantes = 0,
    prioridad = Prioridad.BAJA
)

private val actividadVencida = ActividadFormativa(
    id = 4L,
    titulo = "Entregar informe de avance de Semana 2",
    descripcion = "Subir evidencia al repositorio del proyecto.",
    progreso = 40,
    diasRestantes = -2,
    prioridad = Prioridad.ALTA
)

private val actividadTituloLargo = ActividadFormativa(
    id = 5L,
    titulo = "Analizar, documentar y validar todos los criterios de aceptación " +
            "de las historias de usuario antes de iniciar la implementación en Compose",
    descripcion = "Revisión cruzada con el instructor antes de codificar.",
    progreso = 30,
    diasRestantes = 4,
    prioridad = Prioridad.MEDIA
)

// 1. Estados diferentes, todos en una sola preview para comparar de un vistazo
@Preview(showBackground = true, name = "Estados diferentes")
@Composable
private fun TarjetaActividadEstadosPreview() {
    MiFormacionCTMATheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaActividad(actividad = actividadPendiente, onClick = {})
            TarjetaActividad(actividad = actividadEnProceso, onClick = {})
            TarjetaActividad(actividad = actividadCompletada, onClick = {})
            TarjetaActividad(actividad = actividadVencida, onClick = {})
        }
    }
}

// 2. Título inesperadamente largo
@Preview(showBackground = true, name = "Título largo")
@Composable
private fun TarjetaActividadTituloLargoPreview() {
    MiFormacionCTMATheme {
        TarjetaActividad(
            actividad = actividadTituloLargo,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

// 3. Fuente 1.5x — valida que no haya recortes de texto
@Preview(showBackground = true, name = "Fuente 1.5x", fontScale = 1.5f)
@Composable
private fun TarjetaActividadFuenteGrandePreview() {
    MiFormacionCTMATheme {
        TarjetaActividad(
            actividad = actividadEnProceso,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

// 4. Ancho ampliado — simula tablet u orientación horizontal
@Preview(showBackground = true, name = "Ancho ampliado", widthDp = 700)
@Composable
private fun TarjetaActividadAnchoAmpliadoPreview() {
    MiFormacionCTMATheme {
        TarjetaActividad(
            actividad = actividadEnProceso,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}