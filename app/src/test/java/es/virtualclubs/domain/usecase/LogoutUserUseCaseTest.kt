package es.virtualclubs.domain.usecase

import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.model.User
import es.virtualclubs.domain.usecase.token.ClearTokensUseCase
import es.virtualclubs.fakes.FakeAuthRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LogoutUserUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var userSession: UserSession
    private lateinit var clearTokensUseCase: ClearTokensUseCase
    private lateinit var userPreferences: UserPreferences
    private lateinit var useCase: LogoutUserUseCase

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        userSession = UserSession()
        clearTokensUseCase = mockk(relaxed = true)
        userPreferences = mockk(relaxed = true)
        useCase = LogoutUserUseCase(repository, userSession, clearTokensUseCase, userPreferences)
    }

    // ─── notifyBackend = true (por defecto) ──────────────────────────────────

    @Test
    fun `logout notifica al backend por defecto`() = runTest {
        useCase()

        assertEquals(1, repository.logoutCallCount)
    }

    @Test
    fun `logout limpia la sesion en UserSession`() = runTest {
        userSession.login(User(email = "logged@test.com"))

        useCase()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    @Test
    fun `logout limpia el access token cacheado`() = runTest {
        userSession.cacheAccessToken("token-previo")
        userSession.login(User(email = "logged@test.com"))

        useCase()

        assertNull(userSession.cachedAccessToken)
    }

    @Test
    fun `logout limpia los tokens del disco`() = runTest {
        useCase()

        coVerify { clearTokensUseCase() }
    }

    @Test
    fun `logout limpia el email del disco`() = runTest {
        useCase()

        coVerify { userPreferences.clearUser() }
    }

    @Test
    fun `logout con sesion ya limpia no falla`() = runTest {
        useCase()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    // ─── notifyBackend = false (logout forzado) ──────────────────────────────

    @Test
    fun `logout forzado no llama al repositorio`() = runTest {
        useCase(notifyBackend = false)

        assertEquals(0, repository.logoutCallCount)
    }

    @Test
    fun `logout forzado limpia la sesion y tokens igual que el voluntario`() = runTest {
        userSession.login(User(email = "logged@test.com"))
        userSession.cacheAccessToken("token")

        useCase(notifyBackend = false)

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
        assertNull(userSession.cachedAccessToken)
        coVerify { clearTokensUseCase() }
        coVerify { userPreferences.clearUser() }
    }

    // ─── Resiliencia ─────────────────────────────────────────────────────────

    @Test
    fun `fallo del backend no impide limpiar sesion local`() = runTest {
        repository.logoutResult = Result.failure(Exception("network error"))
        userSession.login(User(email = "logged@test.com"))

        useCase(notifyBackend = true)

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
        coVerify { clearTokensUseCase() }
        coVerify { userPreferences.clearUser() }
    }
}
