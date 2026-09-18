# Plan de Limpieza de Dependencias (libs.versions.toml y build.gradle.kts)

Este plan tiene como objetivo eliminar las duplicidades de librerías y versiones identificadas en el catálogo de versiones y en el archivo de construcción del módulo app, consolidando una única fuente de verdad y utilizando las versiones más estables y recientes.

## User Review Required

> [!IMPORTANT]
> Se consolidarán las versiones de Retrofit (2.11.0) y Kotlinx Serialization (1.8.0). Se eliminarán las declaraciones duplicadas en el bloque `dependencies` de `app/build.gradle.kts`, priorizando el uso de los alias del catálogo de versiones sobre las cadenas de texto hardcodeadas.

## Proposed Changes

### Catálogo de Versiones

#### [MODIFY] [libs.versions.toml](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/gradle/libs.versions.toml)
- Consolidar la sección `[versions]`, eliminando las definiciones duplicadas de `retrofit`, `okhttp` y `kotlinxSerialization`.
- Limpiar la sección `[libraries]`, eliminando las definiciones duplicadas de los componentes de Networking (Retrofit, OkHttp, etc.).
- Organizar las librerías por categorías lógicas.

### Configuración del Módulo App

#### [MODIFY] [build.gradle.kts (app)](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/build.gradle.kts)
- Eliminar el bloque duplicado de "Servicios web" que repite las dependencias de "Networking".
- Eliminar la declaración duplicada de "DataStore".
- Reemplazar las dependencias de prueba hardcodeadas (JUnit, Coroutines Test, MockK) por sus equivalentes del catálogo de versiones (`libs.*`).

## Verification Plan

### Automated Tests
- Ejecutar `./gradlew test` para asegurar que las librerías de prueba siguen funcionando correctamente tras la migración al catálogo.

### Manual Verification
- Realizar un **Gradle Sync** para confirmar que no hay conflictos de nombres o versiones.
- Ejecutar la aplicación para verificar que la capa de red (Retrofit) y la persistencia (Room/DataStore) operan normalmente.
