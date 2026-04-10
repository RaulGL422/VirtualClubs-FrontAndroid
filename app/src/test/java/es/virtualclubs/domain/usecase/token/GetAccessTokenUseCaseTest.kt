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

class GetAccessTokenUseCaseTest {

    private lateinit var securePrefs: SecureUserPreferences
    private lateinit var useCase: GetAccessTokenUseCase

    @Before
    fun setUp() {
        securePrefs = mockk()
        useCase = GetAccessTokenUseCase(securePrefs)
    }

    @Test
    fun `devuelve access token cuando existe`() = runTest {
        every { securePrefs.accessToken } returns flowOf("my-access-token")

        val result = useCase()

        assertEquals("my-access-token", result)
    }

    @Test
    fun `devuelve null cuando no hay token almacenado`() = runTest {
        every { securePrefs.accessToken } returns flowOf(null)

        val result = useCase()

        assertNull(result)
    }

    @Test
    fun `devuelve null cuando el flow esta vacio`() = runTest {
        every { securePrefs.accessToken } returns flowOf()

        val result = useCase()

        assertNull(result)
    }
}
