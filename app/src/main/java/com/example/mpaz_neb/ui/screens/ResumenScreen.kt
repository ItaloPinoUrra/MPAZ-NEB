package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpaz_neb.viewmodels.UsuarioViewModel

@Composable
fun ResumenScreen() {
    val viewModel: UsuarioViewModel = viewModel()
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resumen del Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        Text("Nombre: ${estado.nombre}", style = MaterialTheme.typography.bodyLarge)
        Text("Correo: ${estado.correo}", style = MaterialTheme.typography.bodyLarge)
        Text("Rol: ${estado.rol}", style = MaterialTheme.typography.bodyLarge)
    }
}