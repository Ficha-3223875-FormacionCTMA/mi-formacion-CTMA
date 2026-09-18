# Walkthrough - Guía #9: Capacidades del Dispositivo (Cámara, Galería y Notificaciones)

Se ha completado la integración funcional de las capacidades de hardware en **Mi Formación CTMA**, permitiendo la captura y gestión de evidencias fotográficas con total seguridad y cumplimiento de los requisitos de la Guía #9.

## Logros Realizados

### 📸 Gestión de Evidencias (Cámara y Galería)
- **Selección de Galería:** Se integró el nuevo **Photo Picker** (`PickVisualMedia`) para permitir al usuario elegir imágenes sin solicitar permisos invasivos de acceso a toda la galería (Mínimo Privilegio).
- **Captura con Cámara:** Se implementó la captura de fotos mediante `TakePicture`, utilizando **FileProvider** para generar una URI segura (`content://`) y evitar la exposición de rutas de archivos reales (`file://`).
- **Visualización en Tiempo Real:** Se integró la librería **Coil** para mostrar miniaturas de las evidencias capturadas en la pantalla de detalles.

### 🛡️ Seguridad y Permisos
- **POST_NOTIFICATIONS:** Se implementó el flujo de solicitud de permiso de notificaciones para Android 13+. La solicitud solo ocurre si el usuario intenta activar los recordatorios manualmente en la lista de actividades.
- **Integridad de Datos:** Las evidencias se guardan localmente en **Room** con estados claros (`LOCAL`, `SUBIENDO`, `SINCRONIZADA`, `FALLIDA`), garantizando que la información no se pierda si falla la conexión.

### 🏗️ Evolución de la Arquitectura
- **ViewModel Dinámico:** El `ActividadViewModel` ahora orquestra tanto la lógica de actividades como la de evidencias, gestionando la inyección de dependencias a través de la fábrica actualizada.
- **UI Modular:** Se creó el componente `EvidenciaSection` para desacoplar la lógica de imágenes del resto de la interfaz.

## Verificación

1. **Flujo de Cámara:** Al pulsar "Cámara" en el detalle, se abre la aplicación del sistema, se captura la foto y esta aparece instantáneamente en la lista de evidencias.
2. **Persistencia:** Al reiniciar la app, las evidencias asociadas a cada actividad permanecen visibles (recuperadas de SQLite).
3. **Mínimo Privilegio:** Se verificó en el manifiesto que no se solicitan permisos de almacenamiento innecesarios.

```kotlin
// Ejemplo de launcher seguro implementado en navegacion.kt
val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
) { success ->
    if (success && tempUri != null) {
        viewModel.adjuntarEvidencia(tempUri.toString(), "image/jpeg", size)
    }
}
```

> [!IMPORTANT]
> El sistema está listo para la Semana 10. Se ha garantizado que no hay filtraciones de tokens en Logcat y que todo el tráfico de red se realiza bajo el esquema HTTPS.
