# Contrato hipotético de API — Mi Formación CTMA

## 1. Propósito

Este documento define un contrato hipotético de API REST para **Mi Formación CTMA**.

El contrato se utilizará como referencia para la construcción de un **Mock Server en Postman** durante el Laboratorio 2 de la Semana 4.

La API descrita en este documento es **hipotética** y no representa un backend actualmente implementado en la aplicación Android. Su propósito es establecer una interfaz común para realizar pruebas de integración y validar las reglas de negocio definidas en `ReglasActividad`.

---

# 2. Modelo de datos

El recurso principal de la API es `ActividadFormativa`.

### Estructura

```json
{
  "id": 1,
  "titulo": "Desarrollar aplicación Android",
  "descripcion": "Implementar las funcionalidades de la aplicación Mi Formación CTMA",
  "progreso": 50,
  "diasRestantes": 5,
  "prioridad": "MEDIA",
  "estado": "En proceso"
}
```

### Campos

| Campo           | Tipo          | Obligatorio | Descripción                                                 |
| --------------- | ------------- | ----------- | ----------------------------------------------------------- |
| `id`            | Long          | Sí          | Identificador único de la actividad                         |
| `titulo`        | String        | Sí          | Título de la actividad                                      |
| `descripcion`   | String / null | No          | Descripción de la actividad                                 |
| `progreso`      | Int           | Sí          | Porcentaje de progreso de 0 a 100                           |
| `diasRestantes` | Int           | Sí          | Cantidad de días restantes para completar la actividad      |
| `prioridad`     | Enum          | Sí          | Nivel de prioridad                                          |
| `estado`        | String        | Calculado   | Estado calculado a partir del progreso y los días restantes |

### Valores permitidos para `prioridad`

```text
BAJA
MEDIA
ALTA
```

---

# 3. Reglas de negocio

Las respuestas de la API deben respetar las reglas definidas actualmente en `ReglasActividad`.

## 3.1 Validación de actividad

Una actividad es válida cuando cumple todas las siguientes condiciones:

### Título

El título no puede estar vacío.

Si está vacío:

```text
El título es obligatorio.
```

### Progreso

El progreso debe encontrarse entre `0` y `100`, incluyendo ambos extremos.

Valores válidos:

```text
0 ≤ progreso ≤ 100
```

Valores como `-1` o `101` deben considerarse inválidos.

Mensaje de error:

```text
El progreso debe estar entre 0 y 100.
```

### Días restantes

Los días restantes no pueden ser negativos.

Valores válidos:

```text
diasRestantes >= 0
```

Si se recibe un valor negativo:

```text
Los días restantes no pueden ser negativos.
```

---

# 4. Cálculo del estado

El estado de una actividad se obtiene mediante las reglas de `estadoActividad()`.

| Condición                             | Estado       |
| ------------------------------------- | ------------ |
| `progreso == 100`                     | `Completada` |
| `progreso > 0` y `progreso < 100`     | `En proceso` |
| `progreso == 0` y `diasRestantes < 0` | `Vencida`    |
| Cualquier otro caso válido            | `Pendiente`  |

### Ejemplos

#### Actividad pendiente

```json
{
  "progreso": 0,
  "diasRestantes": 10,
  "estado": "Pendiente"
}
```

#### Actividad en proceso

```json
{
  "progreso": 50,
  "diasRestantes": 5,
  "estado": "En proceso"
}
```

#### Actividad completada

```json
{
  "progreso": 100,
  "diasRestantes": 0,
  "estado": "Completada"
}
```

### Consideración sobre el estado `Vencida`

Actualmente `validarActividad()` rechaza cualquier actividad cuyo `diasRestantes` sea menor que `0`.

Por lo tanto, una actividad con:

```text
progreso == 0
diasRestantes < 0
```

sería inválida antes de que pudiera llegar al cálculo de `estadoActividad()`.

Esto representa una condición identificada durante las pruebas y documentada como parte del riesgo `R-04`.

---

# 5. Actividades urgentes

Una actividad se considera urgente cuando cumple simultáneamente:

```text
progreso < 100
```

y:

```text
diasRestantes <= 3
```

Por lo tanto:

```json
{
  "progreso": 50,
  "diasRestantes": 3
}
```

se considera urgente.

Una actividad con:

```json
{
  "progreso": 100,
  "diasRestantes": 2
}
```

