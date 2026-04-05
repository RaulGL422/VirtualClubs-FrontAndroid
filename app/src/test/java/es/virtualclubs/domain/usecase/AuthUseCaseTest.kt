package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var authUseCase: AuthUseCase

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        authRepository = mockk()
        authUseCase = AuthUseCase(authRepository)
    }

    @Test
    fun `login exitoso devuelve Result success con tokens`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(validTokens)

        val result = authUseCase("user@test.com", "password123")

        assertTrue(result.isSuccess)
        assertEquals(validTokens, result.getOrNull())
    }

    @Test
    fun `login con credenciales invalidas devuelve INVALID_CREDENTIALS`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns
            Result.failure(VirtualClubException(ErrorType.INVALID_CREDENTIALS))

        val result = authUseCase("user@test.com", "wrong")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as VirtualClubException
        assertEquals(ErrorType.INVALID_CREDENTIALS, exception.errorType)
    }

    @Test
    fun `login con email no verificado devuelve EMAIL_NOT_VERIFIED`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns
            Result.failure(VirtualClubException(ErrorType.EMAIL_NOT_VERIFIED))

        val result = authUseCase("unverified@test.com", "password123")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as VirtualClubException
        assertEquals(ErrorType.EMAIL_NOT_VERIFIED, exception.errorType)
    }

    @Test
    fun `login delega email y password al repositorio correctamente`() = runTest {
        val email = "delegated@test.com"
        val password = "pass123"
        coEvery { authRepository.login(email, password) } returns Result.success(validTokens)

        authUseCase(email, password)

        coVerify(exactly = 1) { authRepository.login(email, password) }
    }

    @Test
    fun `login con usuario no encontrado devuelve EMAIL_NOT_FOUND`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns
            Result.failure(VirtualClubException(ErrorType.EMAIL_NOT_FOUND))

        val result = authUseCase("nobody@test.com", "password123")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as VirtualClubException
        assertEquals(ErrorType.EMAIL_NOT_FOUND, exception.errorType)
    }
}
