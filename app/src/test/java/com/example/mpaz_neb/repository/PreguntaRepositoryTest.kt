package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.Pregunta
import com.example.mpaz_neb.data.remote.MpazNebApi
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class TestablePreguntaRepository(
    private val testApi: MpazNebApi
) : PreguntaRepository() {
    override suspend fun getPreguntas(actividadId: Long): List<Pregunta> {
        return testApi.getPreguntas(actividadId)
    }

    override suspend fun crearPregunta(actividadId: Long, pregunta: Pregunta): Pregunta {
        return testApi.crearPregunta(actividadId, pregunta)
    }

    override suspend fun eliminarPregunta(id: Long) {
        testApi.eliminarPregunta(id)
    }
}

class PreguntaRepositoryTest : StringSpec({

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

    "getPreguntas retorna lista simulada" {
        val mockApi = mockk<MpazNebApi>()
        val fakeLista = listOf(preguntaFake, preguntaFake.copy(id = 2L))
        coEvery { mockApi.getPreguntas(1L) } returns fakeLista
        val repo = TestablePreguntaRepository(testApi = mockApi)

        runTest {
            val resultado = repo.getPreguntas(1L)
            resultado shouldContainExactly fakeLista
        }
    }

    "getPreguntas retorna vacío si no hay datos" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.getPreguntas(any()) } returns emptyList()
        val repo = TestablePreguntaRepository(testApi = mockApi)

        runTest {
            repo.getPreguntas(1L).shouldBeEmpty()
        }
    }

    "getPreguntas propaga excepción" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.getPreguntas(any()) } throws RuntimeException("Network error")
        val repo = TestablePreguntaRepository(testApi = mockApi)

        runTest {
            shouldThrow<RuntimeException> {
                repo.getPreguntas(1L)
            }.message shouldBe "Network error"
        }
    }

    "crearPregunta llama al API y retorna la creada" {
        val mockApi = mockk<MpazNebApi>()
        val creada = preguntaFake.copy(id = 99L)
        coEvery { mockApi.crearPregunta(any(), any()) } returns creada
        val repo = TestablePreguntaRepository(testApi = mockApi)

        runTest {
            val resultado = repo.crearPregunta(1L, preguntaFake)
            resultado.id shouldBe 99L
            coVerify(exactly = 1) { mockApi.crearPregunta(any(), any()) }
        }
    }

    "eliminarPregunta llama al API" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.eliminarPregunta(any()) } returns Unit
        val repo = TestablePreguntaRepository(testApi = mockApi)

        runTest {
            repo.eliminarPregunta(1L)
            coVerify(exactly = 1) { mockApi.eliminarPregunta(1L) }
        }
    }
})