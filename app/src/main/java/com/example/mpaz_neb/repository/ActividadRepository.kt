package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.Actividad
import com.example.mpaz_neb.data.remote.MpazNebInstance

open class ActividadRepository {

    open suspend fun getActividades(): List<Actividad> {
        return MpazNebInstance.api.getActividades()
    }

    open suspend fun crearActividad(actividad: Actividad): Actividad {
        return MpazNebInstance.api.crearActividad(actividad)
    }

    open suspend fun eliminarActividad(id: Long) {
        MpazNebInstance.api.eliminarActividad(id)
    }
}