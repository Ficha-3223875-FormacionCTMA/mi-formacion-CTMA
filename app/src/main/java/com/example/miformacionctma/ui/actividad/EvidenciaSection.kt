package com.example.miformacionctma.ui.actividad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.miformacionctma.domain.EstadoEvidencia
import com.example.miformacionctma.domain.Evidencia

@Composable
fun EvidenciaSection(
    evidencias: List<Evidencia>,
    onCapturarFoto: () -> Unit,
    onSeleccionarGaleria: () -> Unit,
    onEliminar: (Evidencia) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Evidencias Fotográficas",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onCapturarFoto,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cámara")
            }

            OutlinedButton(
                onClick = onSeleccionarGaleria,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Galería")
            }
        }

        if (evidencias.isEmpty()) {
            Text(
                text = "No has adjuntado evidencias para esta actividad.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(evidencias, key = { it.id }) { evidencia ->
                    EvidenciaItem(
                        evidencia = evidencia,
                        onEliminar = { onEliminar(evidencia) }
                    )
                }
            }
        }
    }
}

@Composable
fun EvidenciaItem(
    evidencia: Evidencia,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(150.dp, 200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = evidencia.uri,
                contentDescription = "Evidencia fotográfica",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Indicador de estado
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                val color = when (evidencia.estado) {
                    EstadoEvidencia.SINCRONIZADA -> MaterialTheme.colorScheme.primary
                    EstadoEvidencia.FALLIDA -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.secondary
                }
                Surface(
                    color = color,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = evidencia.estado.name,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Botón eliminar
            IconButton(
                onClick = onEliminar,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.errorContainer
                )
            }
        }
    }
}
