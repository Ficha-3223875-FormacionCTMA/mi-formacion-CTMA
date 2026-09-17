# Plan de Implementación: Integración de Networking y Sincronización (Miguel)

Este plan detalla las tareas técnicas asignadas a Miguel para integrar la capa de red (Retrofit + Kotlin Serialization) en la arquitectura existente de **Mi Formación CTMA**, siguiendo un enfoque de "Offline-First".

## User Review Required

> [!IMPORTANT]
> - **Estrategia de Sincronización:** El Repositorio actuará como mediador. Los datos remotos se guardarán siempre en Room (fuente de verdad) para asegurar el funcionamiento offline.
> - **Manejo de Errores:** Se implementará una clasificación de errores (Red, 401, Servidor) para que la UI pueda reaccionar adecuadamente.
> - **Seguridad:** Se configurará un `TokenProvider` básico para manejar el encabezado `Authorization`.

## Proposed Changes

### 1. Modelos de Datos (DTO) y Mapeos
#### [NEW] [ActividadDto.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/remote/dto/ActividadDto.kt)
- Crear el modelo de datos para la API utilizando `@Serializable`.

#### [MODIFY] [ActividadMapper.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/mapper/ActividadMapper.kt)
- Añadir extensiones para mapear de `ActividadDto` a `ActividadEntity` (Room) y `ActividadFormativa` (Dominio).

### 2. Configuración de Red (Retrofit + OkHttp)
#### [NEW] [ActividadApiService.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/remote/api/ActividadApiService.kt)
- Definir los endpoints de la API (`GET /actividades`, `POST /actividades`, etc.).

#### [NEW] [NetworkModule.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/remote/NetworkModule.kt)
- Proveer la instancia única de Retrofit, OkHttp con Logging Interceptor y el conversor de Kotlin Serialization.

### 3. Fuente de Datos Remota
#### [NEW] [RemoteActividadDataSource.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/remote/RemoteActividadDataSource.kt)
- Implementar la lógica para llamar a la API y manejar la clasificación de excepciones de red.

### 4. Integración en el Repositorio
#### [MODIFY] [ActividadRepository.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/repository/ActividadRepository.kt)
- Añadir la función `refreshActividades()` que descargue de la red y actualice la base de datos local.
- Integrar la lógica de creación/edición para que se envíe al servidor además de Room.

### 5. Seguridad y Utilidades
#### [NEW] [TokenProvider.kt](file:///C:/Users/MiguelFormacion.LenovoLOQ_MIGAN/AndroidStudioProjects/MiFormacionCTMA/app/src/main/java/com/example/miformacionctma/data/remote/auth/TokenProvider.kt)
- Implementar la gestión de tokens para el encabezado `Authorization`.

## Verification Plan

### Automated Tests
- Ejecutar pruebas con `MockWebServer` para validar que el `RemoteActividadDataSource` procesa correctamente el JSON y los códigos de error (404, 500, 401).

### Manual Verification
- Iniciar la aplicación y observar en el **Logcat** (etiqueta OkHttp) las peticiones de red salientes.
- Verificar que al registrar una actividad, esta se persista en Room incluso si la red falla momentáneamente.