no se considera urgente porque ya está completada.

---

# 6. Promedio de progreso

El promedio se calcula utilizando el progreso de todas las actividades.

Ejemplo:

```text
Actividad 1 → 100
Actividad 2 → 60
Actividad 3 → 0
```

Resultado:

```text
53.3%
```

Si no existen actividades:

```text
0.0%
```

---

# 7. Búsqueda por título

La búsqueda debe:

* Eliminar espacios al inicio y al final.
* Permitir coincidencias parciales.
* Ignorar mayúsculas y minúsculas.

Por ejemplo, para una actividad:

```text
Proyecto de grado
```

una búsqueda por:

```text
proyecto
```

debe encontrarla.

---

# 8. Endpoints

## 8.1 Autenticación

### POST `/auth/login`

Permite iniciar sesión.

#### Request

```json
{
  "email": "usuario@example.com",
  "password": "123456"
}
```

#### Respuesta exitosa

**HTTP 200**

```json
{
  "token": "mock-token-123",
  "usuario": {
    "id": 1,
    "nombre": "Usuario de prueba",
    "rol": "APRENDIZ"
  }
}
```

#### Respuesta con credenciales inválidas

**HTTP 401**

```json
{
  "error": "Credenciales inválidas"
}
```

---

## 8.2 Obtener usuario autenticado

### GET `/auth/me`

Permite consultar la información del usuario autenticado.

### Header requerido

```http
Authorization: Bearer mock-token-123
```

#### Respuesta exitosa

**HTTP 200**

```json
{
  "id": 1,
  "nombre": "Usuario de prueba",
  "rol": "APRENDIZ"
}
```

#### Sin autenticación

**HTTP 401**

```json
{
  "error": "No autorizado"
}
```

---

# 9. Operaciones sobre actividades

## 9.1 Obtener todas las actividades

### GET `/actividades`

Devuelve la lista de actividades.

#### Respuesta exitosa

**HTTP 200**

```json
[
  {
    "id": 1,
    "titulo": "Desarrollar aplicación Android",
    "descripcion": "Implementar funcionalidades",
    "progreso": 50,
    "diasRestantes": 5,
    "prioridad": "MEDIA",
    "estado": "En proceso"
  },
  {
    "id": 2,
    "titulo": "Documentación",
    "descripcion": "Completar documentación",
    "progreso": 100,
    "diasRestantes": 1,
    "prioridad": "ALTA",
    "estado": "Completada"
  }
]
```

---

## 9.2 Obtener una actividad por ID

### GET `/actividades/{id}`

Devuelve una actividad específica.

### Ejemplo

```http
GET /actividades/1
```

#### Respuesta exitosa

**HTTP 200**

```json
{
  "id": 1,
  "titulo": "Desarrollar aplicación Android",
  "descripcion": "Implementar funcionalidades",
  "progreso": 50,
  "diasRestantes": 5,
  "prioridad": "MEDIA",
  "estado": "En proceso"
}
```

#### Actividad inexistente

**HTTP 404**

```json
{
  "error": "Actividad no encontrada"
}
```

---

# 10. Crear una actividad

### POST `/actividades`

Crea una nueva actividad.

## Request

```json
{
  "titulo": "Nueva actividad",
  "descripcion": "Descripción de la actividad",
  "progreso": 0,
  "diasRestantes": 10,
  "prioridad": "MEDIA"
}
```

## Respuesta exitosa

**HTTP 201**

```json
{
  "id": 3,
  "titulo": "Nueva actividad",
  "descripcion": "Descripción de la actividad",
  "progreso": 0,
  "diasRestantes": 10,
  "prioridad": "MEDIA",
  "estado": "Pendiente"
}
```

## Datos inválidos

Si la actividad incumple alguna regla de `validarActividad()`:

**HTTP 400**

```json
{
  "errores": [
    "El título es obligatorio.",
    "El progreso debe estar entre 0 y 100."
  ]
}
```

---

# 11. Actualizar progreso

### PATCH `/actividades/{id}/progreso`

Actualiza el progreso de una actividad.

## Request

```json
{
  "progreso": 75
}
```

## Respuesta exitosa

**HTTP 200**

```json
{
  "id": 1,
  "progreso": 75,
  "estado": "En proceso"
}
```

### Progreso igual a 100

Request:

