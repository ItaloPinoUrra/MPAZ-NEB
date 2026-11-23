package com.example.mpaz_neb.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mpaz_neb.ui.screens.*
import com.example.mpaz_neb.viewmodels.*
import com.example.mpaz_neb.viewmodels.ActividadViewModel
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.mpaz_neb.viewmodels.PreguntaViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            val vm: LoginViewModel = viewModel()
            LoginScreen(navController, vm)
        }

        // Homes por rol (por ahora redirigen al mismo Inicio, luego los separamos)
        composable("home_admin") {
            val vm: LoginViewModel = viewModel()
            InicioScreen(navController)
        }
        composable("home_asistente") {
            InicioScreen(navController)
        }
        composable("home_estudiante") {
            InicioScreen(navController)
        }

        // Pantallas existentes
        composable("inicio") { InicioScreen(navController) }
        composable("modulos") { ModulosScreen(navController) }
        composable("perfil") { PerfilScreen(navController) }
        composable("registro") {
            val vm: RegistroViewModel = viewModel()
            RegistroScreen(navController, vm)
        }
        composable("resumen") { ResumenScreen() }

        composable("catalogo") {
            val vm: ActividadViewModel = viewModel()
            CatalogoScreen(navController, vm)
        }

        composable("lecturas") {
            val vm: BookViewModel = viewModel()
            LecturasSugeridasScreen(navController, vm)
        }
        composable(route = "formulario_actividad") {
            val vm: ActividadViewModel = viewModel()
            FormularioActividadScreen(navController, vm)
        }
        composable(
            route = "preguntas/{actividadId}",
            arguments = listOf(androidx.navigation.navArgument("actividadId") { type = androidx.navigation.NavType.LongType })
        ) { backStackEntry ->
            val actividadId = backStackEntry.arguments?.getLong("actividadId") ?: 0L
            val vm: PreguntaViewModel = viewModel()
            PreguntasScreen(navController, vm, actividadId)
        }
    }
}