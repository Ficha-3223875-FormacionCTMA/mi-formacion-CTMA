# Plan de Implementación: Integración de UI, Búsqueda y Migración de Room (Guía 6)

Este plan detalla los pasos para completar los requisitos de la Guía 6, incluyendo la conexión total de las funciones de datos con la interfaz de usuario, la integración de búsqueda/filtrado y la ejecución de la migración de la base de datos Room de versión 1 a versión 2.

## User Review Required

> [!IMPORTANT]
> - Se modificará la estructura de la base de datos añadiendo el campo `resuelto` (boolean).
> - Se mantendrá intacto el flujo reactivo y la arquitectura actual por capas (Domain -> Data/Repository -> UI).
> - La búsqueda se ejecutará en tiempo real directamente desde la base de datos SQLite utilizando las consultas reactivas preparadas en el DAO.

## Proposed Changes

### 1. Capa de Datos y Dominio (Modelos y Mapeadores)

#### [MODIFY] [ActividadFormativa.kt (Domain)](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/domain/ActividadFormativa.kt)
- Añadir el campo `val resuelto: Boolean = false` al modelo de dominio.

#### [MODIFY] [ActividadFormativa.kt (Entity)](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/local/entity/ActividadFormativa.kt)
- Añadir la columna `val resuelto: Boolean = false` a la entidad Room.

#### [MODIFY] [ActividadMapper.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/mapper/ActividadMapper.kt)
- Actualizar las funciones `toDomain()` y `toEntity()` para incluir el mapeo bidireccional del campo `resuelto`.

---

### 2. Capa de Datos (Repositorio, Base de Datos y Migración)

#### [MODIFY] [ActividadRepository.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/repository/ActividadRepository.kt)
- Exponer la función `buscarPorTexto(query: String): Flow<List<ActividadFormativa>>` llamando al DAO.

#### [MODIFY] [AppDatabase.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/local/AppDatabase.kt)
- Incrementar la versión de `@Database` de `1` a `2`.
- Definir un objeto de migración estático `MIGRATION_1_2` que ejecute la sentencia SQL:
  `ALTER TABLE actividades ADD COLUMN resuelto INTEGER NOT NULL DEFAULT 0`

#### [MODIFY] [DatabaseProvider.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/local/DatabaseProvider.kt)
- Registrar la migración `MIGRATION_1_2` en el `databaseBuilder` utilizando `.addMigrations(AppDatabase.MIGRATION_1_2)`.

---

### 3. Capa de Presentación (ViewModel e Interfaz de Usuario)

#### [MODIFY] [ActividadViewModel.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/actividad/ActividadViewModel.kt)
- Añadir un estado mutable para el texto de búsqueda (`val textoBusqueda = MutableStateFlow("")`).
- Añadir un estado mutable para el filtro de urgencia (`val soloUrgentes = MutableStateFlow(false)`).
- Combinar reactivamente (`combine`) estos estados con la consulta de la base de datos para ofrecer filtrado en tiempo real sin recargas manuales.

#### [MODIFY] [navegacion.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/ui/navegacion.kt)
- **ListaRoute:** Añadir un campo de búsqueda (`OutlinedTextField`) arriba de la lista.
- Añadir un control de filtro (un interruptor o botón tipo Chip) para activar/desactivar la visualización de "Solo urgentes" utilizando la función existente de `ReglasActividad.actividadesUrgentes`.
- Actualizar los formularios de creación y edición para incluir un checkbox o switch que permita modificar el estado `resuelto` de la actividad.

---

### 4. Pruebas Automáticas y Evidencias

#### [NEW] [BaseDatosTest.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/androidTest/java/com/example/miformacionctma/data/local/BaseDatosTest.kt)
- Crear una clase de prueba instrumentada para validar:
  1. El funcionamiento correcto del DAO (inserción, eliminación, actualización y búsqueda).
  2. La correcta ejecución de la migración de versión 1 a versión 2 sin pérdida de datos.

#### [MODIFY] [README.md](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/README.md)
- Actualizar la documentación reflejando el nuevo diseño, la migración exitosa y los resultados del plan de pruebas instrumentadas.

---

## Verification Plan

### Automated Tests
- Ejecutar las pruebas instrumentadas del DAO y de la migración utilizando el comando de Gradle:
  `gradle_build("app:connectedAndroidTest")` o ejecutándolas directamente.

### Manual Verification
- Iniciar la aplicación, registrar actividades, cerrarla por completo (matar proceso) y volverla a abrir para certificar la persistencia física en SQLite.
- Probar que al escribir en la barra de búsqueda de la UI, la lista se filtre instantáneamente.
