package com.example.miformacionctma.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class NotificationPermissionHelper(private val activity: ComponentActivity) {

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    init {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, puedes proceder a mostrar notificaciones
            } else {
                // Permiso denegado, informar al usuario si es necesario
            }
        }
    }

    /**
     * Verifica y solicita el permiso de notificaciones si es necesario.
     * Solo para Android 13+ (SDK 33).
     */
    fun checkAndRequestPermission(onPermissionAlreadyGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    onPermissionAlreadyGranted()
                }
                else -> {
                    requestPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // En versiones anteriores a Android 13 el permiso se concede al instalar
            onPermissionAlreadyGranted()
        }
    }
}
