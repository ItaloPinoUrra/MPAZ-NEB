package com.example.mpaz_neb.data.model

data class RegistroRequest(
    val alias: String,
    val clave: String,
    val rol: String
)

data class RegistroResponse(
    val mensaje: String,
    val id: Long? = null,
    val alias: String? = null,
    val rol: String? = null
)
