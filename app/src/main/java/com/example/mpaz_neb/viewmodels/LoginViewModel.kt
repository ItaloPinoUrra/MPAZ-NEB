package com.example.mpaz_neb.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_neb.repository.AuthRepository
import com.example.mpaz_neb.sesion.SesionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class LoginUiState(
    val alias: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false,
    val rolLogueado: String? = null
)

open class LoginViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: AuthRepository = AuthRepository(),
    private val sesionManager: SesionManager = SesionManager(application)
) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun onAliasChange(valor: String) {
        _estado.value = _estado.value.copy(alias = valor, error = null)
    }

    fun onClaveChange(valor: String) {
        _estado.value = _estado.value.copy(clave = valor, error = null)
    }

    fun login() {
        val actual = _estado.value
        if (actual.alias.isBlank() || actual.clave.isBlank()) {
            _estado.value = actual.copy(error = "Alias y clave son obligatorios")
            return
        }

        viewModelScope.launch {
            try {
                _estado.value = actual.copy(cargando = true, error = null)
                val respuesta = repository.login(actual.alias, actual.clave)
                sesionManager.guardarSesion(
                    id = respuesta.id,
                    alias = respuesta.alias,
                    rol = respuesta.rol,
                    token = respuesta.token
                )
                _estado.value = _estado.value.copy(
                    cargando = false,
                    loginExitoso = true,
                    rolLogueado = respuesta.rol
                )
            } catch (e: Exception) {
                val mensajeError = if (e is HttpException && e.code() == 401) {
                    "Alias o contraseña incorrectos"
                } else if (e is HttpException && e.code() == 403) {
                    "La cuenta se encuentra inactiva"
                } else {
                    "No se pudo iniciar sesión: ${e.localizedMessage}"
                }
                _estado.value = _estado.value.copy(
                    cargando = false,
                    error = mensajeError
                )
            }
        }
    }
}