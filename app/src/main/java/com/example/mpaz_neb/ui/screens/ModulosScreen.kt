package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_neb.ui.components.MpazBottomBar
import com.example.mpaz_neb.viewmodels.MainViewModel
import kotlinx.coroutines.launch // <-- NUEVO IMPORT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulosScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Módulos") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            MpazBottomBar(navController, rutaActual = "modulos")
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

            //LECTURA
            Button(
                onClick = {
                    // 3. Mostramos el mensaje cuando hacen clic
                    scope.launch {
                        snackbarHostState.showSnackbar("El Módulo de Lectura estará disponible próximamente.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Módulo de Lectura")
            }

            Spacer(modifier = Modifier.height(12.dp))

            //MATEMÁTICA
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("El Módulo de Matemática estará disponible próximamente.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Módulo de Matemática")
            }
        }
    }
}