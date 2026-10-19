package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_neb.viewmodels.MainViewModel

@Composable
fun PerfilScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Perfil de usuario",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Datos ficticios de prueba")
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.navigate("inicio") {
                popUpTo("inicio") { inclusive = true }
            }
        }) {
            Text("Volver al inicio")
        }
    }
}