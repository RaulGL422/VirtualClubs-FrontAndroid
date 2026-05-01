package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.session.UserSession
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ClearTokensUseCaseTest {

    private lateinit var securePrefs: SecureUserPreferences
    private lateinit var userSession: UserSession
    private lateinit var useCase: ClearTokensUseCase

    @Before
    fun setUp() {
        securePrefs = mockk(relaxed = true)
        userSession = UserSession()
        useCase = ClearTokensUseCase(securePrefs, userSession)
    }

    @Test
    fun `limpia todos los datos en SecureUserPreferences`() = runTest {
        useCase()

        coVerify(exactly = 1) { securePrefs.clearAll() }
    }

    @Test
    fun `elimina el access token cacheado en UserSession`() = runTest {
        userSession.cacheAccessToken("token-previo")

        useCase()

        assertNull(userSession.cachedAccessToken)
    }

    @Test
    fun `funciona correctamente si UserSession ya esta limpia`() = runTest {
        // cachedAccessToken ya es null por defecto

        useCase()

        assertNull(userSession.cachedAccessToken)
    }
}
