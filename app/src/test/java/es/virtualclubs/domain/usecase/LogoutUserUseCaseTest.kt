package es.virtualclubs.domain.usecase

import es.virtualclubs.data.models.User
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
    fun `logout limpia el usuario en sesion`() = runTest {
        userSession.updateUser(User(email = "logged@test.com"))

        useCase()

        assertEquals(User(), userSession.currentUser.value)
    }

    @Test
    fun `logout con usuario ya limpio no falla`() = runTest {
        // userSession ya tiene User() por defecto
        useCase()

        assertEquals(User(), userSession.currentUser.value)
    }
}
