    # Planteamiento del problema

Actualmente, los aprendices administran sus actividades académicas, enlaces de acceso, evidencias y fechas de entrega utilizando diferentes canales y herramientas, como aplicaciones de mensajería, correos electrónicos y notas personales. Esta dispersión de la información ocasiona olvidos, pérdida de evidencias, duplicación de tareas y dificultades para realizar un seguimiento adecuado del proceso formativo. Asimismo, los instructores enfrentan limitaciones para comunicar actividades y criterios de evaluación de manera organizada, afectando la trazabilidad del aprendizaje. Desde el punto de vista del desarrollo, resulta necesario contar con una base técnica sólida que permita evolucionar la aplicación sin comprometer su estabilidad. Por ello, surge la necesidad de desarrollar **Mi Formación CTMA**, una aplicación Android que centralice la gestión académica y facilite la organización, la comunicación y el seguimiento del proceso formativo.

---

# Tipos de usuario y necesidades

| Tipo de usuario | Necesidad                                                                                                                                                                                                                 |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Aprendiz**    | Consultar actividades, fechas de entrega, enlaces y registrar el avance de sus evidencias desde un solo lugar, para organizar mejor su proceso de formación y reducir olvidos.                                            |
| **Instructor**  | Publicar actividades, compartir recursos, establecer criterios de evaluación y realizar seguimiento al progreso de los aprendices, garantizando una comunicación clara y una adecuada trazabilidad del proceso formativo. |

---

### Criterios de aceptacion por historia
## Historias de usuario

### Historia de usuario 1 — Consultar actividades

HU-01 - Consultar actividades

Como aprendiz,
quiero consultar mis actividades registradas,
para conocer la información y los recursos asociados a cada una.

Criterios de aceptación

CA-01.1
Al acceder a la pantalla principal se muestran las actividades registradas.

CA-01.2
Cada actividad muestra como mínimo título, descripción y fecha de entrega.

CA-01.3
Si la actividad contiene un enlace, se puede acceder a él.

CA-01.4
Si se navega a un id de actividad que no existe, se muestra un estado recuperable en vez de un cierre inesperado.

Riesgos relacionados
- R-02
- R-08

Casos de prueba relacionados
- TC-01
- TC-02
- TC-03
- TC-21
- TC-22
---

### Historia de usuario 2 — Registrar avance

HU-02 - Registrar avance

Como aprendiz,
quiero actualizar el estado de mis actividades,
para llevar un seguimiento de mi progreso.

Criterios de aceptación

CA-02.1
Al cambiar el progreso de una actividad, la aplicación guarda y muestra el nuevo estado calculado.

CA-02.2
Al alcanzar progreso 100, la actividad se marca y se guarda como "Completada".

CA-02.3
Al volver a consultar una actividad modificada, se muestra el último estado guardado.

CA-02.4
Si el progreso es 0 y los días restantes son negativos, el estado calculado debe reflejar que la actividad está vencida.

Riesgos relacionados
- R-01
- R-04
- R-05

Casos de prueba relacionados
- TC-04
- TC-05
- TC-06
- TC-07
- TC-08
---

### Historia de usuario 3 — Publicar actividades

HU-03 - Publicar actividades

Como instructor,
quiero registrar y publicar actividades,
para que los aprendices puedan consultar la información y los recursos correspondientes.

Criterios de aceptación

CA-03.1
Una actividad solo se almacena si tiene título válido (mínimo 3, máximo 80 caracteres) y días restantes no negativos.

CA-03.2
Los recursos o criterios de evaluación agregados quedan asociados a la actividad.

CA-03.3
Una actividad publicada es visible cuando el aprendiz consulta sus actividades.

Riesgos relacionados
- R-02
- R-03

Casos de prueba relacionados
- TC-09
- TC-10
- TC-11
- TC-12
- TC-13
- TC-14
---

### Historia de usuario 4 — Iniciar Sesion

HU-04 - Iniciar sesión

Como aprendiz o instructor,
quiero iniciar sesión con mis credenciales,
para acceder a las actividades y funciones correspondientes a mi rol.

Criterios de aceptación

CA-04.1
Con credenciales válidas, el sistema da acceso a la pantalla principal según el rol del usuario.

CA-04.2
Con credenciales inválidas, el sistema muestra un mensaje de error y no permite el acceso.

Riesgos relacionados
- R-06

Casos de prueba relacionados
- TC-15
- TC-16
- TC-23
- TC-24
---
### Historia de usuario 5 — Buscar actividad por título

HU-05 - Buscar actividad por título

