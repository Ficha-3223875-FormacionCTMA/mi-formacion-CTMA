package com.example.miformacionctma.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.miformacionctma.domain.ActividadFormativa

private const val ARG_ID = "id"

/**
 * Identificadores de las pantallas/rutas de la aplicación.
 */
sealed class Pantalla(val ruta: String) {
    object Inicio : Pantalla(ruta = "inicio")
    object Lista : Pantalla(ruta = "lista")
    object Crear : Pantalla(ruta = "crear")
    object Detalle : Pantalla(ruta = "detalle/{$ARG_ID}") {
        fun crearRuta(id: Long): String = "detalle/$id"
    }
}

/**
 * Grafo principal de navegación.
 * Recibe la lista de actividades (por ahora en memoria) y arma
 * los tres destinos: Lista, Crear y Detalle.
 */
@Composable
fun GrafoNavegacion(
    actividades: List<ActividadFormativa>,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.Lista.ruta
    ) {
        composable(Pantalla.Lista.ruta) {
            ListaRoute(
                actividades = actividades,
                onActividadClick = { id -> navController.navigate(Pantalla.Detalle.crearRuta(id)) },
                onCrearClick = { navController.navigate(Pantalla.Crear.ruta) }
            )
        }

        composable(Pantalla.Crear.ruta) {
            CrearRoute(
                onGuardar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(
            route = Pantalla.Detalle.ruta,
            arguments = listOf(navArgument(ARG_ID) { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(ARG_ID) ?: -1L
            DetalleRoute(
                actividad = actividades.find { it.id == id },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Destino: Lista. Reutiliza TarjetaActividad (Semana 3) y su
 * callback onClick para navegar al detalle usando el id.
 */
@Composable
fun ListaRoute(
    actividades: List<ActividadFormativa>,
    onActividadClick: (Long) -> Unit,
    onCrearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Mis actividades", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onCrearClick, modifier = Modifier.fillMaxWidth()) {
            Text("Nueva actividad")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items = actividades, key = { it.id }) { actividad ->
                TarjetaActividad(
                    actividad = actividad,
                    onClick = { onActividadClick(actividad.id) }
                )
            }
        }
    }
}

/**
 * Destino: Crear. Placeholder del formulario de creación
 * (se conectará con ValidacionFormulario en la próxima entrega).
 */
@Composable
fun CrearRoute(
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Crear actividad", style = MaterialTheme.typography.titleLarge)

        Text(
            text = "Formulario de creación de actividad.",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onGuardar) { Text("Guardar") }
            OutlinedButton(onClick = onCancelar) { Text("Cancelar") }
        }
    }
}

/**
 * Destino: Detalle. Recibe la actividad ya resuelta a partir del
 * argumento id. Si no existe, muestra un estado NoEncontrada
 * accesible en lugar de fallar.
 */
sealed class EstadoDetalle {
    data class Encontrada(val actividad: ActividadFormativa) : EstadoDetalle()
    object NoEncontrada : EstadoDetalle()
}

@Composable
fun DetalleRoute(
    actividad: ActividadFormativa?,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado = if (actividad != null) {
        EstadoDetalle.Encontrada(actividad)
    } else {
        EstadoDetalle.NoEncontrada
    }

    when (estado) {
        is EstadoDetalle.Encontrada -> DetalleContenido(
            actividad = estado.actividad,
            onVolver = onVolver,
            modifier = modifier
        )

        EstadoDetalle.NoEncontrada -> DetalleNoEncontrada(
            onVolver = onVolver,
            modifier = modifier
        )
    }
}

@Composable
private fun DetalleContenido(
    actividad: ActividadFormativa,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = actividad.titulo, style = MaterialTheme.typography.titleLarge)

        actividad.descripcion?.let {
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }

        Text(text = "Progreso: ${actividad.progreso}%")

        Button(onClick = onVolver) { Text("Volver") }
    }
}

/**
 * Estado de recuperación accesible: usa liveRegion para que
 * TalkBack anuncie el mensaje automáticamente al aparecer,
 * en vez de dejar la pantalla en blanco o crashear.
 */
@Composable
private fun DetalleNoEncontrada(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics(mergeDescendants = true) {
                liveRegion = LiveRegionMode.Polite
                contentDescription = "Actividad no encontrada. Es posible que el enlace sea " +
                        "incorrecto o que la actividad ya no exista."
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Actividad no encontrada",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Es posible que el enlace sea incorrecto o que la actividad ya no exista.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onVolver) {
            Text("Volver a la lista")
        }
    }
}