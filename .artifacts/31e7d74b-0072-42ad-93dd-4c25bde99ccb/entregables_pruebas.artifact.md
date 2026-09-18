# Entregables: Guía de Aprendizaje de Pruebas de Software (Semana 8)

Este documento contiene la documentación técnica y las tablas requeridas para la Parte A de la guía de aprendizaje del proyecto **mi-formacion-CTMA**.

---

## 1. Tabla de Selección de Candidatos (Actividad práctica 1)

A continuación se presentan 10 casos de prueba evaluados para determinar su viabilidad de automatización.

| Caso | Riesgo | Frecuencia | ¿Determinista? | Nivel sugerido | ¿Automatizar? | Motivo | Referencia |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **CP-CTMA-01** | Alto | Alta | Sí | Unitario | **SÍ** | Lógica crítica de cálculo de promedios. | [ReglasActividad](file:///app/src/main/java/com/senactma/miformacionctma/ReglasActividad.kt) |
| **CP-CTMA-05** | Medio | Alta | Sí | Unitario | **SÍ** | Validación de límites de caracteres en títulos. | CA-03.1 |
| **CP-CTMA-09** | Alto | Media | Sí | Integración | **SÍ** | Persistencia en base de datos Room. | DAO Tests |
| **CP-CTMA-12** | Medio | Alta | Sí | Unitario | **SÍ** | Lógica de cálculo de urgencia por fecha. | Reglas Negocio |
| **CP-CTMA-13** | Alto | Alta | Sí | Unitario | **SÍ** | Cambio de estado según progreso (100%). | HU-CTMA-02 |
| **CP-CTMA-14** | Medio | Alta | Sí | Unitario | **SÍ** | Detección de actividades vencidas. | HU-CTMA-02 |
| **CP-CTMA-15** | Alto | Baja | Sí | Unitario | **SÍ (TDD)** | Corrección del BUG-CTMA-01 (Reducción progreso). | BUG-CTMA-01 |
| **CP-CTMA-16** | Bajo | Baja | No | E2E / UI | **NO (Manual)** | Verificación de colores y gradientes en la UI. | Diseño Visual |
| **CP-CTMA-18** | Medio | Media | Sí | Integración | **SÍ** | Sincronización con Preferencias de Usuario. | DataStore |
| **CP-CTMA-20** | Alto | Alta | Sí | Unitario | **SÍ** | Validación de rangos de entrada (0-100%). | CA-07 |

> [!IMPORTANT]
> El caso **CP-CTMA-16** se mantiene manual debido a que la percepción subjetiva de colores y la suavidad de las animaciones son costosas de automatizar y requieren revisión humana visual.

---

## 2. Matriz de Trazabilidad Actualizada

| ID Caso | Requisito / CA | Descripción del Caso | Resultado Manual | Test Automatizado (JUnit) | Estado TDD / Resolución |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **CP-CTMA-13** | HU-CTMA-02 | Progreso 100% devuelve "Completada". | Exitoso | `testEstadoActividad` | Resuelto |
| **CP-CTMA-14** | HU-CTMA-02 | Días < 0 devuelve "Vencida". | Exitoso | `testEstadoActividad` | Resuelto |
| **CA-03.1** | Req. Valid. | Título en límites (3, 80, 81 chars). | Exitoso | `validarActividad_...` | Resuelto |
| **BUG-CTMA-01** | CP-CTMA-15 | Impedir reducción si está al 100%. | Fallido | `validarProgreso_reducir...` | **Resuelto vía TDD** |
| **N/A** | Calculo | Promedio de promedios en lista. | Exitoso | `promedioProgreso_...` | Resuelto |

---

## 3. Demostración del Microciclo TDD (BUG-CTMA-01)

Para resolver el **BUG-CTMA-01**, se aplicó el ciclo Rojo-Verde-Refactor.

### Fase 1: RED (Rojo)
Se escribió el test antes de implementar la validación de protección. El test fallaba porque el código permitía cualquier valor entre 0 y 100.

```kotlin
@Test
fun validarProgreso_reducirProgresoEnActividadCompletada_devuelveFalso() {
    val resultado = ReglasActividad.validarProgreso(100, 90)
    assertFalse(resultado) // FALLABA: retornaba true
}
```

### Fase 2: GREEN (Verde)
Se modificó `ReglasActividad.kt` para incluir la lógica de protección:

```kotlin
fun validarProgreso(progresoAnterior: Int, progresoNuevo: Int): Boolean {
    if (progresoNuevo !in 0..100) return false
    // Solución al BUG-CTMA-01
    if (progresoAnterior == 100 && progresoNuevo < 100) return false
    return true
}
```
*Resultado: El test pasó a exitoso.*

### Fase 3: REFACTOR
Se limpió la función para que fuera más legible y se agruparon las validaciones en el objeto `ReglasActividad`, asegurando que no hubiera efectos secundarios en otros módulos.

---

## Código Implementado

### [Producción: ReglasActividad.kt](file:///app/src/main/java/com/senactma/miformacionctma/ReglasActividad.kt)
### [Pruebas: ReglasActividadTest.kt](file:///app/src/test/java/com/senactma/miformacionctma/ReglasActividadTest.kt)
