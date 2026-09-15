# Plan de Mejora de Visibilidad de Interfaz

Este plan aborda el problema de visibilidad en los campos de entrada de texto donde el texto es blanco sobre un fondo claro, haciendo que sea ilegible. Además, se asegurará que el tema oscuro se aplique correctamente a toda la aplicación.

## User Review Required

> [!IMPORTANT]
> Los cambios son puramente estéticos. Se forzará el color del texto a negro y el fondo de los cuadros a blanco para garantizar la legibilidad absoluta, independientemente de si el teléfono está en modo claro u oscuro.

## Proposed Changes

### Interfaz de Usuario (UI)

#### [MODIFY] [navegacion.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/navegacion.kt)
- Se personalizarán los colores de todos los `OutlinedTextField` en las pantallas de **Crear** y **Editar**.
- Se forzará el texto a `Color.Black` y el contenedor a `Color.White`.

### Configuración Principal

#### [MODIFY] [MainActivity.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/MainActivity.kt)
- Se envolverá la navegación principal en un componente `Surface`. Esto aplicará el color `FondoOscuro` (azul oscuro) definido en el tema a toda la pantalla, evitando que se vea el blanco predeterminado del sistema.

## Verification Plan

### Manual Verification
- Ejecutar la aplicación en el emulador.
- Navegar a "Nueva Actividad".
- Escribir texto y verificar que el fondo sea blanco, el texto negro y el fondo general de la app azul oscuro.
- Repetir la verificación en la pantalla de "Editar".
