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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.miformacionctma.ui.actividad.ActividadViewModel

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
 *
 * Recibe el ViewModel y utiliza su Flow de actividades
 * para mantener la interfaz actualizada automáticamente.
 */
@Composable
fun GrafoNavegacion(
    viewModel: ActividadViewModel,
    navController: NavHostController = rememberNavController()
) {
    val actividades by viewModel.actividades.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Lista.ruta
    ) {

        // ---------------------------------------------------------
        // LISTA
        // ---------------------------------------------------------
        composable(Pantalla.Lista.ruta) {
            ListaRoute(
                actividades = actividades,
                viewModel = viewModel,
                onActividadClick = { id ->
                    navController.navigate(
                        Pantalla.Detalle.crearRuta(id)
                    )
                },
                onCrearClick = {
                    navController.navigate(Pantalla.Crear.ruta)
                }
            )
        }

        // ---------------------------------------------------------
        // CREAR
        // ---------------------------------------------------------
        composable(Pantalla.Crear.ruta) {
            CrearRoute(
                viewModel = viewModel,
                onGuardar = {
                    navController.popBackStack()
                },
                onCancelar = {
                    navController.popBackStack()
                }
            )
        }
        // ---------------------------------------------------------
        // DETALLE
        // ---------------------------------------------------------
        composable(
            route = Pantalla.Detalle.ruta,
            arguments = listOf(
                navArgument(ARG_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id =
                backStackEntry.arguments?.getLong(ARG_ID)
                    ?: -1L

            DetalleRoute(
                actividad = actividades.find {
                    it.id == id
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Destino: Lista.
 *
 * Muestra todas las actividades y permite:
 * - entrar al detalle
 * - editar
 * - eliminar
 * - crear una nueva actividad
 */
@Composable
fun ListaRoute(
    actividades: List<ActividadFormativa>,
    viewModel: ActividadViewModel,
    onActividadClick: (Long) -> Unit,
    onCrearClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Actividad que actualmente se está editando.
    // Si es null, no se muestra el diálogo.
    var actividadEditando by remember {
        mutableStateOf<ActividadFormativa?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis actividades",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onCrearClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva actividad")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = actividades,
                key = { it.id }
            ) { actividad ->

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // Tarjeta que muestra la información de la actividad.
                    TarjetaActividad(
                        actividad = actividad,
                        onClick = {
                            onActividadClick(actividad.id)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // Botones de Editar y Eliminar.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                actividadEditando = actividad
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Editar")
                        }

                        Button(
                            onClick = {
                                viewModel.eliminar(actividad)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------
    // DIÁLOGO DE EDICIÓN
    // -------------------------------------------------------------

    actividadEditando?.let { actividad ->

        var titulo by remember(actividad.id) {
            mutableStateOf(actividad.titulo)
        }

        var descripcion by remember(actividad.id) {
            mutableStateOf(
                actividad.descripcion.orEmpty()
            )
        }

        var progresoTexto by remember(actividad.id) {
            mutableStateOf(
                actividad.progreso.toString()
            )
        }

        var diasTexto by remember(actividad.id) {
            mutableStateOf(
                actividad.diasRestantes.toString()
            )
        }

        AlertDialog(
            onDismissRequest = {
                actividadEditando = null
            },

            title = {
                Text("Editar actividad")
            },

            text = {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedTextField(
                        value = titulo,
                        onValueChange = {
                            titulo = it
                        },
                        label = {
                            Text("Título")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                        },
                        label = {
                            Text("Descripción")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = progresoTexto,
                        onValueChange = {
                            progresoTexto = it
                        },
                        label = {
                            Text("Progreso")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = diasTexto,
                        onValueChange = {
                            diasTexto = it
                        },
                        label = {
                            Text("Días restantes")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        val progreso =
                            progresoTexto.toIntOrNull()
                                ?: actividad.progreso

                        val diasRestantes =
                            diasTexto.toIntOrNull()
                                ?: actividad.diasRestantes

                        viewModel.actualizar(
                            actividad.copy(
                                titulo = titulo,
                                descripcion =
                                    descripcion.ifBlank {
                                        null
                                    },
                                progreso = progreso,
                                diasRestantes =
                                    diasRestantes
                            )
                        )

                        // Cerrar el diálogo.
                        actividadEditando = null
                    }
                ) {
                    Text("Guardar")
                }
            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        actividadEditando = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Destino: Crear.
 *
 * Actualmente es un placeholder del formulario de creación.
 * La conexión real con viewModel.insertar() se realizará
 * en la siguiente etapa.
 */
@Composable
fun CrearRoute(
    viewModel: ActividadViewModel,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {

    var titulo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var progresoTexto by remember {
        mutableStateOf("0")
    }

    var diasTexto by remember {
        mutableStateOf("0")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Crear actividad",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
            },
            label = {
                Text("Descripción")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = progresoTexto,
            onValueChange = {
                progresoTexto = it
            },
            label = {
                Text("Progreso")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diasTexto,
            onValueChange = {
                diasTexto = it
            },
            label = {
                Text("Días restantes")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {

                    if (titulo.isBlank()) {
                        return@Button
                    }

                    val progreso =
                        progresoTexto.toIntOrNull() ?: 0

                    val diasRestantes =
                        diasTexto.toIntOrNull() ?: 0

                    val nuevaActividad =
                        ActividadFormativa(
                            titulo = titulo.trim(),
                            descripcion =
                                descripcion
                                    .trim()
                                    .ifBlank { null },
                            progreso = progreso,
                            diasRestantes = diasRestantes
                        )

                    viewModel.insertar(nuevaActividad)

                    onGuardar()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }

            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }
    }
}

/**
 * Estado utilizado por la pantalla de detalle.
 */
sealed class EstadoDetalle {

    data class Encontrada(
        val actividad: ActividadFormativa
    ) : EstadoDetalle()

    object NoEncontrada : EstadoDetalle()
}

/**
 * Destino: Detalle.
 *
 * Recibe una actividad resuelta mediante su id.
 * Si no existe, muestra un estado de recuperación.
 */
@Composable
fun DetalleRoute(
    actividad: ActividadFormativa?,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {

    val estado =
        if (actividad != null) {
            EstadoDetalle.Encontrada(actividad)
        } else {
            EstadoDetalle.NoEncontrada
        }

    when (estado) {

        is EstadoDetalle.Encontrada -> {
            DetalleContenido(
                actividad = estado.actividad,
                onVolver = onVolver,
                modifier = modifier
            )
        }

        EstadoDetalle.NoEncontrada -> {
            DetalleNoEncontrada(
                onVolver = onVolver,
                modifier = modifier
            )
        }
    }
}

/**
 * Contenido de la pantalla de detalle.
 */
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

        Text(
            text = actividad.titulo,
            style = MaterialTheme.typography.titleLarge
        )

        actividad.descripcion?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "Progreso: ${actividad.progreso}%"
        )

        Button(
            onClick = onVolver
        ) {
            Text("Volver")
        }
    }
}

/**
 * Estado de recuperación accesible.
 *
 * Usa liveRegion para que TalkBack anuncie el mensaje
 * automáticamente al aparecer.
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

                contentDescription =
                    "Actividad no encontrada. " +
                            "Es posible que el enlace sea " +
                            "incorrecto o que la actividad " +
                            "ya no exista."
            },

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Actividad no encontrada",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Es posible que el enlace sea incorrecto " +
                        "o que la actividad ya no exista.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onVolver
        ) {
            Text("Volver a la lista")
        }
    }
}