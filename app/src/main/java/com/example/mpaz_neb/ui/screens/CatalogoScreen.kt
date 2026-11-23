package com.example.mpaz_neb.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_neb.viewmodels.ActividadViewModel
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val actividades = viewModel.actividades.collectAsState().value
    val cargando = viewModel.cargando.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchActividades()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Actividades") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("formulario_actividad") }) {
                Icon(Icons.Filled.Add, contentDescription = "Crear actividad")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                cargando -> CircularProgressIndicator()

                error != null -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Error:", style = MaterialTheme.typography.titleMedium)
                    Text(text = error, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.fetchActividades() }) {
                        Text("Reintentar")
                    }
                }

                actividades.isEmpty() -> Text("No hay actividades creadas todavía.")

                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(actividades) { act ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = act.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Módulo: ${act.modulo}  ·  Curso: ${act.nombreCurso ?: "?"}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Dificultad: ${act.dificultad ?: "-"}  ·  Estado: ${act.estado}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (!act.instrucciones.isNullOrBlank()) {
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = act.instrucciones,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (act.archivoNombre != null) {
                                    Spacer(Modifier.height(12.dp))
                                    OutlinedButton(
                                        onClick = {
                                            val url = "http://10.0.2.2:8080/api/actividades/${act.id}/archivo"
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Filled.AttachFile, contentDescription = null)
                                        Spacer(Modifier.width(4.dp))
                                        Text("Ver material adjunto (${act.archivoNombre})")
                                    }

                                }
                                Spacer(Modifier.height(12.dp))
                                Button(
                                    onClick = { navController.navigate("preguntas/${act.id}") },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Gestionar preguntas")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}