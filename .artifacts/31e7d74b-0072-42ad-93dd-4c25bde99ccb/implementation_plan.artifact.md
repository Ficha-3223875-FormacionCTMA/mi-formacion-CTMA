# Plan de Mejora de Visibilidad y Flujo de Evidencias (Guía #9)

Este plan corrige la ausencia de la sección de captura de evidencias en la interfaz de usuario, asegurando que los controles de cámara y galería sean visibles y funcionales tanto en la vista de detalles como en la de edición.

## User Review Required

> [!IMPORTANT]
> - Se integrará la sección de evidencias en la pantalla de **Edición**, ya que es el lugar donde los usuarios suelen adjuntar archivos por primera vez.
> - Se mejorará la robustez de la pantalla de **Detalle** para asegurar que la información se cargue correctamente incluso si el estado global de la lista está en transición.

## Proposed Changes

### 1. Capa de Presentación (ViewModel)

#### [MODIFY] [ActividadViewModel.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/actividad/ActividadViewModel.kt)
- Añadir un flujo reactivo para obtener una única actividad por ID (`val actividadSeleccionada`).
- Asegurar que al seleccionar una actividad, se carguen tanto sus datos básicos como sus evidencias de forma coordinada.

### 2. Interfaz de Usuario (Compose)

#### [MODIFY] [navegacion.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/navegacion.kt)
- **EditarRoute / FormularioActividad:** Integrar el componente `EvidenciaSection`. Esto permitirá adjuntar fotos mientras se editan otros campos de la actividad.
- **DetalleRoute:** Utilizar el nuevo flujo `actividadSeleccionada` del ViewModel para evitar que la pantalla se muestre vacía si la lista global no ha terminado de cargar.
- **ListaRoute:** Asegurar que el clic en la tarjeta (Card) y el botón "Editar detalles" lleven a experiencias consistentes.

## Verification Plan

### Manual Verification
1. Abrir la app y hacer clic en una actividad de la lista ➔ La sección de evidencias debe ser visible al final del scroll.
2. Hacer clic en "Editar detalles" ➔ La sección de evidencias debe aparecer también en el formulario de edición.
3. Capturar una foto ➔ Verificar que aparece la miniatura en ambas pantallas (Detalle y Edición).
4. Reiniciar la app ➔ Confirmar que las evidencias persistieron en Room.
