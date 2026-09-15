# Plan de Implementación - Semana 7: Reactividad Avanzada y Estados de UI

Este plan detalla la evolución de la arquitectura de **Mi Formación CTMA** hacia un modelo de estados robusto, utilizando Corrutinas de Kotlin, Flows avanzados y `StateFlow`. El objetivo es mejorar la resiliencia de la app ante errores y optimizar el rendimiento de la interfaz.

## User Review Required

> [!IMPORTANT]
> - **Sin cambios en los datos:** Room y SQLite permanecen intactos. No se perderá ninguna actividad guardada.
> - **Refactorización del ViewModel:** Se cambiará la forma en que la UI consume los datos para permitir el manejo de estados "Cargando" y "Error".
> - **Nuevas Dependencias:** Se añadirá `androidx.lifecycle:lifecycle-runtime-compose` para un consumo de flujos más seguro en Compose.

## Proposed Changes

### 1. Preparación del Entorno
#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/build.gradle.kts)
- Añadir `androidx.lifecycle:lifecycle-runtime-compose` para soportar `collectAsStateWithLifecycle()`.
- Añadir `kotlinx-coroutines-test` para las pruebas de JD.

### 2. Definición de Estados (Parte de Miguel)
#### [NEW] [ActividadUiState.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/actividad/ActividadUiState.kt)
- Definir `sealed interface ListadoUiState`: `Cargando`, `Contenido`, `Vacio`, `Error`.
- Definir `sealed interface OperacionUiState`: `Inactiva`, `EnCurso`, `Exitosa`, `Fallida`.

### 3. Evolución de la Lógica (Parte de Miguel y Laverde)
#### [MODIFY] [ActividadViewModel.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/actividad/ActividadViewModel.kt)
- Utilizar `stateIn` para transformar los Flows del repositorio en `StateFlow`.
- Implementar la lógica de búsqueda con `flatMapLatest` para cancelar consultas obsoletas.
- Manejar bloques `try-catch` dentro del `viewModelScope` para emitir estados de `Error`.

### 4. Interfaz de Usuario Reactiva (Parte de Arrunchis)
#### [MODIFY] [navegacion.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/navegacion.kt)
- Migrar de `collectAsState` a `collectAsStateWithLifecycle` (mejor para el ahorro de batería).
- Implementar un bloque `when(uiState)` para mostrar un `CircularProgressIndicator` cuando esté cargando o un mensaje de error si algo falla.

## Verification Plan

### Automated Tests (Parte de JD)
- Ejecutar `ActividadViewModelTest` usando `runTest` para verificar que:
  1. Al iniciar se emite el estado `Cargando`.
  2. Al recibir datos se emite `Contenido`.
  3. Al escribir en la búsqueda se cancela la ejecución anterior.

### Manual Verification
- Abrir la app y observar la transición fluida de carga.
- Forzar un error (ej. desconectar el driver de SQLite temporalmente en código) para verificar que la UI muestra el estado de `Error` y no se cierra la app.
