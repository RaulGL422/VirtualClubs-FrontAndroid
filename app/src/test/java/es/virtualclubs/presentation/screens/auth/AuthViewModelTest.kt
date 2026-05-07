package es.virtualclubs.presentation.screens.auth

import android.content.Context
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.fakes.FakeAuthRepository
import es.virtualclubs.fakes.FakeRefreshRepository
import es.virtualclubs.presentation.managers.GlobalUIManager
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var authRepository: FakeAuthRepository
    private lateinit var refreshRepository: FakeRefreshRepository
    private lateinit var userPreferences: UserPreferences
    private lateinit var securePreferences: SecureUserPreferences
    private lateinit var safeCall: SafeCall
    private lateinit var globalUIManager: GlobalUIManager

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        authRepository = FakeAuthRepository()
        refreshRepository = FakeRefreshRepository()
        userPreferences = mockk(relaxed = true)
        securePreferences = mockk(relaxed = true)
        safeCall = SafeCall(mockk<ErrorDispatcher>(relaxed = true))

        globalUIManager = mockk(relaxed = true)
        coEvery { globalUIManager.withLoading<Unit>(any()) } coAnswers {
            @Suppress("UNCHECKED_CAST")
            (args[0] as suspend () -> Unit).invoke()
        }

        every { securePreferences.refreshToken } returns flowOf(null)
    }

    private fun buildViewModel(
        hasSession: Boolean = false,
        email: String? = null
    ): AuthViewModel {
        every { securePreferences.refreshToken } returns flowOf(if (hasSession) "stored-refresh-token" else null)
        if (email != null) {
            every { userPreferences.userEmailFlow } returns flowOf(email)
        }
        return AuthViewModel(
            repository = authRepository,
            refreshRepository = refreshRepository,
            userPreferences = userPreferences,
            securePreferences = securePreferences,
            userSession = UserSession(),
            safeCall = safeCall,
            globalUIManager = globalUIManager,
            context = mockk<Context>(relaxed = true)
        )
    }

    // ─── Estado inicial ───────────────────────────────────────────────────────

    @Test
    fun `estado inicial es Idle cuando no hay sesion guardada`() = runTest {
        val vm = buildViewModel(hasSession = false)
        advanceUntilIdle()
        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── loginUser ────────────────────────────────────────────────────────────

    @Test
    fun `loginUser exitoso actualiza estado a Success`() = runTest {
        authRepository.loginResult = Result.success(validTokens)
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.loginUser("user@test.com", "password123")
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `loginUser con credenciales invalidas mantiene estado en Idle`() = runTest {
        authRepository.loginResult = Result.failure(VirtualClubException(ErrorType.INVALID_CREDENTIALS))
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.loginUser("user@test.com", "wrong")
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── autoLogin ────────────────────────────────────────────────────────────

    @Test
    fun `autoLogin con refresh token valido y refresh exitoso actualiza estado a Success`() = runTest {
        refreshRepository.refreshResult = Result.success(Unit)
        val vm = buildViewModel(hasSession = true, email = "user@test.com")
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `autoLogin con refresh token valido pero refresh fallido mantiene estado en Idle`() = runTest {
        refreshRepository.refreshResult = Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))
        val vm = buildViewModel(hasSession = true)
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }
}