```json
{
  "progreso": 100
}
```

Respuesta:

```json
{
  "id": 1,
  "progreso": 100,
  "estado": "Completada"
}
```

### Progreso inválido

Request:

```json
{
  "progreso": -1
}
```

Respuesta:

**HTTP 400**

```json
{
  "errores": [
    "El progreso debe estar entre 0 y 100."
  ]
}
```

---

# 12. Buscar actividades por título

### GET `/actividades?titulo={texto}`

Permite buscar actividades utilizando coincidencias parciales en el título.

### Ejemplo

```http
GET /actividades?titulo=proyecto
```

La búsqueda debe ignorar mayúsculas y minúsculas y permitir coincidencias parciales.

### Respuesta exitosa

**HTTP 200**

```json
[
  {
    "id": 1,
    "titulo": "Proyecto de grado",
    "descripcion": "Desarrollo del proyecto",
    "progreso": 50,
    "diasRestantes": 5,
    "prioridad": "ALTA",
    "estado": "En proceso"
  }
]
```

Si no existen coincidencias:

**HTTP 200**

```json
[]
```

---

# 13. Consultar actividades urgentes

### GET `/actividades/urgentes`

Devuelve las actividades que cumplen:

```text
progreso < 100
diasRestantes <= 3
```

### Respuesta

**HTTP 200**

```json
[
  {
    "id": 4,
    "titulo": "Entrega de documentación",
    "descripcion": "Finalizar documentación",
    "progreso": 60,
    "diasRestantes": 2,
    "prioridad": "ALTA",
    "estado": "En proceso"
  }
]
```

---

# 14. Consultar promedio de progreso

### GET `/actividades/resumen`

Devuelve información resumida sobre el progreso de las actividades.

### Respuesta

**HTTP 200**

```json
{
  "promedioProgreso": 53.3
}
```

Cuando no existen actividades:

```json
{
  "promedioProgreso": 0.0
}
```

---

# 15. Códigos HTTP

| Código             | Uso                                                   |
| ------------------ | ----------------------------------------------------- |
| `200 OK`           | Solicitud procesada correctamente                     |
| `201 Created`      | Actividad creada correctamente                        |
| `400 Bad Request`  | Datos inválidos o incumplimiento de reglas de negocio |
| `401 Unauthorized` | Falta autenticación o las credenciales son inválidas  |
| `404 Not Found`    | Recurso inexistente                                   |

---

# 16. Relación con los seis casos seleccionados

Los seis casos seleccionados para la Activación se relacionan con el contrato de la siguiente manera:

| Caso     | Clasificación         | Endpoint                           | Regla / comportamiento                                                |
|----------| --------------------- | ---------------------------------- | --------------------------------------------------------------------- |
| S4-TC-01 | Positivo              | `GET /actividades`                 | Se obtiene correctamente la lista de actividades                      |
| S4-TC-02    | Positivo              | `GET /actividades/{id}`            | Se obtiene correctamente una actividad existente                      |
| S4-TC-03    | Negativo              | `GET /actividades/{id}`            | Un ID inexistente devuelve `404`                                      |
| S4-TC-04    | Negativo              | `POST /actividades`                | Datos inválidos son rechazados con `400`                              |
| S4-TC-05    | Autorización          | `GET /actividades`                 | Una solicitud sin autenticación debe ser rechazada                    |
| S4-TC-06    | Transición de estados | `PATCH /actividades/{id}/progreso` | El cambio de progreso produce la transición correspondiente de estado |

---

# 17. Uso del contrato en Postman

Este contrato será utilizado por el responsable del **Laboratorio 2 — Postman** para construir un Mock Server.

La colección deberá utilizar como referencia:

```text
baseUrl
actividadId
token
```

Las solicitudes deben simular el comportamiento definido en este documento y no requieren un backend real de Mi Formación CTMA.

Cada request deberá indicar explícitamente que se trata de una operación simulada mediante Mock Server.

---

# 18. Relación entre documentación y pruebas

El contrato permite mantener la siguiente trazabilidad:

```text
ReglasActividad
      ↓
Reglas de negocio
      ↓
Contrato hipotético de API
      ↓
Casos de prueba
      ↓
Mock Server de Postman
      ↓
Evidencias
```

De esta manera, las pruebas de integración se basan en las mismas reglas de negocio utilizadas actualmente por la aplicación.
