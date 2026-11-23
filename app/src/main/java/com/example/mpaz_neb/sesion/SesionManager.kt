package com.example.mpaz_neb.sesion

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mpaz_sesion")

class SesionManager(private val context: Context) {

    companion object {
        private val KEY_ID = longPreferencesKey("usuario_id")
        private val KEY_ALIAS = stringPreferencesKey("usuario_alias")
        private val KEY_ROL = stringPreferencesKey("usuario_rol")
        private val KEY_TOKEN = stringPreferencesKey("usuario_token")
    }

    data class SesionActual(
        val id: Long?,
        val alias: String?,
        val rol: String?,
        val token: String?
    )

    val sesion: Flow<SesionActual> = context.dataStore.data.map { prefs ->
        SesionActual(
            id = prefs[KEY_ID],
            alias = prefs[KEY_ALIAS],
            rol = prefs[KEY_ROL],
            token = prefs[KEY_TOKEN]
        )
    }

    suspend fun guardarSesion(id: Long, alias: String, rol: String, token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ID] = id
            prefs[KEY_ALIAS] = alias
            prefs[KEY_ROL] = rol
            prefs[KEY_TOKEN] = token
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { it.clear() }
    }
}