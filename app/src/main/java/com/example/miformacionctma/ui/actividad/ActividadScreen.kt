package com.example.miformacionctma.ui.actividad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad

@Composable
fun ActividadScreen(
    viewModel: ActividadViewModel
) {
    val actividades by viewModel.actividades.collectAsStateWithLifecycle()

    var textoBusqueda by rememberSaveable {
        androidx.compose.runtime.mutableStateOf("")
    }

    val actividadesFiltradas = actividades.filter { actividad ->
        textoBusqueda.isBlank() ||
                actividad.titulo.contains(textoBusqueda, ignoreCase = true) ||
                (!actividad.descripcion.isNullOrBlank() &&
                        actividad.descripcion.contains(textoBusqueda, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Actividades: ${actividadesFiltradas.size}",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = {
                Text("Buscar actividad")
            },
            placeholder = {
                Text("Escribe un título o descripción")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Button(
            onClick = {
                viewModel.insertar(
                    ActividadFormativa(
                        titulo = "Actividad de prueba",
                        descripcion = "Probando Room y SQLite",
                        progreso = 0,
                        diasRestantes = 5,
                        prioridad = Prioridad.MEDIA
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Agregar actividad")
        }

        when {
            actividades.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()

                    Text(
                        text = "Cargando actividades...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            actividadesFiltradas.isEmpty() -> {
                Text(
                    text = "No se encontraron actividades",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
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

                                Text(
                                    text = "Progreso: ${actividad.progreso}%"
                                )

                                Text(
                                    text = "Días restantes: ${actividad.diasRestantes}"
                                )

                                Text(
                                    text = "Prioridad: ${actividad.prioridad}"
                                )

                                if (!actividad.descripcion.isNullOrBlank()) {
                                    Text(
                                        text = actividad.descripcion
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}