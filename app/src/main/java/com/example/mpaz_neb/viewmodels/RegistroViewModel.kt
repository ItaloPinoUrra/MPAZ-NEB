package com.example.mpaz_neb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_neb.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegistroUiState(
    val alias: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val rol: String = "ESTUDIANTE",
    val cargando: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false,
    val mensajeExito: String? = null
)

open class RegistroViewModel @JvmOverloads constructor(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow(RegistroUiState())
    val estado: StateFlow<RegistroUiState> = _estado

    fun onAliasChange(valor: String) {
        _estado.value = _estado.value.copy(alias = valor, error = null)
    }

    fun onClaveChange(valor: String) {
        _estado.value = _estado.value.copy(clave = valor, error = null)
    }

    fun onConfirmarClaveChange(valor: String) {
        _estado.value = _estado.value.copy(confirmarClave = valor, error = null)
    }

    fun onRolChange(valor: String) {
        _estado.value = _estado.value.copy(rol = valor, error = null)
    }

    fun registrar() {
        val actual = _estado.value

        if (actual.alias.isBlank()) {
            _estado.value = actual.copy(error = "El alias es obligatorio")
            return
        }

        if (actual.clave.isBlank()) {
            _estado.value = actual.copy(error = "La contraseña es obligatoria")
            return
        }

        if (actual.clave.length < 6) {
            _estado.value = actual.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        if (actual.clave != actual.confirmarClave) {
            _estado.value = actual.copy(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            try {
                _estado.value = actual.copy(cargando = true, error = null)
                val respuesta = repository.registrar(
                    alias = actual.alias.trim(),
                    clave = actual.clave,
                    rol = actual.rol
                )
                _estado.value = _estado.value.copy(
                    cargando = false,
                    registroExitoso = true,
                    mensajeExito = respuesta.mensaje
                )
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Error al registrar usuario"
                _estado.value = _estado.value.copy(
                    cargando = false,
                    error = if (msg.contains("400")) "El alias ya está registrado o datos inválidos" else msg
                )
            }
        }
    }
}
