package com.example.miformacionctma.ui.actividad

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Genera el archivo de destino para capturar una foto con la cámara del sistema
 * y devuelve su content:// URI a través de FileProvider.
 *
 * El archivo se crea dentro de la carpeta interna "evidencias"
 * (Context.getExternalFilesDir), declarada en res/xml/file_paths.xml.
 *
 * @return la content:// URI otorgada por FileProvider, o null si no fue posible
 *         crear el archivo o la URI.
 */
fun crearUriFoto(contexto: Context): Uri? {
    val directorio = contexto.getExternalFilesDir("evidencias") ?: return null
    if (!directorio.exists() && !directorio.mkdirs()) {
        return null
    }

    // Nombre con marca de tiempo para evitar colisiones entre capturas.
    val nombreArchivo = "evidencia_" +
            SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.ROOT).format(Date()) +
            ".jpg"
    val archivo = File(directorio, nombreArchivo)

    return try {
        FileProvider.getUriForFile(
            contexto,
            "${contexto.packageName}.fileprovider",
            archivo
        )
    } catch (e: Exception) {
        null
    }
}