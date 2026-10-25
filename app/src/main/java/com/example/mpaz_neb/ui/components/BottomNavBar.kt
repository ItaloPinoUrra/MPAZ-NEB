package com.example.mpaz_neb.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MpazBottomBar(navController: NavController, rutaActual: String) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == "inicio",
            onClick = {
                if (rutaActual != "inicio") {
                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                    }
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == "modulos",
            onClick = {
                if (rutaActual != "modulos") {
                    navController.navigate("modulos") {
                        popUpTo("inicio")
                    }
                }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Módulos") },
            label = { Text("Módulos") }
        )
        NavigationBarItem(
            selected = rutaActual == "perfil",
            onClick = {
                if (rutaActual != "perfil") {
                    navController.navigate("perfil") {
                        popUpTo("inicio")
                    }
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}