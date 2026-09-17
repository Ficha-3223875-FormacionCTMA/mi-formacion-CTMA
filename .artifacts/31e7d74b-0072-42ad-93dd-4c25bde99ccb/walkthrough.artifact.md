# Walkthrough - Semana 8: Networking y Sincronización (Offline-First)

Se ha completado la integración de la capa de red para el perfil de **Miguel**, permitiendo que la aplicación se sincronice con un servidor externo mientras mantiene su funcionalidad sin conexión.

## Logros Técnicos

### 🌐 Capa de Red Robusta
Se configuró **Retrofit** con **Kotlinx Serialization** para procesar los datos del servidor. Se incluyeron interceptores de OkHttp para:
- **Logging:** Visualizar todas las peticiones y respuestas en el Logcat durante el desarrollo.
- **Autenticación:** Añadir automáticamente el encabezado `Authorization: Bearer <token>` mediante un `TokenProvider`.

### 🔄 Sincronización Offline-First
El `ActividadRepository` ahora orquestra la sincronización:
- Al iniciar la app, se dispara un `refreshActividades()` que descarga los datos remotos y los guarda en **Room**.
- Room sigue siendo la única fuente de verdad para la UI, garantizando que el usuario siempre vea datos, incluso sin internet.

### 🛡️ Manejo de Errores Avanzado
Se implementó una clasificación de errores en `RemoteActividadDataSource` para capturar específicamente:
- Fallos de conectividad (`IOException`).
- Sesiones expiradas (`401`).
- Recursos no encontrados (`404`).

## Cambios en la Arquitectura

Se introdujeron los siguientes componentes nuevos:
- **`ActividadDto`**: Modelo de datos específico para la transferencia por red.
- **`ActividadApiService`**: Definición de los contratos de los endpoints.
- **`ActividadMapper`**: Extensiones para convertir entre DTO, Entity y Domain de forma limpia.

## Verificación Realizada

1. **Compilación:** El proyecto compila correctamente con todas las nuevas dependencias de red.
2. **Arquitectura:** Se verificó que el flujo cumple con la separación de responsabilidades (Clean Architecture).
3. **Persistencia:** Se validó que la lógica de sincronización actualiza correctamente la base de datos local de Room.

> [!TIP]
> Para probar la autenticación, puedes usar `TokenProvider.setToken("tu_token")` en el `MainActivity` y observar las peticiones en el Logcat filtrando por `OkHttp`.
