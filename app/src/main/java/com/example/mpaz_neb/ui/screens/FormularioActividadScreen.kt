package com.example.mpaz_neb.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mpaz_neb.viewmodels.ActividadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioActividadScreen(
    navController: NavController,
    viewModel: ActividadViewModel
) {
    val form by viewModel.form.collectAsState()
    val context = LocalContext.current

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) viewModel.onArchivoChange(uri)
    }

    // Al crear exitosamente, volver al catálogo
    LaunchedEffect(form.creada) {
        if (form.creada) {
            viewModel.resetForm()
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Datos de la actividad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = form.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre de la actividad *") },
                modifier = Modifier.fillMaxWidth()
            )

            // Módulo
            var modExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = modExpanded,
                onExpandedChange = { modExpanded = !modExpanded }
            ) {
                OutlinedTextField(
                    value = form.modulo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Módulo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(modExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = modExpanded, onDismissRequest = { modExpanded = false }) {
                    listOf("LECTURA", "MATEMATICA").forEach { mod ->
                        DropdownMenuItem(
                            text = { Text(mod) },
                            onClick = { viewModel.onModuloChange(mod); modExpanded = false }
                        )
                    }
                }
            }

            // Curso
            var cursoExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = cursoExpanded,
                onExpandedChange = { cursoExpanded = !cursoExpanded }
            ) {
                OutlinedTextField(
                    value = "${form.curso}° Básico",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Curso") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(cursoExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = cursoExpanded, onDismissRequest = { cursoExpanded = false }) {
                    (1..4).forEach { c ->
                        DropdownMenuItem(
                            text = { Text("${c}° Básico") },
                            onClick = { viewModel.onCursoChange(c); cursoExpanded = false }
                        )
                    }
                }
            }

            // Sección (libre)
            OutlinedTextField(
                value = form.seccion,
                onValueChange = viewModel::onSeccionChange,
                label = { Text("Sección (opcional)") },
                supportingText = { Text("Ej: A, B, C. Vacío = todo el curso") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dificultad
            var difExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = difExpanded,
                onExpandedChange = { difExpanded = !difExpanded }
            ) {
                OutlinedTextField(
                    value = when (form.dificultad) {
                        1 -> "1 - Fácil"
                        2 -> "2 - Medio"
                        else -> "3 - Difícil"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Dificultad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(difExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = difExpanded, onDismissRequest = { difExpanded = false }) {
                    listOf(1 to "Fácil", 2 to "Medio", 3 to "Difícil").forEach { (n, label) ->
                        DropdownMenuItem(
                            text = { Text("$n - $label") },
                            onClick = { viewModel.onDificultadChange(n); difExpanded = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = form.instrucciones,
                onValueChange = viewModel::onInstruccionesChange,
                label = { Text("Instrucciones (opcional)") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Estado
            var estadoExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = estadoExpanded,
                onExpandedChange = { estadoExpanded = !estadoExpanded }
            ) {
                OutlinedTextField(
                    value = form.estado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(estadoExpanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = estadoExpanded, onDismissRequest = { estadoExpanded = false }) {
                    listOf("DISPONIBLE", "BORRADOR", "DESACTIVADA").forEach { est ->
                        DropdownMenuItem(
                            text = { Text(est) },
                            onClick = { viewModel.onEstadoChange(est); estadoExpanded = false }
                        )
                    }
                }
            }

            // ===== ADJUNTAR ARCHIVO =====
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Material adjunto (opcional)", fontWeight = FontWeight.Bold)
                    Text(
                        "Puedes adjuntar un PDF o imagen con la lectura o ejercicios.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))

                    if (form.archivoUri != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AttachFile, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Archivo seleccionado",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { viewModel.onArchivoChange(null) }) {
                                Text("Quitar")
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                fileLauncher.launch(
                                    arrayOf(
                                        "application/pdf",
                                        "image/jpeg",
                                        "image/png"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.AttachFile, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Adjuntar archivo")
                        }
                    }
                }
            }

            val errorActual = form.error
            if (errorActual != null) {
                Text(
                    text = errorActual,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.crearActividad(context) },
                enabled = !form.cargando,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (form.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Crear Actividad")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}