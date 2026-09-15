# Walkthrough - Semana 7: Reactividad Avanzada con Corrutinas y Flows

Se ha evolucionado la arquitectura de **Mi Formación CTMA** hacia un modelo totalmente reactivo, implementando una gestión de estados robusta y optimizando el consumo de datos desde la base de datos.

## Cambios Principales

### 🔄 Gestión de Estados con UiState
Se introdujeron interfaces selladas (`sealed interface`) para representar de forma explícita lo que sucede en la pantalla:
- **ListadoUiState:** Permite a la UI reaccionar a estados de `Cargando`, `Contenido`, `Vacio` o `Error`.
- **OperacionUiState:** Gestiona el ciclo de vida de las operaciones de escritura (inserción, edición, eliminación), informando si están `EnCurso`, si fueron `Exitosas` o si han `Fallado`.

### ⚡ Flujos de Datos Optimizados
- **StateFlow:** El `ActividadViewModel` ahora utiliza `stateIn` para transformar flujos fríos del repositorio en flujos de estado calientes, manteniendo la última emisión disponible para la UI.
- **Búsqueda Cancelable:** Se implementó `flatMapLatest` en la lógica de búsqueda. Esto garantiza que si el usuario escribe rápidamente, las consultas previas a SQLite se cancelan automáticamente, ahorrando recursos.

### 🎨 UI Resiliente y Eficiente
- **collectAsStateWithLifecycle:** Se migró el consumo de flujos en Compose a esta API de ciclo de vida seguro, lo que evita el procesamiento de datos cuando la app está en segundo plano.
- **Indicadores Visuales:** Se añadieron componentes como `CircularProgressIndicator` y mensajes de error descriptivos basados en el estado actual.

### 🧪 Pruebas Unitarias de Corrutinas
Se creó la suite [ActividadViewModelTest.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/test/java/com/example/miformacionctma/ui/actividad/ActividadViewModelTest.kt) que certifica:
- La correcta emisión de estados iniciales.
- El filtrado de datos mediante la búsqueda.
- La gestión de la concurrencia y el tiempo virtual con `runTest`.

## Verificación

- **Pruebas Unitarias:** Ejecutadas exitosamente (`14 passed`).
- **Manual:** Se verificó en el emulador que la búsqueda es instantánea y que la UI responde correctamente a los cambios de Room sin parpadeos ni bloqueos.

```kotlin
// Ejemplo del nuevo flujo reactivo en el ViewModel
val uiState: StateFlow<ListadoUiState> = combine(_textoBusqueda, _soloUrgentes) { ... }
    .flatMapLatest { (query, urgentes) -> repository.buscar(query) }
    .map { lista -> if (lista.isEmpty()) Vacio else Contenido(lista) }
    .stateIn(scope = viewModelScope, ...)
```
