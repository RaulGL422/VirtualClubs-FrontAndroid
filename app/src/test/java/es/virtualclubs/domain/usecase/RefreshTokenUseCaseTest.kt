package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.fakes.FakeRefreshRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshTokenUseCaseTest {

    private lateinit var repository: FakeRefreshRepository
    private lateinit var useCase: RefreshTokenUseCase

    @Before
    fun setUp() {
        repository = FakeRefreshRepository()
        useCase = RefreshTokenUseCase(repository)
    }

    @Test
    fun `invoke exitoso devuelve Result success`() = runTest {
        repository.refreshResult = Result.success(Unit)

        val result = useCase()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke exitoso llama al repositorio exactamente una vez`() = runTest {
        repository.refreshResult = Result.success(Unit)

        useCase()

        assertEquals(1, repository.refreshCallCount)
    }

    @Test
    fun `invoke con token invalido devuelve INVALID_REFRESH_TOKEN`() = runTest {
        repository.refreshResult = Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(ErrorType.INVALID_REFRESH_TOKEN, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `invoke sin tokens almacenados devuelve MISSING_TOKENS`() = runTest {
        repository.refreshResult = Result.failure(VirtualClubException(ErrorType.MISSING_TOKENS))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(ErrorType.MISSING_TOKENS, (result.exceptionOrNull() as VirtualClubException).errorType)
    }
}
