package com.example.miformacionctma.ui.actividad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ActividadScreen(
    viewModel: ActividadViewModel
) {
    // Recolección segura del estado emitido por el ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var textoBusqueda by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Mis Actividades",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("Buscar actividad") },
            placeholder = { Text("Escribe un título o descripción") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Representación visual de los estados (Rol de Arrunchis)
        when (val state = uiState) {
            is ListadoUiState.Cargando -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ListadoUiState.Vacio -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay actividades registradas.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is ListadoUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.mensaje}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is ListadoUiState.Contenido -> {
                val actividadesFiltradas = state.actividades.filter { actividad ->
                    textoBusqueda.isBlank() ||
                            actividad.titulo.contains(textoBusqueda, ignoreCase = true) ||
                            (!actividad.descripcion.isNullOrBlank() &&
                                    actividad.descripcion.contains(textoBusqueda, ignoreCase = true))
                }

                if (actividadesFiltradas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron actividades.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = actividadesFiltradas,
                            key = { actividad -> actividad.id }
                        ) { actividad ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = actividad.titulo,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(text = "Progreso: ${actividad.progreso}%")
                                    Text(text = "Días restantes: ${actividad.diasRestantes}")
                                    Text(text = "Prioridad: ${actividad.prioridad}")

                                    if (!actividad.descripcion.isNullOrBlank()) {
                                        Text(text = actividad.descripcion)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}