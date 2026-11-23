package com.example.mpaz_neb.viewmodels

import com.example.mpaz_neb.data.model.RegistroResponse
import com.example.mpaz_neb.repository.AuthRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()
    val authRepositoryMock = mockk<AuthRepository>()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "registro con campos vacios muestra error" {
        val vm = RegistroViewModel(authRepositoryMock)

        runTest {
            vm.registrar()
            advanceUntilIdle()
            vm.estado.value.error.shouldNotBeNull()
            vm.estado.value.registroExitoso shouldBe false
        }
    }

    "registro con contrasenias que no coinciden muestra error" {
        val vm = RegistroViewModel(authRepositoryMock)
        vm.onAliasChange("nuevo_usuario")
        vm.onClaveChange("123456")
        vm.onConfirmarClaveChange("654321")

        runTest {
            vm.registrar()
            advanceUntilIdle()
            vm.estado.value.error shouldBe "Las contraseñas no coinciden"
            vm.estado.value.registroExitoso shouldBe false
        }
    }

    "registro exitoso cambia registroExitoso a true" {
        coEvery { authRepositoryMock.registrar(any(), any(), any()) } returns
                RegistroResponse("Usuario registrado exitosamente", 2L, "nuevo_usuario", "ESTUDIANTE")

        val vm = RegistroViewModel(authRepositoryMock)
        vm.onAliasChange("nuevo_usuario")
        vm.onClaveChange("123456")
        vm.onConfirmarClaveChange("123456")
        vm.onRolChange("ESTUDIANTE")

        runTest {
            vm.registrar()
            advanceUntilIdle()
            vm.estado.value.registroExitoso shouldBe true
            vm.estado.value.mensajeExito shouldBe "Usuario registrado exitosamente"
        }
    }
})
