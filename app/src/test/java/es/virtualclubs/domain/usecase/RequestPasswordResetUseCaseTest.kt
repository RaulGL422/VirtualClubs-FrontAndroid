package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.fakes.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RequestPasswordResetUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var useCase: RequestPasswordResetUseCase

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        useCase = RequestPasswordResetUseCase(repository)
    }

    @Test
    fun `invoke exitoso devuelve Result success`() = runTest {
        repository.requestPasswordResetResult = Result.success(Unit)

        val result = useCase("user@test.com")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke fallido devuelve el error del repositorio`() = runTest {
        repository.requestPasswordResetResult = Result.failure(VirtualClubException(ErrorType.USER_NOT_FOUND))

        val result = useCase("noexiste@test.com")

        assertTrue(result.isFailure)
        assertEquals(ErrorType.USER_NOT_FOUND, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `invoke delega el email correcto al repositorio`() = runTest {
        repository.requestPasswordResetResult = Result.success(Unit)

        useCase("delegado@test.com")

        assertEquals("delegado@test.com", repository.lastRequestPasswordResetEmail)
    }
}
