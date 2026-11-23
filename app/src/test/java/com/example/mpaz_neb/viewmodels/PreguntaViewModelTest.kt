package com.example.mpaz_neb.viewmodels

import com.example.mpaz_neb.data.model.Pregunta
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
class PreguntaViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest { Dispatchers.setMain(dispatcher) }
    afterTest { Dispatchers.resetMain() }

    val preguntaFake = Pregunta(
        id = 1L,
        actividadId = 1L,
        enunciado = "¿Color del patito?",
        tipo = "ALTERNATIVA",
        alternativas = "Amarillo|Verde",
        respuestaCorrecta = "Amarillo",
        retroalimentacion = "Correcto",
        orden = 1
    )

    "cargarPreguntas actualiza la lista" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) {
                _actividadId.value = actividadId
                _preguntas.value = listOf(preguntaFake)
                _cargando.value = false
            }
        }

        runTest {
            testVM.cargarPreguntas(1L)
            testVM.preguntas.value shouldContainExactly listOf(preguntaFake)
            testVM.cargando.value shouldBe false
            testVM.actividadId.value shouldBe 1L
        }
    }

    "onEnunciadoChange actualiza el enunciado y limpia error" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) { /* no-op */ }
        }

        testVM.onEnunciadoChange("¿Prueba?")
        testVM.form.value.enunciado shouldBe "¿Prueba?"
        testVM.form.value.error shouldBe null
    }

    "onTipoChange actualiza el tipo" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) { /* no-op */ }
        }

        testVM.onTipoChange("DESARROLLO")
        testVM.form.value.tipo shouldBe "DESARROLLO"
    }

    "onRespuestaChange actualiza la respuesta correcta" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) { /* no-op */ }
        }

        testVM.onRespuestaChange("Amarillo")
        testVM.form.value.respuestaCorrecta shouldBe "Amarillo"
    }

    "onAlternativasChange actualiza las alternativas" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) { /* no-op */ }
        }

        testVM.onAlternativasChange("A|B|C")
        testVM.form.value.alternativas shouldBe "A|B|C"
    }

    "onRetroChange actualiza la retroalimentación" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) { /* no-op */ }
        }

        testVM.onRetroChange("¡Muy bien!")
        testVM.form.value.retroalimentacion shouldBe "¡Muy bien!"
    }

    "resetForm reinicia el formulario" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) { /* no-op */ }
        }

        testVM.onEnunciadoChange("Test")
        testVM.onAlternativasChange("A|B")
        testVM.resetForm()

        testVM.form.value.enunciado shouldBe ""
        testVM.form.value.alternativas shouldBe ""
    }

    "crearPregunta sin enunciado muestra error" {
        val testVM = object : PreguntaViewModel() {
            override fun cargarPreguntas(actividadId: Long) {
                // 👇 Seteamos _actividadId (es protected, accesible desde subclase)
                _actividadId.value = actividadId
            }
        }
        testVM.cargarPreguntas(1L)
        testVM.crearPregunta()

        testVM.form.value.error shouldBe "Enunciado y respuesta correcta son obligatorios"
    }
})