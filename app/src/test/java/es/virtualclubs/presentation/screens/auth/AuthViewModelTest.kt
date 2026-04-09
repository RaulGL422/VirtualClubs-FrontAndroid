package es.virtualclubs.presentation.screens.auth

import android.content.Context
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import es.virtualclubs.data.managers.GlobalUIManager
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.AuthRepository
import es.virtualclubs.domain.repository.RefreshRepository
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.Runs
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

    private lateinit var authRepository: AuthRepository
    private lateinit var refreshRepository: RefreshRepository
    private lateinit var userPreferences: UserPreferences
    private lateinit var securePreferences: SecureUserPreferences

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        authRepository = mockk()
        refreshRepository = mockk()
        userPreferences = mockk(relaxed = true)
        securePreferences = mockk(relaxed = true)

        // Mock GlobalUIManager para evitar dependencia de App.appContext
        mockkObject(GlobalUIManager)
        every { GlobalUIManager.setError(any()) } just Runs
        every { GlobalUIManager.handleError(any()) } just Runs
        coEvery { GlobalUIManager.withLoading<Unit>(any()) } coAnswers {
            @Suppress("UNCHECKED_CAST")
            (args[0] as suspend () -> Unit).invoke()
        }

        // autoLogin desactivado por defecto para que init no dispare refresh
        every { userPreferences.autoLoginFlow } returns flowOf(false)
    }

    private fun buildViewModel(
        autoLogin: Boolean = false,
        email: String? = null
    ): AuthViewModel {
        every { userPreferences.autoLoginFlow } returns flowOf(autoLogin)
        if (email != null) {
            every { userPreferences.userEmailFlow } returns flowOf(email)
        }
        return AuthViewModel(
            repository = authRepository,
            refreshRepository = refreshRepository,
            userPreferences = userPreferences,
            securePreferences = securePreferences,
            userSession = UserSession(),
            context = mockk<Context>(relaxed = true)
        )
    }

    // ─── Estado inicial ───────────────────────────────────────────────────────

    @Test
    fun `estado inicial es Idle cuando autoLogin esta desactivado`() = runTest {
        val vm = buildViewModel(autoLogin = false)
        advanceUntilIdle()
        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── loginUser ────────────────────────────────────────────────────────────

    @Test
    fun `loginUser exitoso actualiza estado a Success`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(validTokens)
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.loginUser("user@test.com", "password123", false)
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `loginUser con credenciales invalidas mantiene estado en Idle`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns
            Result.failure(VirtualClubException(ErrorType.INVALID_CREDENTIALS))
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.loginUser("user@test.com", "wrong", false)
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── autoLogin ────────────────────────────────────────────────────────────

    @Test
    fun `autoLogin activo con refresh exitoso actualiza estado a Success`() = runTest {
        coEvery { refreshRepository.refresh(false) } returns Result.success(Unit)
        val vm = buildViewModel(autoLogin = true, email = "user@test.com")
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `autoLogin activo con refresh fallido mantiene estado en Idle`() = runTest {
        coEvery { refreshRepository.refresh(false) } returns
            Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))
        val vm = buildViewModel(autoLogin = true)
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }
}
