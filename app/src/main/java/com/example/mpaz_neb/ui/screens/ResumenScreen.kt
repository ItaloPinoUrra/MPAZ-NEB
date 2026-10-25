package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mpaz_neb.viewmodels.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Centramos el contenido
    ) {
        Text(
            text = "Resumen del Registro",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // MOSTRAMOS LA IMAGEN GUARDADA
        if (estado.imagenUri != null) {
            AsyncImage(
                model = estado.imagenUri,
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(text = "Nombre: ${estado.nombre}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Correo: ${estado.correo}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Contraseña: **********", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "Rol: ${estado.rol}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Términos: " + if (estado.aceptaTerminos) "Aceptados" else "No aceptados",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}