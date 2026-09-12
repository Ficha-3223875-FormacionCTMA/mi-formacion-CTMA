package com.example.miformacionctma.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.local.entity.ActividadFormativa
//Establece conexion con la bases de datos
@Database(
    entities = [ActividadFormativa::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao
}