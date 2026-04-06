package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.RefreshRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshTokenUseCaseTest {

    private lateinit var refreshRepository: RefreshRepository
    private lateinit var refreshTokenUseCase: RefreshTokenUseCase

    @Before
    fun setUp() {
        refreshRepository = mockk()
        refreshTokenUseCase = RefreshTokenUseCase(refreshRepository)
    }

    @Test
    fun `invoke exitoso devuelve Result success`() = runTest {
        coEvery { refreshRepository.refresh(any()) } returns Result.success(Unit)

        val result = refreshTokenUseCase()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { refreshRepository.refresh(true) }
    }

    @Test
    fun `invoke con token invalido devuelve INVALID_REFRESH_TOKEN`() = runTest {
        coEvery { refreshRepository.refresh(any()) } returns
            Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))

        val result = refreshTokenUseCase()

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as VirtualClubException
        assertEquals(ErrorType.INVALID_REFRESH_TOKEN, exception.errorType)
    }

    @Test
    fun `invoke sin tokens almacenados devuelve MISSING_TOKENS`() = runTest {
        coEvery { refreshRepository.refresh(any()) } returns
            Result.failure(VirtualClubException(ErrorType.MISSING_TOKENS))

        val result = refreshTokenUseCase()

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as VirtualClubException
        assertEquals(ErrorType.MISSING_TOKENS, exception.errorType)
    }

    @Test
    fun `invoke delega canLogout=true por defecto al repositorio`() = runTest {
        coEvery { refreshRepository.refresh(true) } returns Result.success(Unit)

        refreshTokenUseCase()

        coVerify { refreshRepository.refresh(true) }
    }

    @Test
    fun `invoke con canLogout=false lo pasa correctamente al repositorio`() = runTest {
        coEvery { refreshRepository.refresh(false) } returns Result.success(Unit)

        refreshTokenUseCase(canLogout = false)

        coVerify { refreshRepository.refresh(false) }
    }
}
