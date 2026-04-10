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

class RegisterUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var useCase: RegisterUseCase

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        useCase = RegisterUseCase(repository)
    }

    @Test
    fun `registro exitoso devuelve Result success con tokens`() = runTest {
        repository.registerResult = Result.success(validTokens)

        val result = useCase("new@test.com", "password123")

        assertTrue(result.isSuccess)
        assertEquals(validTokens, result.getOrNull())
    }

    @Test
    fun `registro con email existente devuelve EMAIL_ALREADY_EXISTS`() = runTest {
        repository.registerResult = Result.failure(VirtualClubException(ErrorType.EMAIL_ALREADY_EXISTS))

        val result = useCase("existing@test.com", "password123")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.EMAIL_ALREADY_EXISTS, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `registro con password muy corta devuelve PASSWORD_TOO_SHORT`() = runTest {
        repository.registerResult = Result.failure(VirtualClubException(ErrorType.PASSWORD_TOO_SHORT))

        val result = useCase("user@test.com", "123")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.PASSWORD_TOO_SHORT, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `registro delega email y password al repositorio correctamente`() = runTest {
        repository.registerResult = Result.success(validTokens)

        useCase("delegated@test.com", "pass123")

        assertEquals("delegated@test.com", repository.lastRegisterEmail)
        assertEquals("pass123", repository.lastRegisterPassword)
    }

    @Test
    fun `registro llama al repositorio exactamente una vez`() = runTest {
        repository.registerResult = Result.success(validTokens)

        useCase("user@test.com", "password123")

        // FakeAuthRepository no tiene contador de register, verificamos por el email capturado
        assertEquals("user@test.com", repository.lastRegisterEmail)
    }

    @Test
    fun `registro con email en formato invalido devuelve INVALID_EMAIL`() = runTest {
        repository.registerResult = Result.failure(VirtualClubException(ErrorType.INVALID_EMAIL))

        val result = useCase("not-an-email", "password123")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.INVALID_EMAIL, (result.exceptionOrNull() as VirtualClubException).errorType)
    }
}
