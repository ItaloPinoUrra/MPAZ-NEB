package com.example.mpaz_neb.data.model

data class Pregunta(
    val id: Long = 0,
    val actividadId: Long? = null,
    val enunciado: String,
    val tipo: String = "ALTERNATIVA",
    val alternativas: String? = null,
    val respuestaCorrecta: String,
    val retroalimentacion: String? = null,
    val orden: Int = 1
)