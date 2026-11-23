package com.example.mpaz_neb.data.model

data class LoginResponse(
    val id: Long,
    val alias: String,
    val rol: String,
    val token: String
)