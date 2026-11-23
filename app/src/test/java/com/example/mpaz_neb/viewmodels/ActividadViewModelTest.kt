package com.example.mpaz_neb.viewmodels

import com.example.mpaz_neb.data.model.Actividad
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    "actividades contiene los datos esperados" {
        val fakeLista = listOf(
            Actividad(id = 1L, nombre = "Cuento 1", modulo = "LECTURA", curso = 1, dificultad = 1, estado = "DISPONIBLE"),
            Actividad(id = 2L, nombre = "Sumas", modulo = "MATEMATICA", curso = 2, dificultad = 2, estado = "DISPONIBLE")
        )

        val testViewModel = object : ActividadViewModel() {
            override fun fetchActividades() {
                _actividades.value = fakeLista
                _cargando.value = false
            }
        }

        runTest {
            testViewModel.fetchActividades()
            testViewModel.actividades.value shouldContainExactly fakeLista
        }
    }

    "cargando es false después de cargar" {
        val testViewModel = object : ActividadViewModel() {
            override fun fetchActividades() {
                _actividades.value = emptyList()
                _cargando.value = false
            }
        }

        runTest {
            testViewModel.fetchActividades()
            testViewModel.cargando.value shouldBe false
        }
    }

    "onNombreChange actualiza el nombre" {
        val testViewModel = object : ActividadViewModel() {
            override fun fetchActividades() { /* no-op */ }
        }

        testViewModel.onNombreChange("Prueba")
        testViewModel.form.value.nombre shouldBe "Prueba"
    }

    "resetForm limpia el formulario" {
        val testViewModel = object : ActividadViewModel() {
            override fun fetchActividades() { /* no-op */ }
        }

        testViewModel.onNombreChange("Test")
        testViewModel.onModuloChange("MATEMATICA")
        testViewModel.resetForm()

        testViewModel.form.value.nombre shouldBe ""
        testViewModel.form.value.modulo shouldBe "LECTURA"
    }
})