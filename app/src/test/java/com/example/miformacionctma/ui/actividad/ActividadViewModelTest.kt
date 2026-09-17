package com.example.miformacionctma.ui.actividad

import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.PreferenciasRepository
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ActividadRepository
    private lateinit var preferencias: PreferenciasRepository
    private lateinit var viewModel: ActividadViewModel

    // Factory sintético para fixtures (Requerimiento Laboratorio 2)
    private fun actividadFixture(overrides: Map<String, Any> = emptyMap()): ActividadFormativa {
        return ActividadFormativa(
            id = overrides["id"] as? Long ?: 1L,
            titulo = overrides["titulo"] as? String ?: "Actividad Base",
            descripcion = overrides["descripcion"] as? String ?: "Descripción por defecto",
            progreso = overrides["progreso"] as? Int ?: 0,
            diasRestantes = overrides["diasRestantes"] as? Int ?: 5,
            prioridad = overrides["prioridad"] as? Prioridad ?: Prioridad.MEDIA,
            resuelto = overrides["resuelto"] as? Boolean ?: false
        )
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        preferencias = mockk(relaxed = true)
        
        every { repository.obtenerTodas() } returns flowOf(emptyList())
        
        viewModel = ActividadViewModel(repository, preferencias)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks() // Limpieza de mocks (Requerimiento Laboratorio 2)
    }

    /**
     * CASO 1: Test de éxito (Actualización válida)
     */
    @Test
    fun `actualizar llama al repositorio si la actividad es valida`() = runTest {
        val actividad = actividadFixture(mapOf("titulo" to "Título Válido"))
        
        // Mocking para obtener la antigua (misma para evitar conflicto de transición)
        coEvery { repository.obtenerPorId(actividad.id) } returns flowOf(actividad)
        coEvery { repository.actualizar(any()) } returns Unit

        viewModel.actualizar(actividad)
        advanceUntilIdle()

        // Verificación con MockK
        coVerify(exactly = 1) { repository.actualizar(any()) }
        assertTrue(viewModel.operacionState.value is OperacionUiState.Exitosa)
    }

    /**
     * CASO 2: Test de fallo 1 (Evidencia ausente / Título obligatorio)
     */
    @Test
    fun `actualizar falla si el titulo esta vacio`() = runTest {
        val actividadInvalida = actividadFixture(mapOf("titulo" to ""))
        
        coEvery { repository.obtenerPorId(actividadInvalida.id) } returns flowOf(actividadInvalida)

        viewModel.actualizar(actividadInvalida)
        advanceUntilIdle()

        // Verificamos que NO se llamó al repositorio
        coVerify(exactly = 0) { repository.actualizar(any()) }
        
        val state = viewModel.operacionState.value
        assertTrue(state is OperacionUiState.Fallida)
        assertEquals("El título es obligatorio.", (state as OperacionUiState.Fallida).mensaje)
    }

    /**
     * CASO 3: Test de fallo 2 (Transición inválida / Reducir progreso al 100)
     */
    @Test
    fun `actualizar falla si se intenta reducir progreso de una actividad completada`() = runTest {
        val actividadAntigua = actividadFixture(mapOf("progreso" to 100))
        val actividadNueva = actividadFixture(mapOf("progreso" to 50))
        
        coEvery { repository.obtenerPorId(actividadNueva.id) } returns flowOf(actividadAntigua)

        viewModel.actualizar(actividadNueva)
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.actualizar(any()) }
        
        val state = viewModel.operacionState.value
        assertTrue(state is OperacionUiState.Fallida)
        assertEquals(
            "No se puede reducir el progreso de una actividad completada.",
            (state as OperacionUiState.Fallida).mensaje
        )
    }
}
