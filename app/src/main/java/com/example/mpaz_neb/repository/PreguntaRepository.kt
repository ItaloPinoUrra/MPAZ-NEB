package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.Pregunta
import com.example.mpaz_neb.data.remote.MpazNebInstance

open class PreguntaRepository {

    open suspend fun getPreguntas(actividadId: Long): List<Pregunta> {
        return MpazNebInstance.api.getPreguntas(actividadId)
    }

    open suspend fun crearPregunta(actividadId: Long, pregunta: Pregunta): Pregunta {
        return MpazNebInstance.api.crearPregunta(actividadId, pregunta)
    }

    open suspend fun eliminarPregunta(id: Long) {
        MpazNebInstance.api.eliminarPregunta(id)
    }
}