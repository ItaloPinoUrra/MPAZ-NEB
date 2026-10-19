package com.example.mpaz_neb.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_neb.ui.screens.*
import com.example.mpaz_neb.viewmodels.MainViewModel
import com.example.mpaz_neb.viewmodels.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable(route = "inicio") {
            InicioScreen(mainViewModel, navController)
        }
        composable(route = "modulos") {
            ModulosScreen(mainViewModel, navController)
        }
        composable(route = "perfil") {
            PerfilScreen(mainViewModel, navController)
        }


        composable(route = "registro") {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable(route = "resumen") {
            ResumenScreen(usuarioViewModel)
        }
    }
}