Como aprendiz,
quiero buscar una actividad escribiendo parte de su título,
para encontrarla rápidamente sin desplazarme por toda la lista.

Criterios de aceptación

CA-05.1
Al escribir un texto parcial, se muestran solo las actividades cuyo título lo contiene.

CA-05.2
Si ninguna actividad coincide con el texto buscado, se muestra un estado vacío de "sin resultados".

Riesgos relacionados
- R-07

Casos de prueba relacionados
- TC-17
- TC-18
---
### Historia de usuario 6 — Ver actividades urgentes

HU-06 - Ver actividades urgentes

Como aprendiz,
quiero identificar cuáles de mis actividades son urgentes,
para priorizar mi tiempo antes de que venzan.

Criterios de aceptación

CA-06.1
Una actividad con progreso menor a 100 y máximo 3 días restantes se marca como urgente.

CA-06.2
Una actividad completada no se marca como urgente sin importar los días restantes.

Riesgos relacionados
(ninguno registrado todavía en RIESGOS.md)

Casos de prueba relacionados
- TC-19
- TC-20
### Criterios no funcional medible

### Identificacion de dependencias, supuestos y preguntas abiertas

## Dependencias y elementos externos

Son elementos externos o componentes de los que depende el funcionamiento del proyecto.

* **Android Studio:** para el desarrollo y ejecución de la aplicación.
* **Kotlin y Android SDK:** para la construcción de la aplicación Android.
* **Base de datos:** para almacenar usuarios, actividades, recursos, estados y criterios de evaluación.
* **Conexión a Internet:** para acceder a recursos externos y sincronizar información, dependiendo de la arquitectura definida.
* **Servicio de autenticación:** para diferenciar los permisos de aprendices e instructores, si se implementa autenticación mediante un servicio externo.
* **Navegador o aplicación compatible:** para abrir los enlaces externos asociados a las actividades.
Se recomienda incluir uno que sea fácil de demostrar y medir durante el proyecto:

**Rendimiento:** El 95 % de las operaciones principales de consulta y actualización de actividades deberán mostrar una respuesta en un tiempo máximo de **2 segundos**, bajo condiciones normales de funcionamiento y una conexión de red estable.

Este criterio es adecuado para el README porque no se queda en algo ambiguo como "la aplicación debe ser rápida".

También podrían agregarse posteriormente otros criterios, como disponibilidad, seguridad o usabilidad, pero con uno medible ya se cumple el requisito.

### Identificacion de dependencias, supuestos y preguntas abiertas

## 3. Dependencias

Son elementos externos o componentes de los que depende el funcionamiento del proyecto.

* **Android Studio:** para el desarrollo, compilación y ejecución de la aplicación.
* **Kotlin y Android SDK:** para la construcción y funcionamiento de la aplicación Android.
* **Base de datos:** para almacenar información relacionada con usuarios, actividades, recursos, estados y criterios de evaluación.
* **Conexión a Internet:** necesaria para acceder a recursos externos y sincronizar información, dependiendo de la arquitectura definida.
* **Servicio de autenticación:** utilizado para diferenciar los permisos de aprendices e instructores, en caso de implementar autenticación mediante un servicio externo.
* **Navegador o aplicación compatible:** necesario para abrir los enlaces externos asociados a las actividades.

## 4. Supuestos

Los supuestos son condiciones que se consideran ciertas para poder desarrollar el proyecto.

* Se asume que los usuarios tendrán un dispositivo Android compatible con la versión mínima definida para la aplicación.
* Se asume que cada usuario tendrá un tipo de rol definido: **aprendiz** o **instructor**.
* Se asume que los instructores serán responsables de registrar información correcta sobre las actividades, fechas y criterios de evaluación.
* Se asume que los aprendices tendrán acceso a las actividades correspondientes a su proceso formativo.
* Se asume que el usuario tendrá conexión a Internet para las funcionalidades que requieran sincronización con el servidor.
* Se asume que los enlaces y recursos publicados por los instructores serán accesibles y válidos.

## 5. Preguntas abiertas

Estas son decisiones que todavía deberían definirse durante el desarrollo del proyecto.

1. ¿Qué versión mínima de Android será compatible con la aplicación?
2. ¿La aplicación funcionará parcialmente sin conexión a Internet?
3. ¿Qué tecnología se utilizará para el backend y la base de datos?
4. ¿Cómo se realizará el inicio de sesión y la autenticación de los usuarios?
5. ¿Cómo se asignarán los aprendices a sus respectivos instructores o grupos de formación?
6. ¿Los aprendices podrán adjuntar archivos o evidencias directamente desde la aplicación?
7. ¿Se implementarán notificaciones para recordar fechas próximas de entrega?
8. ¿Los instructores podrán modificar o eliminar actividades después de publicarlas?
9. ¿Qué formatos y tamaño máximo tendrán las evidencias que puedan subir los aprendices?

