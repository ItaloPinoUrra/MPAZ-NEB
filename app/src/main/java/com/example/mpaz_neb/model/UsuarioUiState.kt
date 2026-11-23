package com.example.mpaz_neb.model

// Modelo principal que representa el estado del formulario del usuario
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val rol: String = "Alumno",
    val aceptaTerminos: Boolean = false,
    val imagenUri: String? = null,
    val errores: UsuarioErrores = UsuarioErrores()
)

