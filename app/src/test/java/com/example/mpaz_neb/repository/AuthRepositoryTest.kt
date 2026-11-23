package com.example.mpaz_neb.repository

import com.example.mpaz_neb.data.model.LoginRequest
import com.example.mpaz_neb.data.model.LoginResponse
import com.example.mpaz_neb.data.remote.MpazNebApi
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class TestableAuthRepository(
    private val testApi: MpazNebApi
) : AuthRepository() {
    override suspend fun login(alias: String, clave: String): LoginResponse {
        return testApi.login(LoginRequest(alias, clave))
    }
}

class AuthRepositoryTest : StringSpec({

    "login exitoso retorna respuesta del backend" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.login(any()) } returns LoginResponse(
            id = 1L, alias = "admin", rol = "ADMIN", token = "token-123"
        )
        val repo = TestableAuthRepository(testApi = mockApi)

        runTest {
            val resultado = repo.login("admin", "admin123")
            resultado.alias shouldBe "admin"
            resultado.rol shouldBe "ADMIN"
            resultado.token shouldBe "token-123"
        }
    }

    "login falla con credenciales inválidas" {
        val mockApi = mockk<MpazNebApi>()
        coEvery { mockApi.login(any()) } throws RuntimeException("401 Unauthorized")
        val repo = TestableAuthRepository(testApi = mockApi)

        runTest {
            shouldThrow<RuntimeException> {
                repo.login("admin", "mala")
            }.message shouldBe "401 Unauthorized"
        }
    }
})