# Taller 2 — Plan de pruebas v1 (Mi Formación CTMA)

## Resumen de responsabilidades por integrante

| Integrante     | Secciones | Enfoque de su parte |
|----------------|---|---|
| Miguel Angel O | 1, 2 y 3 | Identificación, objetivo y alcance incluido |
| Juan Daniel P  | 4 y 5 | Fuera de alcance y base de prueba |
| Juan Jose G    | 6, 7, 8 y 9 | Riesgos, enfoque, ambiente/datos y roles |
| Juan Goez      | 10, 11 y 12 | Criterios de entrada/salida, entregables y cronograma |

### 1. Identificación

**Producto:** Mi Formación CTMA. **Documento:** Plan de pruebas v1 (borrador). **Responsable de esta versión:** equipo de pruebas (4 integrantes). **Fecha de elaboración:** 19 de agosto de 2026.

### 2. Objetivo

Las pruebas de esta iteración deben soportar la decisión de si el flujo de consulta de actividades (HU-CTMA-01), registro de avance (HU-CTMA-02) y publicación de actividades por el instructor (HU-CTMA-03) cumple los criterios de aceptación definidos en el README del proyecto, incluyendo las reglas de validación codificadas en `ReglasActividad.kt`, antes de considerar estable este incremento de la app.

### 3. Alcance incluido

Se valida la consulta de actividades, el registro y actualización del estado de avance, la publicación de actividades por el instructor, y las reglas de negocio de `validarActividad`, `estadoActividad`, `actividadesUrgentes` y `promedioProgreso`, ejecutadas en el emulador de Android Studio y, si está disponible, en un dispositivo Android físico.

### 4. Fuera de alcance

Quedan excluidas la autenticación real contra un servicio externo, la sincronización con un backend real, las notificaciones de fechas próximas y la carga de archivos como evidencia — todas siguen siendo preguntas abiertas sin resolver en el README, por lo que no pueden probarse todavía.

### 5. Base de prueba

Las tres historias de usuario del README (Consultar actividades, Registrar avance, Publicar actividades) con sus criterios Given-When-Then, el código de `ReglasActividad.kt`, y el criterio no funcional medible del README (95% de las operaciones de consulta/actualización responden en máximo 2 segundos).

### 6. Riesgos

| Riesgo | Prob. | Impacto | Exposición | Prioridad |
|---|---|---|---|---|
| El estado de avance no persiste tras cambiarlo | 4 | 5 | 20 | Muy alta |
| Una actividad publicada por el instructor no aparece para el aprendiz | 3 | 5 | 15 | Alta |
| Se guarda una actividad con título vacío o progreso fuera de rango | 3 | 4 | 12 | Alta |
| Cálculo incorrecto de actividades urgentes (`progreso < 100` y `diasRestantes <= 3`) | 2 | 3 | 6 | Media |
| El enlace asociado a la actividad no abre correctamente | 2 | 2 | 4 | Baja |

### 7. Enfoque

Pruebas unitarias (JUnit) sobre las funciones puras de `ReglasActividad` sin necesidad de UI; pruebas de integración para confirmar que `TarjetaActividad` refleja el estado calculado; pruebas de sistema/UI en Compose para el flujo completo de consultar y actualizar una actividad; pruebas de aceptación con el instructor sobre el flujo de publicación; pruebas no funcionales sobre el tiempo de respuesta de 2 segundos.

### 8. Ambiente y datos

Android Studio con emulador (o dispositivo Android físico), conexión a internet estable, acceso al código y al README del proyecto. Datos de prueba: actividades ficticias que cubran título vacío, progreso en 0/50/100, y días restantes negativos/positivos; cuentas simuladas de rol aprendiz e instructor, ya que la autenticación real aún no está definida.

### 9. Roles

El equipo (4 integrantes) se distribuye el diseño y la redacción de este plan por secciones según la tabla de cierre. Cada integrante ejecuta los casos derivados de su sección y participa en la revisión cruzada antes de la entrega final.

### 10. Criterios de entrada, suspensión, reanudación y salida

| Categoría | Ejemplo |
|---|---|
| Entrada | Historias y criterios revisados; proyecto compila sin errores; emulador configurado; versión identificada. |
| Suspensión | La app no compila; el emulador falla repetidamente; datos de prueba corruptos; más del 30% de casos bloqueados por la misma causa. |
| Reanudación | Corrección aplicada; build exitoso; smoke test aprobado. |
| Salida | 100% de casos críticos ejecutados; cero defectos críticos abiertos; riesgos residuales aceptados y comunicados. |

