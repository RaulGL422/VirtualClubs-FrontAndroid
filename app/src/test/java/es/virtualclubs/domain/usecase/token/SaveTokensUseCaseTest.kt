package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.session.UserSession
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveTokensUseCaseTest {

    private lateinit var securePrefs: SecureUserPreferences
    private lateinit var userSession: UserSession
    private lateinit var useCase: SaveTokensUseCase

    @Before
    fun setUp() {
        securePrefs = mockk(relaxed = true)
        userSession = UserSession()
        useCase = SaveTokensUseCase(securePrefs, userSession)
    }

    @Test
    fun `guarda access token en SecureUserPreferences`() = runTest {
        useCase("access-123", "refresh-456")

        coVerify(exactly = 1) { securePrefs.saveAccessToken("access-123") }
    }

    @Test
    fun `guarda refresh token en SecureUserPreferences`() = runTest {
        useCase("access-123", "refresh-456")

        coVerify(exactly = 1) { securePrefs.saveRefreshToken("refresh-456") }
    }

    @Test
    fun `cachea access token en UserSession`() = runTest {
        useCase("access-123", "refresh-456")

        assertEquals("access-123", userSession.cachedAccessToken)
    }

    @Test
    fun `cachea el access token correcto cuando se llama varias veces`() = runTest {
        useCase("first-token", "refresh-1")
        useCase("second-token", "refresh-2")

        assertEquals("second-token", userSession.cachedAccessToken)
    }
}
