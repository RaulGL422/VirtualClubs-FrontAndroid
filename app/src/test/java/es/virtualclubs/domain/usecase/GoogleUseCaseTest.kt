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

class GoogleUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var useCase: GoogleUseCase

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        useCase = GoogleUseCase(repository)
    }

    @Test
    fun `autenticacion google exitosa devuelve Result success con tokens`() = runTest {
        repository.googleResult = Result.success(validTokens)

        val result = useCase("valid-google-id-token")

        assertTrue(result.isSuccess)
        assertEquals(validTokens, result.getOrNull())
    }

    @Test
    fun `autenticacion google con token invalido devuelve INVALID_GOOGLE_TOKEN`() = runTest {
        repository.googleResult = Result.failure(VirtualClubException(ErrorType.INVALID_GOOGLE_TOKEN))

        val result = useCase("invalid-token")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.INVALID_GOOGLE_TOKEN, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `use case delega idToken al repositorio correctamente`() = runTest {
        repository.googleResult = Result.success(validTokens)

        useCase("my-google-id-token")

        assertEquals("my-google-id-token", repository.lastGoogleIdToken)
    }

    @Test
    fun `error de red devuelve CANT_CONNECT_SERVER`() = runTest {
        repository.googleResult = Result.failure(VirtualClubException(ErrorType.CANT_CONNECT_SERVER))

        val result = useCase("valid-token")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.CANT_CONNECT_SERVER, (result.exceptionOrNull() as VirtualClubException).errorType)
    }
}
