# Plan de pruebas — Mi Formación CTMA

## 1. Objetivo

Definir los casos de prueba utilizados para verificar el funcionamiento de **Mi Formación CTMA**, considerando las historias de usuario, criterios de aceptación, reglas de negocio y escenarios de error identificados durante el desarrollo.

También se incluyen seis casos de prueba seleccionados para la práctica de integración con la API **DummyJSON**, con el objetivo de comprobar comportamientos observables mediante solicitudes HTTP.

---

## 2. Casos de prueba del sistema

Los siguientes casos corresponden a las historias de usuario y criterios de aceptación definidos para **Mi Formación CTMA**.

| ID    | HU/CA           | Técnica                   | Precondición                                  | Pasos                                    | Esperado                                                 | Ejecución | Resultado real | Evidencia | Defecto asociado |
| ----- | --------------- | ------------------------- | --------------------------------------------- | ---------------------------------------- | -------------------------------------------------------- | --------- | -------------- | --------- | ---------------- |
| TC-01 | HU-01 / CA-01.1 | Caso de uso               | Existen actividades registradas               | Abrir la pantalla principal              | Se muestra la lista de actividades                       | Pendiente | —              | —         | —                |
| TC-25 | HU-01 / CA-01.2 | Caso de uso               | Ancho de pantalla ≥ 600dp                     | Abrir el listado                         | Se muestran 2 columnas en vez de 1                       | Pendiente | —              | —         | —                |
| TC-02 | HU-02 / CA-02.1 | Caso de uso               | Actividad con datos completos                 | Consultar la actividad                   | Se ve título, descripción y fecha de entrega             | Pendiente | —              | —         | —                |
| TC-26 | HU-02 / CA-02.2 | Caso de uso               | Actividad con enlace asociado                 | Consultar la actividad y tocar el enlace | Se abre el enlace correspondiente                        | Pendiente | —              | —         | —                |
| TC-03 | HU-02 / CA-02.3 | Negativa                  | Actividad con id inexistente                  | Navegar al detalle con un id inválido    | Se muestra estado recuperable en vez de fallar           | Pendiente | —              | —         | —                |
| TC-04 | HU-03 / CA-03.1 | Valores límite            | progreso = -1                                 | Guardar el avance                        | Rechazado: "El progreso debe estar entre 0 y 100."       | Pendiente | —              | —         | —                |
| TC-06 | HU-03 / CA-03.1 | Partición de equivalencia | progreso = 50                                 | Guardar el avance                        | El estado se muestra como "En proceso"                   | Pendiente | —              | —         | —                |
| TC-05 | HU-03 / CA-03.2 | Valores límite            | progreso = 100 (límite máximo)                | Guardar el avance                        | El estado pasa a "Completada"                            | Pendiente | —              | —         | —                |
| TC-07 | HU-03 / CA-03.3 | Caso de uso               | Actividad con estado ya modificado            | Volver a consultarla                     | Se ve el último estado guardado                          | Pendiente | —              | —         | —                |
| TC-08 | HU-03 / CA-03.4 | Transición de estados     | progreso = 0 y diasRestantes = -2             | Consultar el estado calculado            | Debería mostrar "Vencida", pero nunca ocurre (ver R-04)  | Pendiente | —              | —         | R-04             |
| TC-09 | HU-04 / CA-04.1 | Partición de equivalencia | Título vacío                                  | El instructor intenta guardar            | Rechazado: "El título es obligatorio."                   | Pendiente | —              | —         | —                |
| TC-10 | HU-04 / CA-04.1 | Valores límite            | diasRestantes = -1                            | El instructor intenta guardar            | Rechazado: "Los días restantes no pueden ser negativos." | Pendiente | —              | —         | —                |
| TC-11 | HU-04 / CA-04.1 | Partición de equivalencia | Título y fecha válidos                        | El instructor guarda la actividad        | Se almacena correctamente                                | Pendiente | —              | —         | —                |
| TC-12 | HU-04 / CA-04.2 | Caso de uso               | Actividad publicada por el instructor         | El aprendiz consulta sus actividades     | Ve la actividad publicada                                | Pendiente | —              | —         | —                |
| TC-17 | HU-05 / CA-05.1 | Partición de equivalencia | Texto de búsqueda parcial                     | Buscar una actividad                     | Se muestran solo las coincidencias                       | Pendiente | —              | —         | —                |
| TC-18 | HU-05 / CA-05.2 | Negativa                  | Texto que no coincide con ningún título       | Buscar una actividad                     | Se muestra el estado vacío "Sin resultados"              | Pendiente | —              | —         | —                |
| TC-19 | HU-06 / CA-06.1 | Partición de equivalencia | progreso < 100 y diasRestantes ≤ 3            | Consultar el resumen                     | Aparece marcada como urgente                             | Pendiente | —              | —         | —                |
| TC-20 | HU-06 / CA-06.2 | Negativa                  | Actividad completada con pocos días restantes | Consultar el resumen                     | No aparece como urgente                                  | Pendiente | —              | —         | —                |
| TC-27 | HU-07 / CA-07.1 | Caso de uso               | Actividades con progreso 100, 60 y 0          | Consultar el resumen                     | Muestra el promedio correcto (ej. 53.3%)                 | Pendiente | —              | —         | —                |
| TC-28 | HU-07 / CA-07.2 | Valores límite            | Lista de actividades vacía                    | Consultar el resumen                     | Muestra 0.0% en vez de fallar                            | Pendiente | —              | —         | —                |
| TC-29 | HU-08 / CA-08.1 | Caso de uso               | Campo de título recién abierto, sin escribir  | Observar el campo                        | No muestra ningún error todavía                          | Pendiente | —              | —         | —                |
| TC-30 | HU-08 / CA-08.2 | Negativa                  | Campo vacío tras intentar guardar             | Observar el campo                        | Muestra "Escribe un título"                              | Pendiente | —              | —         | —                |
| TC-13 | HU-08 / CA-08.3 | Valores límite            | Título de 2 caracteres mientras se escribe    | Observar el campo                        | Muestra "Usa al menos 3 caracteres"                      | Pendiente | —              | —         | —                |
| TC-14 | HU-08 / CA-08.3 | Valores límite            | Título de 81 caracteres mientras se escribe   | Observar el campo                        | Muestra "Usa máximo 80 caracteres"                       | Pendiente | —              | —         | —                |

