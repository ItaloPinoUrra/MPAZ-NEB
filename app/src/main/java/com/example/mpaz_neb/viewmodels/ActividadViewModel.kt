package com.example.mpaz_neb.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_neb.data.model.Actividad
import com.example.mpaz_neb.data.remote.ArchivoUploader
import com.example.mpaz_neb.repository.ActividadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FormActividadState(
    val nombre: String = "",
    val modulo: String = "LECTURA",
    val curso: Int = 1,
    val seccion: String = "",
    val dificultad: Int = 1,
    val instrucciones: String = "",
    val estado: String = "DISPONIBLE",
    val archivoUri: Uri? = null,          // ← NUEVO
    val cargando: Boolean = false,
    val error: String? = null,
    val creada: Boolean = false
)

open class ActividadViewModel : ViewModel() {

    private val repository = ActividadRepository()

    protected val _actividades = MutableStateFlow<List<Actividad>>(emptyList())
    val actividades: StateFlow<List<Actividad>> = _actividades

    protected val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _form = MutableStateFlow(FormActividadState())
    val form: StateFlow<FormActividadState> = _form

    init {
        fetchActividades()
    }

    open fun fetchActividades() {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                _actividades.value = repository.getActividades()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al conectar con el servidor"
            } finally {
                _cargando.value = false
            }
        }
    }

    // ===== Formulario =====
    fun onNombreChange(v: String) { _form.value = _form.value.copy(nombre = v, error = null) }
    fun onModuloChange(v: String) { _form.value = _form.value.copy(modulo = v) }
    fun onCursoChange(v: Int) { _form.value = _form.value.copy(curso = v) }
    fun onSeccionChange(v: String) { _form.value = _form.value.copy(seccion = v.uppercase().take(2)) }
    fun onDificultadChange(v: Int) { _form.value = _form.value.copy(dificultad = v) }
    fun onInstruccionesChange(v: String) { _form.value = _form.value.copy(instrucciones = v) }
    fun onEstadoChange(v: String) { _form.value = _form.value.copy(estado = v) }
    fun onArchivoChange(uri: Uri?) { _form.value = _form.value.copy(archivoUri = uri) }  // ← NUEVO

    fun resetForm() {
        _form.value = FormActividadState()
    }

    fun crearActividad(context: Context) {
        val f = _form.value
        if (f.nombre.isBlank()) {
            _form.value = f.copy(error = "El nombre es obligatorio")
            return
        }

        viewModelScope.launch {
            try {
                _form.value = f.copy(cargando = true, error = null)
                val nueva = Actividad(
                    id = 0,
                    nombre = f.nombre,
                    modulo = f.modulo,
                    curso = f.curso,
                    seccion = f.seccion.ifBlank { null },
                    dificultad = f.dificultad,
                    instrucciones = f.instrucciones.ifBlank { null },
                    estado = f.estado
                )
                val creada = repository.crearActividad(nueva)

                // Subir archivo si hay uno seleccionado
                if (f.archivoUri != null) {
                    ArchivoUploader.subir(context, creada.id, f.archivoUri)
                }

                _form.value = _form.value.copy(cargando = false, creada = true)
                fetchActividades()
            } catch (e: Exception) {
                _form.value = _form.value.copy(
                    cargando = false,
                    error = "No se pudo crear: ${e.localizedMessage}"
                )
            }
        }
    }
}