package com.example.miformacionctma.ui

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Evidencia
import com.example.miformacionctma.ui.actividad.ListadoUiState
import com.example.miformacionctma.ui.actividad.ActividadViewModel
import com.example.miformacionctma.ui.actividad.OperacionUiState
import com.example.miformacionctma.ui.actividad.ResumenHeader
import com.example.miformacionctma.ui.actividad.EvidenciaSection
import java.io.File

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
    object Editar : Pantalla(ruta = "editar/{$ARG_ID}") {
        fun crearRuta(id: Long): String = "editar/$id"
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Pantalla.Lista.ruta
    ) {

        // ---------------------------------------------------------
        // LISTA
        // ---------------------------------------------------------
        composable(Pantalla.Lista.ruta) {
            ListaRoute(
                uiState = uiState,
                viewModel = viewModel,
                onActividadClick = { id ->
                    navController.navigate(Pantalla.Detalle.crearRuta(id))
                },
                onEditarClick = { id ->
                    navController.navigate(Pantalla.Editar.crearRuta(id))
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
            val id = backStackEntry.arguments?.getLong(ARG_ID) ?: -1L
            
            val actividad = (uiState as? ListadoUiState.Contenido)?.actividades?.find { it.id == id }
            val evidencias by viewModel.evidencias.collectAsStateWithLifecycle()

            // Sincronizar el ID seleccionado en el ViewModel
            LaunchedEffect(id) {
                viewModel.seleccionarActividad(id)
            }
            
            DetalleRoute(
                actividad = actividad,
                evidencias = evidencias,
                onVolver = {
                    viewModel.seleccionarActividad(null)
                    navController.popBackStack()
                },
                onCapturarFoto = { uri, mime, size ->
                    viewModel.adjuntarEvidencia(uri, mime, size)
                },
                onSeleccionarGaleria = { uri, mime, size ->
                    viewModel.adjuntarEvidencia(uri, mime, size)
                },
                onEliminarEvidencia = { evidencia ->
                    viewModel.eliminarEvidencia(evidencia)
                }
            )
        }

        // ---------------------------------------------------------
        // EDITAR (Navegación nativa inmune a bugs de diálogos flotantes)
        // ---------------------------------------------------------
        composable(
            route = Pantalla.Editar.ruta,
            arguments = listOf(
                navArgument(ARG_ID) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong(ARG_ID) ?: -1L
            val actividad = (uiState as? ListadoUiState.Contenido)?.actividades?.find { it.id == id }
            
            EditarRoute(
                actividad = actividad,
                viewModel = viewModel,
                onGuardar = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Destino: Lista con soporte de búsqueda y filtros reactivos.
 */
@Composable
fun ListaRoute(
    uiState: ListadoUiState,
    viewModel: ActividadViewModel,
    onActividadClick: (Long) -> Unit,
    onEditarClick: (Long) -> Unit,
    onCrearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textoBusqueda by viewModel.textoBusqueda.collectAsStateWithLifecycle()
    val soloUrgentes by viewModel.soloUrgentes.collectAsStateWithLifecycle()
    val operacionState by viewModel.operacionState.collectAsStateWithLifecycle()
    val errorSincronizacion by viewModel.errorSincronizacion.collectAsStateWithLifecycle()
    val estaSincronizando by viewModel.estaSincronizando.collectAsStateWithLifecycle()
    
    val snackbarHostState = remember { SnackbarHostState() }

    // Feedback automático para operaciones de escritura
    LaunchedEffect(operacionState) {
        when (operacionState) {
            OperacionUiState.Exitosa -> {
                snackbarHostState.showSnackbar("Operación realizada con éxito")
                viewModel.resetearEstadoOperacion()
            }
            is OperacionUiState.Fallida -> {
                snackbarHostState.showSnackbar("Error: ${(operacionState as OperacionUiState.Fallida).mensaje}")
                viewModel.resetearEstadoOperacion()
            }
            else -> {}
        }
    }

    // Feedback sutil para errores de sincronización en segundo plano (CA-03)
    LaunchedEffect(errorSincronizacion) {
        if (errorSincronizacion != null && uiState is ListadoUiState.Contenido) {
            snackbarHostState.showSnackbar("Modo offline: No se pudo actualizar con el servidor.")
        }
    }

    val coloresCampos = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        cursorColor = Color.Black
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (estaSincronizando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = "Mis actividades",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de Búsqueda reactiva en tiempo real (DAO query)
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { viewModel.actualizarBusqueda(it) },
                label = { Text("Buscar por título o descripción...") },
                colors = coloresCampos,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Permiso de Notificaciones (Android 13+ - Guia #9)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                var permitirNotificaciones by remember { mutableStateOf(false) }
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    permitirNotificaciones = isGranted
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Activar recordatorios", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = permitirNotificaciones,
                        onCheckedChange = { checked ->
                            if (checked) {
                                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                permitirNotificaciones = false
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Filtro de Urgencia reactivo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Mostrar solo urgentes (<= 3 días)", style = MaterialTheme.typography.bodyMedium)
                Switch(
                    checked = soloUrgentes,
                    onCheckedChange = { viewModel.cambiarFiltroUrgentes(it) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCrearClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nueva actividad")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                ListadoUiState.Cargando -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ListadoUiState.Contenido -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // ITEM DE RESUMEN (REGLAS DE NEGOCIO)
                        item {
                            ResumenHeader(actividades = uiState.actividades)
                        }

                        items(
                            items = uiState.actividades,
                            key = { it.id }
                        ) { actividad ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                TarjetaActividad(
                                    actividad = actividad,
                                    onClick = { onActividadClick(actividad.id) },
                                    onEliminar = { viewModel.eliminar(actividad) }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = { onEditarClick(actividad.id) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Editar detalles")
                                }
                            }
                        }
                    }
                }
                ListadoUiState.Vacio -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay actividades que coincidan con los filtros.")
                    }
                }
                is ListadoUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        ) {
                            Text(
                                text = "Error: ${uiState.mensaje}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.refrescar() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FormularioActividad(
    actividadInicial: ActividadFormativa?,
    onGuardar: (ActividadFormativa) -> Unit,
    onCancelar: () -> Unit,
    tituloPantalla: String,
    modifier: Modifier = Modifier
) {
    var titulo by remember(actividadInicial?.id) { mutableStateOf(actividadInicial?.titulo ?: "") }
    var descripcion by remember(actividadInicial?.id) { mutableStateOf(actividadInicial?.descripcion ?: "") }
    var progresoTexto by remember(actividadInicial?.id) { mutableStateOf(actividadInicial?.progreso?.toString() ?: "0") }
    var diasTexto by remember(actividadInicial?.id) { mutableStateOf(actividadInicial?.diasRestantes?.toString() ?: "0") }
    var resuelto by remember(actividadInicial?.id) { mutableStateOf(actividadInicial?.resuelto ?: false) }

    val coloresCampos = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        cursorColor = Color.Black,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = Color.Gray
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = tituloPantalla,
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            colors = coloresCampos,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            colors = coloresCampos,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = progresoTexto,
            onValueChange = { progresoTexto = it },
            label = { Text("Progreso (%)") },
            colors = coloresCampos,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = diasTexto,
            onValueChange = { diasTexto = it },
            label = { Text("Días restantes") },
            colors = coloresCampos,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = resuelto,
                onCheckedChange = { resuelto = it }
            )
            Text(text = "Actividad resuelta / finalizada", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (titulo.isBlank()) return@Button
                    val progreso = progresoTexto.toIntOrNull() ?: 0
                    val diasRestantes = diasTexto.toIntOrNull() ?: 0

                    val actividad = (actividadInicial ?: ActividadFormativa(titulo = "")).copy(
                        titulo = titulo.trim(),
                        descripcion = descripcion.trim().ifBlank { null },
                        progreso = progreso,
                        diasRestantes = diasRestantes,
                        resuelto = resuelto
                    )
                    onGuardar(actividad)
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
 * Destino: Crear con campo de resuelto incorporado.
 */
@Composable
fun CrearRoute(
    viewModel: ActividadViewModel,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    FormularioActividad(
        actividadInicial = null,
        tituloPantalla = "Nueva actividad",
        onGuardar = { actividad ->
            viewModel.insertar(actividad)
            onGuardar()
        },
        onCancelar = onCancelar,
        modifier = modifier
    )
}

/**
 * Destino: Editar con campo de resuelto incorporado.
 */
@Composable
fun EditarRoute(
    actividad: ActividadFormativa?,
    viewModel: ActividadViewModel,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (actividad == null) {
        Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Actividad no encontrada")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onCancelar) { Text("Volver") }
        }
        return
    }

    FormularioActividad(
        actividadInicial = actividad,
        tituloPantalla = "Editar actividad",
        onGuardar = { actualizada ->
            viewModel.actualizar(actualizada)
            onGuardar()
        },
        onCancelar = onCancelar,
        modifier = modifier
    )
}

/**
 * Estado utilizado por la pantalla de detalle.
 */
sealed class EstadoDetalle {
    data class Encontrada(val actividad: ActividadFormativa) : EstadoDetalle()
    object NoEncontrada : EstadoDetalle()
}

/**
 * Destino: Detalle.
 * Gestiona la captura de evidencias fotográficas.
 */
@Composable
fun DetalleRoute(
    actividad: ActividadFormativa?,
    evidencias: List<Evidencia>,
    onVolver: () -> Unit,
    onCapturarFoto: (String, String, Long) -> Unit,
    onSeleccionarGaleria: (String, String, Long) -> Unit,
    onEliminarEvidencia: (Evidencia) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // URI temporal para la cámara
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para Galería (Photo Picker)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
            val size = context.contentResolver.openAssetFileDescriptor(uri, "r")?.use { it.length } ?: 0L
            onSeleccionarGaleria(uri.toString(), mime, size)
        }
    }

    // Launcher para Cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            val mime = "image/jpeg"
            val size = context.contentResolver.openAssetFileDescriptor(tempUri!!, "r")?.use { it.length } ?: 0L
            onCapturarFoto(tempUri.toString(), mime, size)
        }
    }

    // Launcher para Permiso de Cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File(context.cacheDir, "evidencias").apply { mkdirs() }
            val imageFile = File(file, "temp_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            tempUri = uri
            cameraLauncher.launch(uri)
        }
    }

    val estado = if (actividad != null) EstadoDetalle.Encontrada(actividad) else EstadoDetalle.NoEncontrada
    
    when (estado) {
        is EstadoDetalle.Encontrada -> {
            DetalleContenido(
                actividad = estado.actividad,
                evidencias = evidencias,
                onVolver = onVolver,
                onCapturarFoto = {
                    val permission = android.Manifest.permission.CAMERA
                    if (androidx.core.content.ContextCompat.checkSelfPermission(context, permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        val file = File(context.cacheDir, "evidencias").apply { mkdirs() }
                        val imageFile = File(file, "temp_${System.currentTimeMillis()}.jpg")
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            imageFile
                        )
                        tempUri = uri
                        cameraLauncher.launch(uri)
                    } else {
                        permissionLauncher.launch(permission)
                    }
                },
                onSeleccionarGaleria = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onEliminarEvidencia = onEliminarEvidencia,
                modifier = modifier
            )
        }
        EstadoDetalle.NoEncontrada -> {
            DetalleNoEncontrada(onVolver = onVolver, modifier = modifier)
        }
    }
}

@Composable
private fun DetalleContenido(
    actividad: ActividadFormativa,
    evidencias: List<Evidencia>,
    onVolver: () -> Unit,
    onCapturarFoto: () -> Unit,
    onSeleccionarGaleria: () -> Unit,
    onEliminarEvidencia: (Evidencia) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = actividad.titulo, style = MaterialTheme.typography.titleLarge)
            actividad.descripcion?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            Text(text = "Progreso: ${actividad.progreso}%")
        }

        // SECCION DE EVIDENCIAS (GUIA #9)
        item {
            EvidenciaSection(
                evidencias = evidencias,
                onCapturarFoto = onCapturarFoto,
                onSeleccionarGaleria = onSeleccionarGaleria,
                onEliminar = onEliminarEvidencia
            )
        }

        item {
            Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}

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
                contentDescription = "Actividad no encontrada. Es posible que el enlace sea incorrecto o que la actividad ya no exista."
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Actividad no encontrada", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Es posible que el enlace sea incorrecto o que la actividad ya no exista.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolver) { Text("Volver a la lista") }
    }
}