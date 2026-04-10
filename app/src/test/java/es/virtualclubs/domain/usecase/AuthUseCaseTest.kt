package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.fakes.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var authUseCase: AuthUseCase

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        authUseCase = AuthUseCase(repository)
    }

    @Test
    fun `login exitoso devuelve Result success con tokens`() = runTest {
        repository.loginResult = Result.success(validTokens)

        val result = authUseCase("user@test.com", "password123")

        assertTrue(result.isSuccess)
        assertEquals(validTokens, result.getOrNull())
    }

    @Test
    fun `login con credenciales invalidas devuelve INVALID_CREDENTIALS`() = runTest {
        repository.loginResult = Result.failure(VirtualClubException(ErrorType.INVALID_CREDENTIALS))

        val result = authUseCase("user@test.com", "wrong")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.INVALID_CREDENTIALS, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `login con email no verificado devuelve EMAIL_NOT_VERIFIED`() = runTest {
        repository.loginResult = Result.failure(VirtualClubException(ErrorType.EMAIL_NOT_VERIFIED))

        val result = authUseCase("unverified@test.com", "password123")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.EMAIL_NOT_VERIFIED, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `login delega email y password al repositorio correctamente`() = runTest {
        repository.loginResult = Result.success(validTokens)

        authUseCase("delegated@test.com", "pass123")

        assertEquals("delegated@test.com", repository.lastLoginEmail)
        assertEquals("pass123", repository.lastLoginPassword)
    }

    @Test
    fun `login con usuario no encontrado devuelve EMAIL_NOT_FOUND`() = runTest {
        repository.loginResult = Result.failure(VirtualClubException(ErrorType.EMAIL_NOT_FOUND))

        val result = authUseCase("nobody@test.com", "password123")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.EMAIL_NOT_FOUND, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `login llama al repositorio exactamente una vez`() = runTest {
        repository.loginResult = Result.success(validTokens)

        authUseCase("user@test.com", "password123")

        assertEquals(1, repository.loginCallCount)
    }
}
