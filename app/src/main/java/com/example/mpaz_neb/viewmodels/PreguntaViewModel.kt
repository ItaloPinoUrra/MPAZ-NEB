package com.example.mpaz_neb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpaz_neb.data.model.Pregunta
import com.example.mpaz_neb.repository.PreguntaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FormPreguntaState(
    val enunciado: String = "",
    val tipo: String = "ALTERNATIVA",
    val alternativas: String = "",
    val respuestaCorrecta: String = "",
    val retroalimentacion: String = "",
    val orden: Int = 1,
    val cargando: Boolean = false,
    val error: String? = null,
    val creada: Boolean = false
)

open class PreguntaViewModel : ViewModel() {

    private val repository = PreguntaRepository()

    protected val _preguntas = MutableStateFlow<List<Pregunta>>(emptyList())
    val preguntas: StateFlow<List<Pregunta>> = _preguntas

    protected val _actividadId = MutableStateFlow<Long?>(null)
    val actividadId: StateFlow<Long?> = _actividadId

    protected val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _form = MutableStateFlow(FormPreguntaState())
    val form: StateFlow<FormPreguntaState> = _form

    open fun cargarPreguntas(actividadId: Long) {
        _actividadId.value = actividadId
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                _preguntas.value = repository.getPreguntas(actividadId)
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al cargar preguntas"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun onEnunciadoChange(v: String) { _form.value = _form.value.copy(enunciado = v, error = null) }
    fun onTipoChange(v: String) { _form.value = _form.value.copy(tipo = v) }
    fun onAlternativasChange(v: String) { _form.value = _form.value.copy(alternativas = v) }
    fun onRespuestaChange(v: String) { _form.value = _form.value.copy(respuestaCorrecta = v) }
    fun onRetroChange(v: String) { _form.value = _form.value.copy(retroalimentacion = v) }
    fun onOrdenChange(v: Int) { _form.value = _form.value.copy(orden = v) }

    fun resetForm() {
        _form.value = FormPreguntaState(orden = _preguntas.value.size + 1)
    }

    fun crearPregunta() {
        val f = _form.value
        val actId = _actividadId.value ?: return

        if (f.enunciado.isBlank() || f.respuestaCorrecta.isBlank()) {
            _form.value = f.copy(error = "Enunciado y respuesta correcta son obligatorios")
            return
        }

        viewModelScope.launch {
            try {
                _form.value = f.copy(cargando = true, error = null)
                repository.crearPregunta(actId, Pregunta(
                    actividadId = actId,
                    enunciado = f.enunciado,
                    tipo = f.tipo,
                    alternativas = f.alternativas.ifBlank { null },
                    respuestaCorrecta = f.respuestaCorrecta,
                    retroalimentacion = f.retroalimentacion.ifBlank { null },
                    orden = f.orden
                ))
                _form.value = _form.value.copy(cargando = false, creada = true)
                cargarPreguntas(actId)
            } catch (e: Exception) {
                _form.value = _form.value.copy(cargando = false, error = e.localizedMessage)
            }
        }
    }

    fun eliminarPregunta(id: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarPregunta(id)
                _actividadId.value?.let { cargarPreguntas(it) }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }
}