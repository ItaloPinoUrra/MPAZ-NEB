package com.example.mpaz_neb.viewmodels

import android.app.Application
import com.example.mpaz_neb.data.model.LoginResponse
import com.example.mpaz_neb.repository.AuthRepository
import com.example.mpaz_neb.sesion.SesionManager
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()
    val application = mockk<Application>(relaxed = true)
    val sesionManagerMock = mockk<SesionManager>(relaxed = true)
    val authRepositoryMock = mockk<AuthRepository>()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "login con campos vacíos muestra error" {
        val vm = LoginViewModel(application, authRepositoryMock, sesionManagerMock)

        runTest {
            vm.login()
            advanceUntilIdle()
            vm.estado.value.error.shouldNotBeNull()
            vm.estado.value.loginExitoso shouldBe false
        }
    }

    "login exitoso cambia loginExitoso a true" {
        coEvery { authRepositoryMock.login(any(), any()) } returns
                LoginResponse(1L, "admin", "ADMIN", "token-123")
        coEvery { sesionManagerMock.guardarSesion(any(), any(), any(), any()) } returns Unit

        val vm = LoginViewModel(application, authRepositoryMock, sesionManagerMock)
        vm.onAliasChange("admin")
        vm.onClaveChange("admin123")

        runTest {
            vm.login()
            advanceUntilIdle()
            vm.estado.value.loginExitoso shouldBe true
            vm.estado.value.rolLogueado shouldBe "ADMIN"
            coVerify(exactly = 1) {
                sesionManagerMock.guardarSesion(1L, "admin", "ADMIN", "token-123")
            }
        }
    }

    "login fallido muestra mensaje de error" {
        coEvery { authRepositoryMock.login(any(), any()) } throws RuntimeException("401")

        val vm = LoginViewModel(application, authRepositoryMock, sesionManagerMock)
        vm.onAliasChange("admin")
        vm.onClaveChange("mala")

        runTest {
            vm.login()
            advanceUntilIdle()
            vm.estado.value.loginExitoso shouldBe false
            vm.estado.value.error.shouldNotBeNull()
        }
    }

    "onAliasChange actualiza el estado" {
        val vm = LoginViewModel(application, authRepositoryMock, sesionManagerMock)
        vm.onAliasChange("admin")
        vm.estado.value.alias shouldBe "admin"
    }
})