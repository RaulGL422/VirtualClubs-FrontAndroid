package es.virtualclubs.data.session

import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserSessionTest {

    private lateinit var userSession: UserSession

    private val testUser = User(email = "test@test.com")

    @Before
    fun setUp() {
        userSession = UserSession()
    }

    // ─── Estado inicial ───────────────────────────────────────────────────────

    @Test
    fun `estado inicial es LoggedOut`() {
        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    @Test
    fun `token cacheado inicial es null`() {
        assertNull(userSession.cachedAccessToken)
    }

    // ─── login ────────────────────────────────────────────────────────────────

    @Test
    fun `login emite LoggedIn con el usuario correcto`() {
        userSession.login(testUser)

        assertEquals(SessionState.LoggedIn(testUser), userSession.sessionState.value)
    }

    @Test
    fun `login con usuario sin email emite LoggedIn`() {
        val userSinEmail = User(email = null)
        userSession.login(userSinEmail)

        assertEquals(SessionState.LoggedIn(userSinEmail), userSession.sessionState.value)
    }

    // ─── logout ───────────────────────────────────────────────────────────────

    @Test
    fun `logout emite LoggedOut`() {
        userSession.login(testUser)

        userSession.logout()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    @Test
    fun `logout limpia el token cacheado`() {
        userSession.cacheAccessToken("mi-token")

        userSession.logout()

        assertNull(userSession.cachedAccessToken)
    }

    @Test
    fun `logout limpia estado y token en la misma operacion`() {
        userSession.login(testUser)
        userSession.cacheAccessToken("mi-token")

        userSession.logout()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
        assertNull(userSession.cachedAccessToken)
    }

    @Test
    fun `logout desde LoggedOut no falla`() {
        userSession.logout()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    // ─── cacheAccessToken ─────────────────────────────────────────────────────

    @Test
    fun `cacheAccessToken almacena el token`() {
        userSession.cacheAccessToken("nuevo-token")

        assertEquals("nuevo-token", userSession.cachedAccessToken)
    }

    @Test
    fun `cacheAccessToken con null borra el token`() {
        userSession.cacheAccessToken("token-previo")

        userSession.cacheAccessToken(null)

        assertNull(userSession.cachedAccessToken)
    }

    @Test
    fun `cacheAccessToken no modifica el sessionState`() {
        userSession.login(testUser)

        userSession.cacheAccessToken("token")

        assertEquals(SessionState.LoggedIn(testUser), userSession.sessionState.value)
    }
}
