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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Encabezado simplificado
        Text(
            text = "Mis Actividades",
            style = MaterialTheme.typography.headlineSmall
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

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            ListadoUiState.Cargando -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ListadoUiState.Contenido -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = (uiState as ListadoUiState.Contenido).actividades,
                        key = { actividad -> actividad.id }
                    ) { actividad ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = actividad.titulo, style = MaterialTheme.typography.titleMedium)
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
            ListadoUiState.Vacio -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay actividades.")
                }
            }
            is ListadoUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${(uiState as ListadoUiState.Error).mensaje}")
                }
            }
        }
    }
}
