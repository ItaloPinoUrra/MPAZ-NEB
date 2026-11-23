package com.example.mpaz_neb.data.remote

import com.example.mpaz_neb.data.model.Actividad
import com.example.mpaz_neb.data.model.LoginRequest
import com.example.mpaz_neb.data.model.LoginResponse
import okhttp3.MultipartBody
import retrofit2.http.*
import com.example.mpaz_neb.data.model.Pregunta

import com.example.mpaz_neb.data.model.RegistroRequest
import com.example.mpaz_neb.data.model.RegistroResponse

interface MpazNebApi {

    // ===== AUTH =====
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/registro")
    suspend fun registrar(@Body request: RegistroRequest): RegistroResponse

    // ===== ACTIVIDADES =====
    @GET("api/actividades")
    suspend fun getActividades(): List<Actividad>

    @GET("api/actividades/{id}")
    suspend fun getActividad(@Path("id") id: Long): Actividad

    @POST("api/actividades")
    suspend fun crearActividad(@Body actividad: Actividad): Actividad

    @PUT("api/actividades/{id}")
    suspend fun actualizarActividad(
        @Path("id") id: Long,
        @Body actividad: Actividad
    ): Actividad

    @DELETE("api/actividades/{id}")
    suspend fun eliminarActividad(@Path("id") id: Long)

    // ===== ARCHIVOS =====
    @Multipart
    @POST("api/actividades/{id}/archivo")
    suspend fun subirArchivo(
        @Path("id") id: Long,
        @Part archivo: MultipartBody.Part
    ): Map<String, String>

    @GET("api/actividades/{id}/preguntas")
    suspend fun getPreguntas(@Path("id") actividadId: Long): List<Pregunta>

    @POST("api/actividades/{id}/preguntas")
    suspend fun crearPregunta(
        @Path("id") actividadId: Long,
        @Body pregunta: Pregunta
    ): Pregunta

    @PUT("api/preguntas/{id}")
    suspend fun actualizarPregunta(
        @Path("id") id: Long,
        @Body pregunta: Pregunta
    ): Pregunta

    @DELETE("api/preguntas/{id}")
    suspend fun eliminarPregunta(@Path("id") id: Long)
}