package com.example.mpaz_neb.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_neb.viewmodels.PreguntaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreguntasScreen(
    navController: NavController,
    viewModel: PreguntaViewModel,
    actividadId: Long
) {
    val preguntas = viewModel.preguntas.collectAsState().value
    val cargando = viewModel.cargando.collectAsState().value
    val error = viewModel.error.collectAsState().value
    val form by viewModel.form.collectAsState()

    var mostrarFormulario by remember { mutableStateOf(false) }

    // Cargar al entrar
    LaunchedEffect(actividadId) {
        viewModel.cargarPreguntas(actividadId)
        viewModel.resetForm()
    }

    // Al crear, cerrar formulario
    LaunchedEffect(form.creada) {
        if (form.creada) {
            mostrarFormulario = false
            viewModel.resetForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preguntas de la Actividad") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = !mostrarFormulario }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar pregunta")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Formulario (colapsable)
            if (mostrarFormulario) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Nueva Pregunta", fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = form.enunciado,
                            onValueChange = viewModel::onEnunciadoChange,
                            label = { Text("Enunciado *") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Tipo
                        var tipoExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = tipoExpanded,
                            onExpandedChange = { tipoExpanded = !tipoExpanded }
                        ) {
                            OutlinedTextField(
                                value = form.tipo,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tipo") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(tipoExpanded) },
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                            )
                            ExposedDropdownMenu(expanded = tipoExpanded, onDismissRequest = { tipoExpanded = false }) {
                                listOf("ALTERNATIVA", "DESARROLLO").forEach { t ->
                                    DropdownMenuItem(
                                        text = { Text(t) },
                                        onClick = { viewModel.onTipoChange(t); tipoExpanded = false }
                                    )
                                }
                            }
                        }

                        if (form.tipo == "ALTERNATIVA") {
                            OutlinedTextField(
                                value = form.alternativas,
                                onValueChange = viewModel::onAlternativasChange,
                                label = { Text("Alternativas (separadas por |)") },
                                supportingText = { Text("Ej: Amarillo|Verde|Azul|Rojo") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        OutlinedTextField(
                            value = form.respuestaCorrecta,
                            onValueChange = viewModel::onRespuestaChange,
                            label = { Text("Respuesta correcta *") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = form.retroalimentacion,
                            onValueChange = viewModel::onRetroChange,
                            label = { Text("Retroalimentación (opcional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        form.error?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }

                        Button(
                            onClick = { viewModel.crearPregunta() },
                            enabled = !form.cargando,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (form.cargando) CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                            else Text("Guardar Pregunta")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Lista de preguntas
            Text("Preguntas existentes (${preguntas.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            when {
                cargando -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                preguntas.isEmpty() -> Text("Aún no hay preguntas. Presiona el botón + para agregar la primera.")
                else -> preguntas.forEach { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${p.orden}. ${p.enunciado}", fontWeight = FontWeight.Bold)
                                Text("Tipo: ${p.tipo}", style = MaterialTheme.typography.bodySmall)
                                if (!p.alternativas.isNullOrBlank()) {
                                    Text("Opciones: ${p.alternativas}", style = MaterialTheme.typography.bodySmall)
                                }
                                Text("✔ Correcta: ${p.respuestaCorrecta}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { viewModel.eliminarPregunta(p.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}