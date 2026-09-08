<!-- docs/RIESGOS.md -->
# Riesgos — Mi Formación CTMA

| ID | Riesgo | Probabilidad | Impacto | Nivel | Tratamiento |
| ---- | --------------------------------- | -----------: | ------: | ----- | -------------------- |
| R-01 | Registrar avance no persiste tras cambiar el estado | Alta | Alta | Alto | Pruebas unitarias sobre `estadoActividad` + prueba manual de UI |
| R-02 | Una actividad publicada no aparece en el listado del aprendiz | Media | Alta | Alto | Prueba de integración lista–detalle |
| R-03 | Se guarda una actividad con título vacío o datos fuera de rango | Media | Alta | Alto | Validación en `validarActividad` + pruebas de límites |
| R-04 | El estado "Vencida" nunca se muestra pese a existir en el código | Alta | Media | Alto | Revisar el orden de evaluación en `estadoActividad` |
| R-05 | El progreso de una actividad completada puede reducirse sin control | Media | Media | Medio | Definir y validar una regla de negocio explícita |
| R-06 | La búsqueda por título entrega resultados inconsistentes | Baja | Baja | Bajo | Prueba unitaria sobre `buscarPorTitulo` |
| R-07 | Navegar a un id de actividad inexistente provoca un cierre inesperado | Media | Media | Medio | Manejo de estado recuperable en `DetalleRoute` |
| R-08 | El campo de título muestra "obligatorio" antes de que el usuario interactúe con él | Baja | Media | Medio | Parámetro `mostrarVacio` en `validarTitulo`, ya implementado |