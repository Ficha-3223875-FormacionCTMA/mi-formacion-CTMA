package com.example.miformacionctma.data.remote.api

import com.example.miformacionctma.data.remote.dto.ActividadDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ActividadApiService {

    @GET("actividades")
    suspend fun obtenerActividades(): List<ActividadDto>

    @GET("actividades/{id}")
    suspend fun obtenerActividadPorId(@Path("id") id: Long): ActividadDto

    @POST("actividades")
    suspend fun crearActividad(@Body actividad: ActividadDto): ActividadDto

    @PATCH("actividades/{id}")
    suspend fun actualizarActividad(
        @Path("id") id: Long,
        @Body actividad: ActividadDto
    ): ActividadDto

    @DELETE("actividades/{id}")
    suspend fun eliminarActividad(@Path("id") id: Long)

    @Multipart
    @POST("actividades/evidencias")
    suspend fun subirEvidencia(
        @Part("actividad_id") actividadId: RequestBody,
        @Part imagen: MultipartBody.Part
    ): ActividadDto // O el DTO de respuesta que el backend devuelva
}
