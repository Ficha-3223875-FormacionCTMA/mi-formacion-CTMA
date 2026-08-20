package com.example.miformacionctma.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaColorMiFormacion = darkColorScheme(
    primary = AzulPrimario,
    onPrimary = AzulTextoSobrePrimario,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulTextoSobreContenedor,
    secondary = VerdeSecundario,
    onSecondary = VerdeTextoSobreSecundario,
    secondaryContainer = VerdeContenedor,
    onSecondaryContainer = VerdeTextoSobreContenedor,
    background = FondoOscuro,
    onBackground = TextoSobreFondo,
    surface = SuperficieOscura,
    onSurface = TextoSobreFondo,
    surfaceVariant = SuperficieVarianteOscura,
    onSurfaceVariant = TextoSecundarioSobreFondo
    // error/errorContainer se dejan en el valor por defecto de darkColorScheme():
    // ya trae un rojo pensado para fondos oscuros, útil para el estado "Vencida"
)

@Composable
fun MiFormacionCTMATheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaColorMiFormacion,
        typography = Typography,
        content = content
    )
}