package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpaz_neb.viewmodels.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

    Column(Modifier.padding(all = 16.dp)) {
        Text(
            text = "Resumen del Registro",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Nombre: ${estado.nombre}")
        Text(text = "Correo: ${estado.correo}")
        Text(text = "Dirección: ${estado.direccion}")
        Text(text = "Contraseña: **********")
        Text(
            text = "Términos: " + if (estado.aceptaTerminos) "Aceptados" else "No aceptados"
        )
    }
}