package com.example.mpaz_neb.navigation

sealed class Screen(val route: String) {
    data object Inicio : Screen(route = "inicio_page")
    data object Modulos : Screen(route = "modulos_page")
    data object Perfil : Screen(route = "perfil_page")
}