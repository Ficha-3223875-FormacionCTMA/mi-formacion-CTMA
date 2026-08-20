    # Planteamiento del problema

Actualmente, los aprendices administran sus actividades académicas, enlaces de acceso, evidencias y fechas de entrega utilizando diferentes canales y herramientas, como aplicaciones de mensajería, correos electrónicos y notas personales. Esta dispersión de la información ocasiona olvidos, pérdida de evidencias, duplicación de tareas y dificultades para realizar un seguimiento adecuado del proceso formativo. Asimismo, los instructores enfrentan limitaciones para comunicar actividades y criterios de evaluación de manera organizada, afectando la trazabilidad del aprendizaje. Desde el punto de vista del desarrollo, resulta necesario contar con una base técnica sólida que permita evolucionar la aplicación sin comprometer su estabilidad. Por ello, surge la necesidad de desarrollar **Mi Formación CTMA**, una aplicación Android que centralice la gestión académica y facilite la organización, la comunicación y el seguimiento del proceso formativo.

---

# Tipos de usuario y necesidades

| Tipo de usuario | Necesidad                                                                                                                                                                                                                 |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Aprendiz**    | Consultar actividades, fechas de entrega, enlaces y registrar el avance de sus evidencias desde un solo lugar, para organizar mejor su proceso de formación y reducir olvidos.                                            |
| **Instructor**  | Publicar actividades, compartir recursos, establecer criterios de evaluación y realizar seguimiento al progreso de los aprendices, garantizando una comunicación clara y una adecuada trazabilidad del proceso formativo. |

---

# Historias de usuario

### Historia de usuario 1

**Como** aprendiz, **quiero** consultar todas mis actividades, fechas de entrega y enlaces en una sola aplicación, **para** organizar mejor mi proceso de formación y evitar olvidos.

**Criterio de aceptación**

* **Dado** que el aprendiz ha iniciado sesión,
* **Cuando** acceda a la pantalla principal,
* **Entonces** deberá visualizar la lista de actividades con su fecha de entrega, descripción y enlace correspondiente.

---

### Historia de usuario 2

**Como** aprendiz, **quiero** registrar el avance de mis evidencias y tareas, **para** llevar un control de mi progreso y cumplir oportunamente con mis compromisos académicos.

**Criterio de aceptación**

* **Dado** que el aprendiz selecciona una actividad,
* **Cuando** marque su estado como **"En progreso"** o **"Completada"**,
* **Entonces** la aplicación deberá guardar el cambio y mostrar el estado actualizado.

---

### Historia de usuario 3

**Como** instructor, **quiero** publicar actividades, recursos y criterios de evaluación, **para** comunicar la información de manera organizada y realizar un seguimiento efectivo del proceso formativo de los aprendices.

**Criterio de aceptación**

* **Dado** que el instructor ha iniciado sesión,
* **Cuando** registre una nueva actividad con su descripción, fecha y criterios de evaluación,
* **Entonces** la actividad deberá almacenarse y estar disponible para los aprendices.

### Criterios de aceptacion por historia

### Criterios no funcional medible

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

# Taller 2 Plan de pruebas V1

## Resumen de responsabilidades por integrante

| Integrante      | Secciones   | Enfoque de su parte                                                                                                 |
|-----------------| ----------- | ------------------------------------------------------------------------------------------------------------------- |
| Miguel Angel O  | 1, 2 y 3    | Identificación, objetivo y alcance incluido: qué decisión deben soportar las pruebas y qué entra en esta iteración. |
| Juan Daniel P   | 4 y 5       | Fuera de alcance y base de prueba: qué queda excluido y sobre qué documentos se apoya el plan.                      |
| Juan Jose G     | 6 y 7       | Riesgos y enfoque: prioriza el catálogo de riesgos y define niveles y tipos de prueba por riesgo.                   |
| Wendi Daianna R | 8 y 9       | Ambiente y roles: qué se necesita para ejecutar y quién hace qué dentro del equipo.                                 |
| Juan David G    | 10, 11 y 12 | Criterios de entrada/suspensión/salida, entregables y cronograma: cuándo empezar, pausar y cerrar.                  |

# 1. Identificación

**Producto:** EntregaSegura.
**Documento:** Plan de pruebas v1 (borrador).
**Responsable de esta versión:** equipo de pruebas conformado por cinco integrantes.
**Fecha de elaboración:** 19 de agosto de 2026.

# 2. Objetivo

Las pruebas de esta iteración deben soportar la decisión de si el flujo de autenticación, autorización por rol y confirmación de entrega con evidencia fotográfica cumple los criterios de aceptación definidos para la historia **HU-ENT-01** y la regla de negocio de **no duplicidad**, antes de habilitar el paso a producción del incremento correspondiente.

# 3. Alcance incluido

Se valida la autenticación, la autorización por rol y la confirmación de entrega con evidencia, en los navegadores **Chrome** y **Edge** de escritorio y en un dispositivo **Android** representativo.

## 4. Fuera de alcance

Quedan excluidas de esta iteración la **facturación**, la **integración con operadores logísticos** y las **pruebas de carga masiva**, porque no forman parte del incremento actual. Estos componentes se abordarán cuando entren en desarrollo.


## 5. Base de prueba

La historia **HU-ENT-01** con sus criterios de aceptación redactados en formato **Given-When-Then**, la regla de negocio sobre evitar duplicidad de registro y el catálogo de riesgos identificado en el taller de planificación.


## 6. Riesgos

Se priorizan según la matriz construida en el taller de planificación. Las dos primeras filas concentran el esfuerzo inicial de prueba por su exposición muy alta.

| Riesgo | Prob. | Impacto | Exposición | Prioridad |
| :--- | :---: | :---: | :---: | :--- |
| Acceso a órdenes ajenas | 4 | 5 | 20 | Muy alta |
| Entrega sin evidencia | 4 | 5 | 20 | Muy alta |
| Registro duplicado por doble clic | 3 | 4 | 12 | Alta |
| Texto desalineado en escritorio | 2 | 1 | 2 | Baja |

---

## 7. Enfoque

Se combinan varios niveles según el riesgo asociado:

- **Pruebas unitarias:** Para la regla que impide guardar una entrega sin foto.
- **Pruebas de integración:** Para confirmar que la evidencia queda asociada a la orden correcta.
- **Pruebas de sistema o end-to-end:** Para el flujo completo de iniciar sesión, abrir la orden, adjuntar evidencia y confirmar.
- **Pruebas de aceptación:** Para validar la política de entrega con el responsable de negocio.
- **Pruebas no funcionales:** Para tiempo de respuesta y autorización por rol.