---

## 3. Práctica Semana 4 — Integración con DummyJSON

### 3.1 Mapeo actividad ↔ todo

Para la práctica de integración se utiliza la API genérica **DummyJSON** como fuente externa de datos.

El recurso `/todos` no representa exactamente el modelo de actividades de **Mi Formación CTMA**, por lo que se establece el siguiente mapeo:

| Mi Formación CTMA                                  | DummyJSON (`/todos`) |
| -------------------------------------------------- | -------------------- |
| `id` (Long)                                        | `id`                 |
| `titulo` (String)                                  | `todo`               |
| `progreso == 100` → estado "Completada"            | `completed: true`    |
| `progreso < 100` → estado "Pendiente"/"En proceso" | `completed: false`   |
| `descripcion`                                      | No existe            |
| `diasRestantes`                                    | No existe            |
| `prioridad`                                        | No existe            |

### Aclaración

DummyJSON es una API genérica y no conoce las reglas de negocio propias de **Mi Formación CTMA**, como `validarActividad` o `estadoActividad`.

Por esta razón, DummyJSON no necesariamente rechazará datos que serían inválidos según las reglas de negocio de la aplicación. Las validaciones relacionadas con el rango de `progreso`, títulos, días restantes u otras reglas propias permanecen en el código de **Mi Formación CTMA**.

Por lo tanto, los seis casos seleccionados para esta práctica representan comportamientos observables a nivel HTTP y no constituyen una copia literal de los casos de prueba definidos para la semana 3.

---

## 3.2 Los 6 casos seleccionados

