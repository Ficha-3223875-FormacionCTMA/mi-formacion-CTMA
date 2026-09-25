    package com.example.miformacionctma.ui.actividad

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.miformacionctma.domain.EstadoEvidencia
import com.example.miformacionctma.domain.Evidencia
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

/**
 * Selector y captura de evidencia fotográfica para una actividad.
 *
 * - Sin imagen: ofrece "Elegir de galería" (Photo Picker) y "Tomar foto" (cámara vía FileProvider).
 * - Con imagen: muestra el preview con Coil y permite reemplazarla o eliminarla.
 */
@Composable
fun SelectorEvidenciaFoto(
    actividadId: Long,
    onEvidenciaLista: (Evidencia) -> Unit,
    onEvidenciaEliminada: () -> Unit
) {
    val contexto = LocalContext.current

    // URI de la evidencia seleccionada; sobrevive a la recomposición.
    var uriFoto by rememberSaveable { mutableStateOf<String?>(null) }

    // URI temporal creada antes de delegar la captura en la cámara del sistema.
    var uriCamara by remember { mutableStateOf<Uri?>(null) }

    // Construye la Evidencia (LOCAL) a partir de la URI elegida o capturada.
    fun procesarUri(uri: Uri) {
        val tipoMime = contexto.contentResolver.getType(uri) ?: "image/jpeg"
        val tamanio = obtenerTamanoImagen(contexto, uri)

        uriFoto = uri.toString()
        onEvidenciaLista(
            Evidencia(
                actividadId = actividadId,
                uri = uri.toString(),
                tipoMime = tipoMime,
                tamanio = tamanio,
                estado = EstadoEvidencia.LOCAL
            )
        )
    }

    // Photo Picker: permite elegir UNA sola imagen sin acceso general a la galería.
    val lanzadorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { procesarUri(it) }
    }

    // Captura con la cámara del sistema sobre una content:// URI creada con FileProvider.
    val lanzadorCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { capturaExitosa: Boolean ->
        val uri = uriCamara
        uriCamara = null
        if (capturaExitosa && uri != null) {
            procesarUri(uri)
        }
    }

    // Solicitud de permiso de cámara en tiempo de ejecución
    val lanzadorPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            val uri = crearUriFoto(contexto)
            if (uri != null) {
                uriCamara = uri
                lanzadorCamara.launch(uri)
            }
        }
    }

    val uriActual = uriFoto?.let { Uri.parse(it) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (uriActual == null) {
            Text(
                text = "Adjuntar evidencia fotográfica",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        lanzadorGaleria.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Elegir de galería")
                }

                OutlinedButton(
                    onClick = {
                        val permiso = android.Manifest.permission.CAMERA
                        if (androidx.core.content.ContextCompat.checkSelfPermission(contexto, permiso) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            val uri = crearUriFoto(contexto)
                            if (uri != null) {
                                uriCamara = uri
                                lanzadorCamara.launch(uri)
                            }
                        } else {
                            lanzadorPermisoCamara.launch(permiso)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tomar foto")
                }
            }
        } else {
            AsyncImage(
                model = uriActual,
                contentDescription = "Foto de la evidencia seleccionada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        lanzadorGaleria.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reemplazar")
                }

                OutlinedButton(
                    onClick = {
                        uriFoto = null
                        onEvidenciaEliminada()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

/**
 * Calcula el tamaño en bytes de una imagen consultando ContentResolver.
 * FileProvider no soporta query(), por lo que se lee el descriptor como respaldo.
 */
private fun obtenerTamanoImagen(contexto: Context, uri: Uri): Long {
    val contentResolver = contexto.contentResolver

    contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val indiceTamanio = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (indiceTamanio >= 0 && !cursor.isNull(indiceTamanio)) {
                val tamanio = cursor.getLong(indiceTamanio)
                if (tamanio > 0L) return tamanio
            }
        }
    }

    return try {
        contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
            descriptor.statSize
        } ?: 0L
    } catch (e: Exception) {
        0L
    }
}

@Preview(showBackground = true, name = "Selector de evidencia (sin imagen)")
@Composable
private fun SelectorEvidenciaFotoVacioPreview() {
    MiFormacionCTMATheme {
        SelectorEvidenciaFoto(
            actividadId = 1L,
            onEvidenciaLista = {},
            onEvidenciaEliminada = {}
        )
    }
}