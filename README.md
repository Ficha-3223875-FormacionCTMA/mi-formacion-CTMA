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

**Como aprendiz**, quiero consultar mis actividades registradas para conocer la información y los recursos asociados a cada una.

**Criterios de aceptación:**

* **Dado que** el aprendiz ha iniciado sesión, **cuando** acceda a la pantalla principal, **entonces** deberá visualizar sus actividades registradas.
* **Dado que** existen actividades registradas, **cuando** el aprendiz consulte una actividad, **entonces** deberá visualizar como mínimo su título, descripción y fecha de entrega.
* **Dado que** una actividad contiene un enlace, **cuando** el aprendiz consulte dicha actividad, **entonces** deberá poder acceder al enlace correspondiente.

---

### Historia de usuario 2 — Registrar avance

**Como aprendiz**, quiero actualizar el estado de mis actividades para llevar un seguimiento de mi progreso.

**Criterios de aceptación:**

* **Dado que** el aprendiz ha seleccionado una actividad, **cuando** cambie su estado a **"En progreso"**, **entonces** la aplicación deberá guardar y mostrar el nuevo estado.
* **Dado que** una actividad se encuentra en progreso, **cuando** el aprendiz la marque como **"Completada"**, **entonces** la aplicación deberá actualizar y guardar el estado.
* **Dado que** el aprendiz vuelva a consultar una actividad cuyo estado fue modificado, **cuando** acceda a ella, **entonces** deberá visualizar el último estado guardado.

---

### Historia de usuario 3 — Publicar actividades

**Como instructor**, quiero registrar y publicar actividades para que los aprendices puedan consultar la información y los recursos correspondientes.

**Criterios de aceptación:**

* **Dado que** el instructor ha iniciado sesión, **cuando** registre una actividad con título, descripción y fecha de entrega, **entonces** la aplicación deberá almacenarla correctamente.
* **Dado que** el instructor ha registrado una actividad, **cuando** agregue recursos o criterios de evaluación, **entonces** estos deberán quedar asociados a la actividad.
* **Dado que** una actividad ha sido publicada, **cuando** un aprendiz consulte sus actividades, **entonces** deberá poder visualizar la información publicada por el instructor.

### Criterios no funcional medible

Se recomienda incluir uno que sea fácil de demostrar y medir durante el proyecto:

**Rendimiento:** El 95 % de las operaciones principales de consulta y actualización de actividades deberán mostrar una respuesta en un tiempo máximo de **2 segundos**, bajo condiciones normales de funcionamiento y una conexión de red estable.

Este criterio es adecuado para el README porque no se queda en algo ambiguo como "la aplicación debe ser rápida".

También podrían agregarse posteriormente otros criterios, como disponibilidad, seguridad o usabilidad, pero con uno medible ya se cumple el requisito.

### Identificacion de dependencias, supuestos y preguntas abiertas

## 3. Dependencia