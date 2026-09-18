# Plan de Implementación: Finalización Guía 8 (Perfil Laverde)

Este plan aborda los puntos pendientes de la Distribución Propuesta de la Guía 8, enfocándose en la retroalimentación visual de sincronización y la documentación técnica de riesgos y pruebas.

## User Review Required

> [!IMPORTANT]
> - Se añadirá una pequeña barra de progreso en la parte superior de la lista de actividades que solo aparecerá durante la sincronización con el servidor.
> - Se actualizará el README con la matriz de riesgos de red y los resultados de los 8 casos de prueba obligatorios.

## Proposed Changes

### 1. Interfaz y ViewModel (Feedback Visual)
#### [MODIFY] [ActividadViewModel.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/actividad/ActividadViewModel.kt)
- Implementar un `StateFlow<Boolean>` llamado `estaSincronizando` que se active al iniciar el `refrescar()` y se desactive al finalizar (éxito o error).

#### [MODIFY] [navegacion.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/navegacion.kt)
- Añadir un `LinearProgressIndicator` en el encabezado de la lista, condicionado al estado `estaSincronizando`.

### 2. Documentación (README)
#### [MODIFY] [README.md](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/README.md)
- Añadir sección **"Matriz Riesgo–Respuesta (Capa de Red)"**.
- Añadir sección **"Reporte de Pruebas - Guía 8"** documentando los 8 escenarios:
  1. Carga inicial exitosa.
  2. Modo offline (vuelo) con datos previos.
  3. Primer inicio sin red (Error fatal).
  4. Error 401 (Sesión expirada).
  5. Timeout de conexión.
  6. Cancelación de búsqueda rápida.
  7. Reintento manual tras error.
  8. Sincronización en segundo plano con éxito.

## Verification Plan

### Automated Tests
- Ejecutar `ActividadViewModelTest.kt` para asegurar que el estado `estaSincronizando` cambia correctamente.

### Manual Verification
- Abrir la app y verificar que la barra de progreso aparece brevemente.
- Simular un error de red y verificar que la barra desaparece y se muestra el Snackbar de "Modo offline".
