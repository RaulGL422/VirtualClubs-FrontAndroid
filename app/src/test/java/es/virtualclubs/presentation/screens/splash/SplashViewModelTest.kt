package es.virtualclubs.presentation.screens.splash

import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.SessionState
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.usecase.RefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.fakes.FakeRefreshRepository
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getRefreshToken: GetRefreshTokenUseCase
    private lateinit var fakeRefreshRepository: FakeRefreshRepository
    private lateinit var userPreferences: UserPreferences
    private lateinit var userSession: UserSession

    @Before
    fun setUp() {
        getRefreshToken = mockk()
        fakeRefreshRepository = FakeRefreshRepository()
        userPreferences = mockk(relaxed = true)
        userSession = UserSession()
        every { userPreferences.userEmailFlow } returns flowOf("user@test.com")
    }

    private fun buildViewModel() = SplashViewModel(
        getRefreshToken = getRefreshToken,
        refreshToken = RefreshTokenUseCase(fakeRefreshRepository),
        userPreferences = userPreferences,
        userSession = userSession
    )

    // ─── Sin token almacenado ─────────────────────────────────────────────────

    @Test
    fun `sin refresh token emite destino Login`() = runTest {
        coEvery { getRefreshToken() } returns null

        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(SplashDestination.Login, vm.destination.value)
    }

    @Test
    fun `sin refresh token la sesion permanece LoggedOut`() = runTest {
        coEvery { getRefreshToken() } returns null

        buildViewModel()
        advanceUntilIdle()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }

    // ─── Con token, refresh exitoso ───────────────────────────────────────────

    @Test
    fun `con refresh token valido y refresh exitoso emite destino Home`() = runTest {
        coEvery { getRefreshToken() } returns "valid-token"
        fakeRefreshRepository.refreshResult = Result.success(Unit)

        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(SplashDestination.Home, vm.destination.value)
    }

    @Test
    fun `con refresh exitoso la sesion queda LoggedIn con el email guardado`() = runTest {
        coEvery { getRefreshToken() } returns "valid-token"
        fakeRefreshRepository.refreshResult = Result.success(Unit)
        every { userPreferences.userEmailFlow } returns flowOf("test@example.com")

        buildViewModel()
        advanceUntilIdle()

        val session = userSession.sessionState.value
        assertTrue(session is SessionState.LoggedIn)
        assertEquals("test@example.com", (session as SessionState.LoggedIn).user.email)
    }

    // ─── Con token, refresh fallido ───────────────────────────────────────────

    @Test
    fun `con token expirado y refresh fallido emite destino Login`() = runTest {
        coEvery { getRefreshToken() } returns "expired-token"
        fakeRefreshRepository.refreshResult = Result.failure(
            VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN)
        )

        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(SplashDestination.Login, vm.destination.value)
    }

    @Test
    fun `con token expirado y refresh fallido la sesion permanece LoggedOut`() = runTest {
        coEvery { getRefreshToken() } returns "expired-token"
        fakeRefreshRepository.refreshResult = Result.failure(
            VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN)
        )

        buildViewModel()
        advanceUntilIdle()

        assertEquals(SessionState.LoggedOut, userSession.sessionState.value)
    }
}
