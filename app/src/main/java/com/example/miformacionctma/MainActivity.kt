package com.example.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import com.example.miformacionctma.domain.ReglasActividad
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val actividades = listOf(
            ActividadFormativa(
                id = 1,
                titulo = "",
                descripcion = "Aprender variables y funciones",
                progreso = 100,
                diasRestantes = -2,
                prioridad = Prioridad.ALTA
            ),
            ActividadFormativa(
                id = 2,
                titulo = "Android Studio",
                descripcion = "Instalar Android Studio",
                progreso = 60,
                diasRestantes = 1,
                prioridad = Prioridad.MEDIA
            ),
            ActividadFormativa(
                id = 3,
                titulo = "Jetpack Compose",
                descripcion = "Crear la primera pantalla",
                progreso = 0,
                diasRestantes = 5,
                prioridad = Prioridad.BAJA
            )
        )
        val promedio = ReglasActividad.promedioProgreso(actividades)
        val urgentes = ReglasActividad.actividadesUrgentes(actividades)

        val resumen = """
        Total de actividades: ${actividades.size}
        Promedio de progreso: ${"%.1f".format(promedio)}%
        Actividades urgentes: ${urgentes.size}
        """.trimIndent()

        setContent {
            MiFormacionCTMATheme {
                PantallaInicio(resumen = resumen)
            }
        }
    }
}

@Composable
fun PantallaInicio(
    nombre: String = "Aprendiz",
    resumen: String
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Mi Formación CTMA",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Hola, $nombre")
        Text(text = "Aquí organizarás actividades y evidencias.")

        Spacer(modifier = Modifier.height(16.dp))

        // Resumen de las actividades
        Text(
            text = resumen,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección 1: Valores del Manifiesto Ágil
        Text(
            text = """
            Valores del Manifiesto Ágil

            • Individuos e interacciones:
            La comunicación del equipo es más importante que las herramientas.

            • Software funcionando:
            Es mejor una aplicación que funcione que mucha documentación.

            • Colaboración con el cliente:
            Trabajar junto al cliente permite obtener mejores resultados.

            • Respuesta ante el cambio:
            Adaptarse a los cambios ayuda a mejorar el proyecto.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección 2: Fundamentos de Scrum
        Text(
            text = """
            ¿Qué es Scrum?
            Es un marco de trabajo ágil para desarrollar proyectos en ciclos cortos y repetitivos (de 2 a 4 semanas) llamados Sprints.

            Roles
            • Product Owner: Prioriza lo que se debe hacer (representa al cliente).
            • Scrum Master: Facilita el proceso y elimina bloqueos.
            • Developers: Diseñan, programan y prueban el producto.

            Artefactos
            • Product Backlog: Lista general de todo lo que requiere el proyecto.
            • Sprint Backlog: Tareas seleccionadas para trabajar en el Sprint actual.
            • Incremento: La versión funcional y terminada del producto al final del Sprint.

            Ceremonias
            • Sprint Planning: Definir qué se hará en el Sprint.
            • Daily Scrum: Reunión diaria de 15 min para sincronizar avances.
            • Sprint Review: Mostrar el producto terminado a los interesados.
            • Sprint Retrospective: Evaluar cómo trabajó el equipo para mejorar en el siguiente ciclo.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección 3: Pruebas de Software
        Text(
            text = """
            Pruebas de Software
            Proceso de verificar que la app funcione bien, no tenga fallas y cumpla con lo esperado.

            Principales Tipos de Pruebas:
            • Unitarias: Prueban pequeñas partes de código de forma aislada (ej. funciones o cálculos).
            • De Integración: Verifican que varios componentes funcionen correctamente juntos.
            • De Interfaz / UI: Comprueban que los elementos visuales y pantallas se muestren e interactúen bien.
            • Funcionales: Validan que el sistema completo cumpla con los requisitos del usuario.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    MiFormacionCTMATheme {
        PantallaInicio(
            resumen = """
            Total de actividades: 3
            Promedio de progreso: 53.3%
            Actividades urgentes: 1
            """.trimIndent()
        )
    }
}