| ID    | Clasificación         | Técnica   | Dato / solicitud                                                     | Esperado                                                                                  | Ejecución | Resultado real | Evidencia | Defecto asociado |
| ----- | --------------------- | --------- | -------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | --------- | -------------- | --------- | ---------------- |
| TC-21 | Positivo              | WEB + API | `GET /todos`                                                         | HTTP 200 y lista de todos en JSON                                                         | Pendiente | —              | —         | —                |
| TC-22 | Negativo              | WEB + API | `GET /todos/9999`                                                    | HTTP 404 y mensaje de error                                                               | Pendiente | —              | —         | —                |
| TC-23 | Negativo              | WEB + API | `POST /auth/login` con contraseña incorrecta                         | HTTP 400 y mensaje relacionado con las credenciales                                       | Pendiente | —              | —         | —                |
| TC-24 | Autorización          | WEB + API | `GET /auth/me` sin header `Authorization`                            | HTTP 401 y acceso denegado                                                                | Pendiente | —              | —         | —                |
| TC-31 | Positivo              | API       | `PUT /todos/1` con `{"completed": true}`                             | HTTP 200 y respuesta con `completed: true`                                                | Pendiente | —              | —         | —                |
| TC-32 | Transición de estados | API       | `PUT /todos/1` (`completed: false → true`) seguido de `GET /todos/1` | El PUT responde como si hubiera guardado, pero el GET posterior muestra el valor original | Pendiente | —              | —         | —                |

> **Nota:** Se utilizan los identificadores `TC-31` y `TC-32` para evitar duplicar los identificadores existentes del plan de pruebas del sistema.

### Consideración sobre TC-32

El caso TC-32 permite comprobar que la respuesta de una operación `PUT` no implica necesariamente que el cambio quede almacenado de manera permanente en DummyJSON.

Si después del `PUT` el `GET` devuelve el valor original, este comportamiento debe documentarse como una característica o limitación conocida de DummyJSON y **no como un defecto de Mi Formación CTMA**, salvo que la aplicación propia sea responsable de la persistencia y presente un comportamiento incorrecto.

---

## 4. Evidencias

Las evidencias de ejecución deben almacenarse en:

```text
docs/evidencias/
```

Se recomienda utilizar el ID del caso de prueba en el nombre del archivo para facilitar la trazabilidad.

Ejemplo:

```text
docs/evidencias/TC-21.png
docs/evidencias/TC-22.png
docs/evidencias/TC-23.png
docs/evidencias/TC-24.png
docs/evidencias/TC-31.png
docs/evidencias/TC-32.png
```

Las evidencias pueden corresponder a capturas de pantalla de:

* Navegador y respuesta HTTP.
* DevTools / Network.
* Postman.
* Request y response de la API.
* Código de estado HTTP.
* Mensajes de error o respuestas relevantes.

---

## 5. Colección de Postman

La colección utilizada para ejecutar las pruebas de API debe almacenarse en:

```text
docs/postman/
```

Ejemplo:

```text
docs/postman/MiFormacionCTMA.postman_collection.json
```

La colección debe contener las solicitudes necesarias para reproducir los casos de prueba API.

---

## 6. Relación con riesgos

Cuando un caso de prueba permita comprobar un riesgo previamente identificado, debe registrarse el identificador del riesgo correspondiente en la columna **Defecto asociado** o en la documentación complementaria.

Por ejemplo:

```text
TC-08 → R-04
```

Esto permite mantener la trazabilidad entre:

```text
Riesgo → Caso de prueba → Resultado → Evidencia
```

---

## 7. Estado de ejecución

Los casos se encuentran inicialmente en estado:

```text
Pendiente
```

Después de ejecutar cada prueba, la columna **Ejecución** debe actualizarse, por ejemplo, a:

```text
Ejecutado
```

y se deben completar las columnas:

* **Resultado real**
* **Evidencia**
* **Defecto asociado**, cuando corresponda.

El estado final de cada prueba debe reflejar lo observado durante su ejecución y no únicamente el resultado esperado.
