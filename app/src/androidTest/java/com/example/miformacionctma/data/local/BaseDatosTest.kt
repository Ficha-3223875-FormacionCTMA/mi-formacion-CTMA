package com.example.miformacionctma.data.local

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.miformacionctma.data.local.entity.ActividadFormativa
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BaseDatosTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: com.example.miformacionctma.data.local.dao.ActividadDao

    @Before
    fun createDb() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        // Uso de base de datos en memoria (Requerimiento Laboratorio 3)
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setDriver(AndroidSQLiteDriver())
            .allowMainThreadQueries()
            .build()
        dao = db.actividadDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * CASO 1: Insertar una ActividadFormativa válida y verificar su lectura.
     */
    @Test
    fun insertarYLeerActividad() = runBlocking {
        val actividad = ActividadFormativa(
            titulo = "Estudiar Kotlin",
            progreso = 50,
            diasRestantes = 10,
            prioridad = "ALTA"
        )
        val id = dao.insertar(actividad)
        val recuperada = dao.obtenerPorId(id).first()
        
        assertNotNull(recuperada)
        assertEquals("Estudiar Kotlin", recuperada?.titulo)
    }

    /**
     * CASO 2: Intentar guardar un registro con título vacío.
     * En Room, esto se persiste si el tipo es String (no nulo), 
     * el rechazo lógico debe ocurrir en capas superiores (ViewModel/Repository).
     */
    @Test
    fun guardarTituloVacioPersisteEnRoom() = runBlocking {
        val actividadVacia = ActividadFormativa(titulo = "", progreso = 0)
        val id = dao.insertar(actividadVacia)
        val recuperada = dao.obtenerPorId(id).first()
        
        assertNotNull(recuperada)
        assertEquals("", recuperada?.titulo)
    }

    /**
     * CASO 3: Modificar el progreso a 100 y corroborar la persistencia del nuevo estado.
     */
    @Test
    fun actualizarProgresoA100() = runBlocking {
        val actividad = ActividadFormativa(titulo = "Tarea", progreso = 10)
        val id = dao.insertar(actividad)
        
        val editada = actividad.copy(id = id, progreso = 100)
        dao.actualizar(editada)
        
        val recuperada = dao.obtenerPorId(id).first()
        assertEquals(100, recuperada?.progreso)
    }
}
