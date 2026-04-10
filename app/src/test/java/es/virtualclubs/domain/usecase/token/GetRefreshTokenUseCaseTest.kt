package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetRefreshTokenUseCaseTest {

    private lateinit var securePrefs: SecureUserPreferences
    private lateinit var useCase: GetRefreshTokenUseCase

    @Before
    fun setUp() {
        securePrefs = mockk()
        useCase = GetRefreshTokenUseCase(securePrefs)
    }

    @Test
    fun `devuelve refresh token cuando existe`() = runTest {
        every { securePrefs.refreshToken } returns flowOf("my-refresh-token")

        val result = useCase()

        assertEquals("my-refresh-token", result)
    }

    @Test
    fun `devuelve null cuando no hay token almacenado`() = runTest {
        every { securePrefs.refreshToken } returns flowOf(null)

        val result = useCase()

        assertNull(result)
    }

    @Test
    fun `devuelve null cuando el flow esta vacio`() = runTest {
        every { securePrefs.refreshToken } returns flowOf()

        val result = useCase()

        assertNull(result)
    }
}
