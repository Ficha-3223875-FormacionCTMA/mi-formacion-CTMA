package com.example.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import com.example.miformacionctma.domain.ReglasActividad
import com.example.miformacionctma.ui.TarjetaActividad
import com.example.miformacionctma.ui.GrafoNavegacion
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Lista construida con 10 elementos
        val actividades = listOf(
            ActividadFormativa(
                id = 1,
                titulo = "Fundamentos de Kotlin",
                descripcion = "Aprender variables y funciones",
                progreso = 100,
                diasRestantes = -2,
                prioridad = Prioridad.ALTA
            ),
            ActividadFormativa(
                id = 2,
                titulo = "Android Studio",
                descripcion = "Instalar Android Studio y entorno",
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
            ),
            ActividadFormativa(
                id = 4,
                titulo = "Diseño UI/UX",
                descripcion = "Crear prototipo de interfaz en Figma",
                progreso = 80,
                diasRestantes = 2,
                prioridad = Prioridad.ALTA
            ),
            ActividadFormativa(
                id = 5,
                titulo = "Base de Datos Room",
                descripcion = "Modelar las entidades principales de la app",
                progreso = 30,
                diasRestantes = 4,
                prioridad = Prioridad.MEDIA
            ),
            ActividadFormativa(
                id = 6,
                titulo = "Navegación Compose",
                descripcion = "Configurar NavHost y rutas de la aplicación",
                progreso = 0,
                diasRestantes = 7,
                prioridad = Prioridad.BAJA
            ),
            ActividadFormativa(
                id = 7,
                titulo = "Pruebas Unitarias",
                descripcion = "Implementar pruebas JUnit para las reglas de negocio",
                progreso = 10,
                diasRestantes = 3,
                prioridad = Prioridad.ALTA
            ),
            ActividadFormativa(
                id = 8,
                titulo = "Uso de ViewModel",
                descripcion = "Gestionar el estado de la interfaz de usuario",
                progreso = 0,
                diasRestantes = 8,
                prioridad = Prioridad.MEDIA
            ),
            ActividadFormativa(
                id = 9,
                titulo = "Consumo de API REST",
                descripcion = "Conectar Retrofit con el backend de servicios",
                progreso = 0,
                diasRestantes = 10,
                prioridad = Prioridad.BAJA
            ),
            ActividadFormativa(
                id = 10,
                titulo = "Entrega de Evidencias",
                descripcion = "Subir el proyecto final al repositorio institucional",
                progreso = 0,
                diasRestantes = 12,
                prioridad = Prioridad.ALTA
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
                GrafoNavegacion(actividades = actividades)
            }
        }
    }
}

@Composable
fun PantallaInicio(
    nombre: String = "Aprendiz",
    resumen: String,
    actividades: List<ActividadFormativa>
) {
    // Estado de demostración para la Parte 7 (recomposición)
    var actividadDemo by remember {
        mutableStateOf(
            ActividadFormativa(
                id = 99,
                titulo = "Demostración de recomposición",
                descripcion = "Cambia el progreso para observar qué se vuelve a dibujar.",
                progreso = 60,
                diasRestantes = 1,
                prioridad = Prioridad.MEDIA
            )
        )
    }

    // Detectamos el ancho de la pantalla para adaptar las columnas
    val configuration = LocalConfiguration.current
    val nColumnas = if (configuration.screenWidthDp >= 600) 2 else 1

    // LazyVerticalGrid es ahora el contenedor de scroll principal
    LazyVerticalGrid(
        columns = GridCells.Fixed(nColumnas),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // 1. Encabezado y Resumen (ocupan todo el ancho)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(32.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mi Formación CTMA",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Hola, $nombre")
                Text(text = "Aquí organizarás actividades y evidencias.")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = resumen, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Mis actividades", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // 2. Lista de Actividades o Estado Vacío
        if (actividades.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EstadoVacioActividades()
            }
        } else {
            items(
                items = actividades,
                key = { it.id }
            ) { actividad ->
                TarjetaActividad(actividad = actividad, onClick = {})
            }
        }

        // 3. Sección de Demostración (ocupa todo el ancho)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Demostración de recomposición",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                TarjetaActividad(actividad = actividadDemo, onClick = {})
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { actividadDemo = actividadDemo.copy(progreso = 100) }) {
                    Text("Cambiar progreso a 100")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Composable independiente para representar el estado vacío
@Composable
fun EstadoVacioActividades(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null, // Imagen decorativa
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay actividades registradas",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "En este momento no tienes tareas ni evidencias pendientes.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Preview compacta con lista de actividades
@Preview(showBackground = true, widthDp = 360)
@Composable
fun PantallaInicioPreview() {
    MiFormacionCTMATheme {
        PantallaInicio(
            resumen = """
    Total de actividades: 3
    Promedio de progreso: 53.3%
    Actividades urgentes: 1
    """.trimIndent(),
            actividades = listOf(
                ActividadFormativa(1, "Fundamentos de Kotlin", "Aprender variables y funciones", 100, -2, Prioridad.ALTA),
                ActividadFormativa(2, "Android Studio", "Instalar Android Studio", 60, 1, Prioridad.MEDIA),
                ActividadFormativa(3, "Jetpack Compose", "Crear la primera pantalla", 0, 5, Prioridad.BAJA)
            )
        )
    }
}

// Preview adaptativa en modo horizontal/tablet
@Preview(showBackground = true, widthDp = 700)
@Composable
fun PantallaInicioGridPreview() {
    MiFormacionCTMATheme {
        PantallaInicio(
            resumen = """
    Total de actividades: 3
    Promedio de progreso: 53.3%
    Actividades urgentes: 1
    """.trimIndent(),
            actividades = listOf(
                ActividadFormativa(1, "Fundamentos de Kotlin", "Aprender variables y funciones", 100, -2, Prioridad.ALTA),
                ActividadFormativa(2, "Android Studio", "Instalar Android Studio", 60, 1, Prioridad.MEDIA),
                ActividadFormativa(3, "Jetpack Compose", "Crear la primera pantalla", 0, 5, Prioridad.BAJA)
            )
        )
    }
}

// Preview específica para validar visualmente el Estado Vacío
@Preview(showBackground = true, widthDp = 360)
@Composable
fun PantallaInicioEstadoVacioPreview() {
    MiFormacionCTMATheme {
        PantallaInicio(
            resumen = """
    Total de actividades: 0
    Promedio de progreso: 0.0%
    Actividades urgentes: 0
    """.trimIndent(),
            actividades = emptyList()
        )
    }
}