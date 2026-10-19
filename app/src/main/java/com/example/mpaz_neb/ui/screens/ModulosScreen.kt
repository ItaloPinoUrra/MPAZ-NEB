package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_neb.viewmodels.MainViewModel

@Composable
fun ModulosScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // ESTO ES EL NAVIGATION BAR (la barra de abajo)
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { }, // Ya estamos aquí
                    icon = { Icon(Icons.Filled.List, contentDescription = "Módulos") },
                    label = { Text("Módulos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("perfil")
                    },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Módulos disponibles",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text("Módulo de Lectura")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text("Módulo de Matemática")
            }
        }
    }
}