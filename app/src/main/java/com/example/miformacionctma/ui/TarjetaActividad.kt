package com.example.miformacionctma.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.ReglasActividad

@Composable
fun EtiquetaEstado(
    texto: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = containerColor
    ) {
        Text(
            text = texto,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun TarjetaActividad(
    actividad: ActividadFormativa,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado = ReglasActividad.estadoActividad(actividad)
    val colorScheme = MaterialTheme.colorScheme
    val (containerColor, contentColor) = coloresParaEstado(estado, colorScheme)

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = actividad.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            actividad.descripcion?.let { descripcion ->
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EtiquetaEstado(
                    texto = estado,
                    containerColor = containerColor,
                    contentColor = contentColor
                )
                Text(
                    text = "${actividad.progreso}%",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                text = textoDiasRestantes(actividad.diasRestantes),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun coloresParaEstado(estado: String, colorScheme: ColorScheme): Pair<Color, Color> =
    when (estado) {
        "Completada" -> colorScheme.secondaryContainer to colorScheme.onSecondaryContainer
        "En proceso" -> colorScheme.primaryContainer to colorScheme.onPrimaryContainer
        "Vencida" -> colorScheme.errorContainer to colorScheme.onErrorContainer
        else -> colorScheme.surfaceVariant to colorScheme.onSurfaceVariant // Pendiente
    }

private fun textoDiasRestantes(dias: Int): String = when {
    dias < 0 -> "Vencida hace ${-dias} día(s)"
    dias == 0 -> "Entrega hoy"
    else -> "Faltan $dias día(s)"
}