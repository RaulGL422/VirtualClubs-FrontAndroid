package es.virtualclubs.domain.usecase

import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSessionStateUseCaseTest {

    private lateinit var userSession: UserSession
    private lateinit var useCase: GetSessionStateUseCase

    @Before
    fun setUp() {
        userSession = UserSession()
        useCase = GetSessionStateUseCase(userSession)
    }

    @Test
    fun `invoke emite LoggedOut por defecto`() = runTest {
        val estado = useCase().first()

        assertEquals(SessionState.LoggedOut, estado)
    }

    @Test
    fun `invoke emite LoggedIn tras actualizar la sesion`() = runTest {
        val usuario = User(email = "test@test.com")
        userSession.login(usuario)

        val estado = useCase().first()

        assertEquals(SessionState.LoggedIn(usuario), estado)
    }

    @Test
    fun `invoke devuelve el sessionState actual del UserSession`() = runTest {
        assertEquals(userSession.sessionState.value, useCase().first())

        val usuario = User(email = "otro@test.com")
        userSession.login(usuario)

        assertEquals(userSession.sessionState.value, useCase().first())
    }
}
