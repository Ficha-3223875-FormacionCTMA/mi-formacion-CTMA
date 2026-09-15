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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad

@Composable
fun ActividadScreen(
    viewModel: ActividadViewModel
) {
    val actividades by viewModel.actividades.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Actividades: ${actividades.size}",
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = actividades,
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