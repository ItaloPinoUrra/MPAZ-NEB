package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.LoginRequest
import com.example.mpaz_neb.data.model.LoginResponse
import com.example.mpaz_neb.data.model.RegistroRequest
import com.example.mpaz_neb.data.model.RegistroResponse
import com.example.mpaz_neb.data.remote.MpazNebInstance

open class AuthRepository {

    open suspend fun login(alias: String, clave: String): LoginResponse {
        return MpazNebInstance.api.login(LoginRequest(alias, clave))
    }

    open suspend fun registrar(alias: String, clave: String, rol: String): RegistroResponse {
        return MpazNebInstance.api.registrar(RegistroRequest(alias, clave, rol))
    }
}