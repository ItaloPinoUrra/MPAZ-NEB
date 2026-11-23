package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.Actividad
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

class TestableActividadRepository(
    private val testApi: MpazNebApi
) : ActividadRepository() {

    override suspend fun getActividades(): List<Actividad> {
        return testApi.getActividades()
    }

    override suspend fun crearActividad(actividad: Actividad): Actividad {
        return testApi.crearActividad(actividad)
    }

    override suspend fun eliminarActividad(id: Long) {
        testApi.eliminarActividad(id)
    }
}

class ActividadRepositoryTest : StringSpec({

    val actividadFake = Actividad(
        id = 1L,
        nombre = "Cuento del Patito",
        modulo = "LECTURA",
        curso = 1,
        seccion = "A",
        dificultad = 1,
        instrucciones = "Lee el cuento",
        estado = "DISPONIBLE"
    )

    "getActividades retorna lista simulada del API" {
        val fakeLista = listOf(
            actividadFake,
            actividadFake.copy(id = 2L, nombre = "Sumas hasta 10", modulo = "MATEMATICA")
        )
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.getActividades() } returns fakeLista
        val repo = TestableActividadRepository(testApi = mockApi)

        runTest {
            val resultado = repo.getActividades()
            resultado shouldContainExactly fakeLista
        }
    }

    "getActividades retorna vacío si no hay datos" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.getActividades() } returns emptyList()
        val repo = TestableActividadRepository(testApi = mockApi)

        runTest {
            repo.getActividades().shouldBeEmpty()
        }
    }

    "getActividades propaga excepción del API" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.getActividades() } throws RuntimeException("Network error")
        val repo = TestableActividadRepository(testApi = mockApi)

        runTest {
            shouldThrow<RuntimeException> {
                repo.getActividades()
            }.message shouldBe "Network error"
        }
    }

    "crearActividad llama al API y retorna la creada" {
        val mockApi = mockk<MpazNebApi>()
        val creada = actividadFake.copy(id = 99L)
        coEvery { mockApi.crearActividad(any()) } returns creada
        val repo = TestableActividadRepository(testApi = mockApi)

        runTest {
            val resultado = repo.crearActividad(actividadFake)
            resultado.id shouldBe 99L
            coVerify(exactly = 1) { mockApi.crearActividad(any()) }
        }
    }
})