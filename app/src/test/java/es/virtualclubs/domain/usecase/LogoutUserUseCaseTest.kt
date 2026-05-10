package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.model.User
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.fakes.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LogoutUserUseCaseTest {

    private lateinit var repository: FakeAuthRepository
    private lateinit var userSession: UserSession
    private lateinit var useCase: LogoutUserUseCase

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        userSession = UserSession()
        useCase = LogoutUserUseCase(repository, userSession)
    }

    @Test
    fun `logout llama al repositorio una vez`() = runTest {
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
    fun `logout con sesion ya limpia no falla`() = runTest {
        // sessionState ya es LoggedOut por defecto
        useCase()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    @Test
    fun `logout limpia el access token cacheado`() = runTest {
        userSession.cacheAccessToken("token-previo")
        userSession.login(User(email = "logged@test.com"))

        useCase()

        assertEquals(null, userSession.cachedAccessToken)
    }
}
