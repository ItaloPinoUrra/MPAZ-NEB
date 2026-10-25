package com.example.mpaz_neb.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_neb.database.AppDatabase
import com.example.mpaz_neb.database.UsuarioEntity
import com.example.mpaz_neb.model.UsuarioErrores
import com.example.mpaz_neb.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    // Base de datos
    private val database = AppDatabase.getDatabase(application)
    private val usuarioDao = database.usuarioDao()

    // AQUÍ ESTÁ LA CORRECCIÓN: La lista va a nivel de clase
    val listaUsuarios = usuarioDao.obtenerTodos()

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun onImagenChange(uri: String?) {
        _estado.update { it.copy(imagenUri = uri) }
    }

    fun onRolChange(nuevoRol: String) {
        _estado.update { it.copy(rol = nuevoRol) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains(other = "@")) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    fun guardarUsuario(onSuccess: () -> Unit) {
        val estadoActual = _estado.value
        val nuevoUsuario = UsuarioEntity(
            nombre = estadoActual.nombre,
            correo = estadoActual.correo,
            clave = estadoActual.clave,
            rol = estadoActual.rol,
            imagenUri = estadoActual.imagenUri
        )

        viewModelScope.launch {
            usuarioDao.insertar(nuevoUsuario)
            onSuccess()
        }
    }
    fun actualizarImagenPerfil(usuario: UsuarioEntity, nuevaUri: String?) {
        val usuarioActualizado = usuario.copy(imagenUri = nuevaUri)
        viewModelScope.launch {
            usuarioDao.actualizar(usuarioActualizado)
        }
    }
}