### 11. Entregables

Casos de prueba diseñados y ejecutados, evidencias de ejecución (capturas del emulador), registro de defectos encontrados, métricas de cobertura y avance, e informe breve de cierre para la revisión entre pares.

### 12. Cronograma

Dentro de los 90 minutos asignados: 20 minutos para consolidar la matriz de riesgos, 40 minutos para redactar las 12 secciones en paralelo, y 30 minutos para integrar y revisar antes de la revisión entre pares.

---
# Semana 3 — Diseño de casos de prueba y gestión de defectos (Mi Formación CTMA)

## 0. Activación

- **Criterios de partida:** HU-CTMA-03/CA1 (el instructor registra una actividad con título, descripción y fecha de entrega) y HU-CTMA-02/CA1-CA2 (el aprendiz cambia el estado a "En progreso" y luego a "Completada").
- **Riesgo asociado:** se guarda una actividad con título vacío o progreso fuera de rango — probabilidad 3, impacto 4, exposición 12, prioridad Alta.
- **Preguntas de diseño:**
    1. ¿Qué pasa si el instructor intenta guardar una actividad con el título vacío o solo con espacios?
    2. ¿Qué progreso mínimo y máximo son válidos, y qué ocurre justo en esos límites (0 y 100)?
    3. ¿El estado "Completada" impide que el progreso se reduzca después, o el sistema lo permite sin advertencia?
    4. ¿Qué ocurre si `diasRestantes` es negativo en una actividad que ya tiene progreso 100?
- **Escenario que debería aprobarse:** título "Entrega final", progreso 50, `diasRestantes` 3 → se guarda y el estado calculado es "En proceso".
- **Escenario que debería rechazarse:** título vacío → `validarActividad` devuelve el error correspondiente y la actividad no se guarda.

## 1. Laboratorio 1 — 12 casos de prueba

### Responsable: Miguel Angel O — HU-CTMA-03, validación de creación de actividad (partición y valores límite sobre título y `diasRestantes`)

| ID | Referencia | Técnica | Tipo | Datos | Resultado esperado | Prioridad |
|---|---|---|---|---|---|---|
| CP-CTMA-01 | HU-CTMA-03/CA1 | Partición de equivalencia | Positiva | Título "Entrega final", diasRestantes=5 | Se guarda sin errores | Alta |
| CP-CTMA-02 | HU-CTMA-03/CA1 | Partición de equivalencia | Negativa | Título "" (vacío) | Error: "El título es obligatorio." | Alta |
| CP-CTMA-03 | HU-CTMA-03/CA1 | Valores límite | Negativa | diasRestantes = -1 | Error: "Los días restantes no pueden ser negativos." | Alta |
| CP-CTMA-04 | HU-CTMA-03/CA1 | Valores límite | Positiva | diasRestantes = 0 (límite mínimo exacto) | Se guarda sin errores | Alta |
| CP-CTMA-05 | HU-CTMA-03/CA1 | Partición de equivalencia | Positiva | Título válido, descripción = null | Se guarda correctamente (descripción es opcional) | Media |
| CP-CTMA-06 | HU-CTMA-03/CA1 | Partición de equivalencia | Negativa | Título "   " (solo espacios) | Error: "El título es obligatorio." (`isBlank()` lo detecta) | Alta |

--- 
# Semana 4: Estado, formularios y navegación

![diagrama quien posea el estado del formulario](diagrama1.png)

### Punto 1 — ¿Quién posee el estado?

FormularioRoute es el dueño: ahí viven titulo y descripcion como rememberSaveable, porque son datos de interfaz pequeños que deben sobrevivir a una rotación pero no a un cierre de la app. FormularioActividad es stateless — no guarda nada, solo recibe value y comunica intención hacia arriba mediante onTituloChange, onDescripcionChange y onGuardarClick. Esto es justo el "flujo unidireccional" del punto 3 de la guía: el estado baja, los eventos suben, y nunca al revés.

Esta separación es la razón por la que FormularioActividad se puede probar y reutilizar sin depender de dónde vive el estado — igual que TarjetaActividad en la Semana 3 no sabía nada sobre ReglasActividad, solo recibía la actividad ya resuelta.

### Punto 2 - Funcion validarTitulo y pruebas manuales 
![Vista de pruebas manuales de la funcion ValidarTitulo](PruebasManualesValidarTitulo.png)