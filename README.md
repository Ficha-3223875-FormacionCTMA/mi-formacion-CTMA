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

### Identificacion de dependencias, supuestos y preguntas abiertas

## Dependencias y elementos externos

Son elementos externos o componentes de los que depende el funcionamiento del proyecto.

* **Android Studio:** para el desarrollo y ejecución de la aplicación.
* **Kotlin y Android SDK:** para la construcción de la aplicación Android.
* **Base de datos:** para almacenar usuarios, actividades, recursos, estados y criterios de evaluación.
* **Conexión a Internet:** para acceder a recursos externos y sincronizar información, dependiendo de la arquitectura definida.
* **Servicio de autenticación:** para diferenciar los permisos de aprendices e instructores, si se implementa autenticación mediante un servicio externo.
* **Navegador o aplicación compatible:** para abrir los enlaces externos asociados a las actividades.
