package com.example.mpaz_neb.data.model

data class Actividad(
    val id: Long = 0,
    val nombre: String,
    val modulo: String,
    val curso: Int? = null,
    val seccion: String? = null,
    val dificultad: Int? = null,
    val instrucciones: String? = null,
    val estado: String,
    val nombreCurso: String? = null,
    val archivoNombre: String? = null,
    val archivoRuta: String? = null
)