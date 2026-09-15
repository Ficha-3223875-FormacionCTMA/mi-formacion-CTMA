package com.example.miformacionctma.ui.actividad

import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.PreferenciasRepository
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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

    private val actividadPrueba = ActividadFormativa(
        id = 1,
        titulo = "Prueba Kotlin",
        descripcion = "Test unitario",
        progreso = 50,
        diasRestantes = 3,
        prioridad = Prioridad.ALTA
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        preferencias = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- 1. Estado Inicial y Carga ---

    @Test
    fun test01_estadoInicial_debeSerCargando() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        val viewModel = ActividadViewModel(repository, preferencias)

        assertEquals(ListadoUiState.Cargando, viewModel.uiState.value)
    }

    @Test
    fun test02_cargaConExito_emiteEstadoContenido() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(listOf(actividadPrueba))
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue("Se esperaba Contenido pero fue $estado", estado is ListadoUiState.Contenido)
        assertEquals(1, (estado as ListadoUiState.Contenido).actividades.size)
        job.cancel()
    }

    // --- 2. Filtros y Lista Vacía ---

    @Test
    fun test03_listaSinResultados_emiteEstadoVacio() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertEquals(ListadoUiState.Vacio, viewModel.uiState.value)
        job.cancel()
    }

    @Test
    fun test04_filtroUrgentes_activaFiltroYEmiteResultados() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(listOf(actividadPrueba))
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.cambiarFiltroUrgentes(true)
        advanceUntilIdle()

        assertTrue(viewModel.soloUrgentes.value)
        job.cancel()
    }

    @Test
    fun test05_filtroUrgentesDesactivado_mantieneEstadoCorrecto() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(listOf(actividadPrueba))
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.cambiarFiltroUrgentes(false)
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue("Se esperaba Contenido pero fue $estado", estado is ListadoUiState.Contenido)
        job.cancel()
    }

    // --- 3. Búsqueda y Cancelación ---

    @Test
    fun test06_busquedaRapida_cancelaConsultasAnteriores() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.buscarPorTexto(any()) } returns flowOf(listOf(actividadPrueba))
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.actualizarBusqueda("A")
        viewModel.actualizarBusqueda("An")
        viewModel.actualizarBusqueda("Android")
        advanceUntilIdle()

        assertEquals("Android", viewModel.textoBusqueda.value)
        coVerify(exactly = 1) { repository.buscarPorTexto("Android") }
        job.cancel()
    }

    @Test
    fun test07_busquedaVacia_llamaObtenerTodas() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(listOf(actividadPrueba))
        coEvery { repository.buscarPorTexto("Android") } returns flowOf(listOf(actividadPrueba))
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.actualizarBusqueda("Android")
        advanceUntilIdle()

        viewModel.actualizarBusqueda("")
        advanceUntilIdle()

        coVerify(atLeast = 1) { repository.obtenerTodas() }
        job.cancel()
    }

    @Test
    fun test08_busquedaSinCoincidencias_emiteEstadoVacio() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.buscarPorTexto("Inexistente") } returns flowOf(emptyList())
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.actualizarBusqueda("Inexistente")
        advanceUntilIdle()

        assertEquals(ListadoUiState.Vacio, viewModel.uiState.value)
        job.cancel()
    }

    // --- 4. Operaciones CRUD y OperacionUiState ---

    @Test
    fun test09_insertarActividad_emiteEstadoExitosa() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.insertar(any()) } returns 1L
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.operacionState.collect() }
        viewModel.insertar(actividadPrueba)
        advanceUntilIdle()

        assertEquals(OperacionUiState.Exitosa, viewModel.operacionState.value)
        job.cancel()
    }

    @Test
    fun test10_actualizarActividad_emiteEstadoExitosa() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.actualizar(any()) } returns Unit
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.operacionState.collect() }
        viewModel.actualizar(actividadPrueba)
        advanceUntilIdle()

        assertEquals(OperacionUiState.Exitosa, viewModel.operacionState.value)
        coVerify(exactly = 1) { repository.actualizar(actividadPrueba) }
        job.cancel()
    }

    @Test
    fun test11_eliminarActividad_emiteEstadoExitosa() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.eliminar(any()) } returns Unit
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.operacionState.collect() }
        viewModel.eliminar(actividadPrueba)
        advanceUntilIdle()

        assertEquals(OperacionUiState.Exitosa, viewModel.operacionState.value)
        coVerify(exactly = 1) { repository.eliminar(actividadPrueba) }
        job.cancel()
    }

    @Test
    fun test12_operacionFallida_emiteEstadoFallida() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.insertar(any()) } throws Exception("Error al insertar")
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.operacionState.collect() }
        viewModel.insertar(actividadPrueba)
        advanceUntilIdle()

        val estado = viewModel.operacionState.value
        assertTrue(estado is OperacionUiState.Fallida)
        assertEquals("Error al insertar", (estado as OperacionUiState.Fallida).mensaje)
        job.cancel()
    }

    // --- 5. Manejo de Errores y Limpieza ---

    @Test
    fun test13_errorEnRepositorio_emiteListadoUiStateError() = runTest {
        coEvery { repository.obtenerTodas() } returns flow { throw Exception("Fallo en base de datos") }
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()

        val estado = viewModel.uiState.value
        assertTrue(estado is ListadoUiState.Error)
        assertEquals("Fallo en base de datos", (estado as ListadoUiState.Error).mensaje)
        job.cancel()
    }

    @Test
    fun test14_resetearEstadoOperacion_regresaAEstadoInactivo() = runTest {
        coEvery { repository.obtenerTodas() } returns flowOf(emptyList())
        coEvery { repository.insertar(any()) } returns 1L
        val viewModel = ActividadViewModel(repository, preferencias)

        val job = backgroundScope.launch(testDispatcher) { viewModel.operacionState.collect() }
        viewModel.insertar(actividadPrueba)
        advanceUntilIdle()

        viewModel.resetearEstadoOperacion()
        advanceUntilIdle()

        assertEquals(OperacionUiState.Inactiva, viewModel.operacionState.value)
        job.cancel()
    }
}