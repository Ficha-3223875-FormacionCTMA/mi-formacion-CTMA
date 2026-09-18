package com.example.miformacionctma.ui.actividad

import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.data.repository.PreferenciasRepository
import com.example.miformacionctma.domain.ActividadFormativa
import com.example.miformacionctma.domain.Prioridad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ActividadRepository
    private lateinit var preferencias: PreferenciasRepository
    private lateinit var viewModel: ActividadViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(ActividadRepository::class.java)
        preferencias = mock(PreferenciasRepository::class.java)
        
        // Configuración por defecto
        `when`(repository.obtenerTodas()).thenReturn(flowOf(emptyList()))
        `when`(preferencias.filtroCategoria).thenReturn(flowOf("TODAS"))
        
        viewModel = ActividadViewModel(repository, preferencias)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al iniciar el estado debe ser Cargando y luego Vacio si no hay datos`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        // El initialValue es Cargando
        assertTrue(viewModel.uiState.value is ListadoUiState.Cargando)
        
        advanceUntilIdle()
        
        assertTrue(viewModel.uiState.value is ListadoUiState.Vacio)
        collectJob.cancel()
    }

    @Test
    fun `al recibir actividades el estado debe ser Contenido`() = runTest {
        val lista = listOf(
            ActividadFormativa(id = 1, titulo = "Test", prioridad = Prioridad.ALTA)
        )
        `when`(repository.obtenerTodas()).thenReturn(flowOf(lista))
        
        viewModel = ActividadViewModel(repository, preferencias)
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state is ListadoUiState.Contenido)
        assertEquals(1, (state as ListadoUiState.Contenido).actividades.size)
        collectJob.cancel()
    }

    @Test
    fun `la busqueda filtra los resultados`() = runTest {
        val lista = listOf(
            ActividadFormativa(id = 1, titulo = "Android", prioridad = Prioridad.ALTA)
        )
        `when`(repository.buscarPorTexto("And")).thenReturn(flowOf(lista))
        
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.actualizarBusqueda("And")
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state is ListadoUiState.Contenido)
        assertEquals("Android", (state as ListadoUiState.Contenido).actividades[0].titulo)
        collectJob.cancel()
    }
}
