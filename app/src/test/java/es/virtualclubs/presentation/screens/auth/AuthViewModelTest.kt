package es.virtualclubs.presentation.screens.auth

import android.content.Context
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.AuthTokens
import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.usecase.AuthUseCase
import es.virtualclubs.domain.usecase.GoogleUseCase
import es.virtualclubs.domain.usecase.RefreshTokenUseCase
import es.virtualclubs.domain.usecase.RegisterUseCase
import es.virtualclubs.domain.usecase.RequestPasswordResetUseCase
import es.virtualclubs.domain.usecase.token.GetRefreshTokenUseCase
import es.virtualclubs.domain.usecase.token.SaveTokensUseCase
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

    private lateinit var authUseCase: AuthUseCase
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var googleUseCase: GoogleUseCase
    private lateinit var refreshTokenUseCase: RefreshTokenUseCase
    private lateinit var requestPasswordResetUseCase: RequestPasswordResetUseCase
    private lateinit var saveTokensUseCase: SaveTokensUseCase
    private lateinit var getRefreshTokenUseCase: GetRefreshTokenUseCase
    private lateinit var userPreferences: UserPreferences
    private lateinit var userSession: UserSession
    private lateinit var safeCall: SafeCall
    private lateinit var globalUIManager: GlobalUIManager

    private val validTokens = AuthTokens(
        accessToken = "access-token-test",
        refreshToken = "refresh-token-test"
    )

    @Before
    fun setUp() {
        authUseCase = mockk()
        registerUseCase = mockk()
        googleUseCase = mockk()
        refreshTokenUseCase = mockk()
        requestPasswordResetUseCase = mockk()
        saveTokensUseCase = mockk(relaxed = true)
        getRefreshTokenUseCase = mockk()
        userPreferences = mockk(relaxed = true)
        userSession = UserSession()
        safeCall = SafeCall(mockk<ErrorDispatcher>(relaxed = true))
        globalUIManager = mockk(relaxed = true)

        coEvery { globalUIManager.withLoading<Any?>(any()) } coAnswers {
            @Suppress("UNCHECKED_CAST")
            (args[0] as suspend () -> Any?).invoke()
        }

        // Sin sesión previa por defecto
        coEvery { getRefreshTokenUseCase() } returns null
        every { userPreferences.userEmailFlow } returns flowOf(null)
    }

    private fun buildViewModel() = AuthViewModel(
        authUseCase = authUseCase,
        registerUseCase = registerUseCase,
        googleUseCase = googleUseCase,
        refreshTokenUseCase = refreshTokenUseCase,
        requestPasswordResetUseCase = requestPasswordResetUseCase,
        saveTokensUseCase = saveTokensUseCase,
        getRefreshTokenUseCase = getRefreshTokenUseCase,
        userPreferences = userPreferences,
        userSession = userSession,
        safeCall = safeCall,
        globalUIManager = globalUIManager,
        context = mockk<Context>(relaxed = true)
    )

    // ─── Estado inicial ───────────────────────────────────────────────────────

    @Test
    fun `estado inicial es Idle cuando no hay sesion guardada`() = runTest {
        val vm = buildViewModel()
        advanceUntilIdle()
        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── loginUser ────────────────────────────────────────────────────────────

    @Test
    fun `loginUser exitoso actualiza estado a Success`() = runTest {
        coEvery { authUseCase(any(), any()) } returns Result.success(validTokens)
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.loginUser("user@test.com", "password123")
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `loginUser con credenciales invalidas mantiene estado en Idle`() = runTest {
        coEvery { authUseCase(any(), any()) } returns Result.failure(VirtualClubException(ErrorType.INVALID_CREDENTIALS))
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.loginUser("user@test.com", "wrong")
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── autoLogin ────────────────────────────────────────────────────────────

    @Test
    fun `autoLogin con refresh token valido y refresh exitoso actualiza estado a Success`() = runTest {
        coEvery { getRefreshTokenUseCase() } returns "stored-refresh-token"
        coEvery { refreshTokenUseCase() } returns Result.success(Unit)
        every { userPreferences.userEmailFlow } returns flowOf("user@test.com")
        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `autoLogin con refresh token valido pero refresh fallido mantiene estado en Idle`() = runTest {
        coEvery { getRefreshTokenUseCase() } returns "stored-refresh-token"
        coEvery { refreshTokenUseCase() } returns Result.failure(VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN))
        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── registerUser ────────────────────────────────────────────────────────

    @Test
    fun `registerUser exitoso actualiza estado a Success`() = runTest {
        coEvery { registerUseCase(any(), any()) } returns Result.success(validTokens)
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.registerUser("user@test.com", "Pass123!", "Pass123!")
        advanceUntilIdle()

        assertEquals(AuthUiState.Success, vm.uiState.value)
    }

    @Test
    fun `registerUser con contrasenas distintas mantiene estado en Idle`() = runTest {
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.registerUser("user@test.com", "Pass123!", "Diferente!")
        advanceUntilIdle()

        assertEquals(AuthUiState.Idle, vm.uiState.value)
    }

    // ─── requestPasswordReset ────────────────────────────────────────────────

    @Test
    fun `requestPasswordReset exitoso actualiza passwordResetUiState a Success`() = runTest {
        coEvery { requestPasswordResetUseCase(any()) } returns Result.success(Unit)
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.requestPasswordReset("user@test.com")
        advanceUntilIdle()

        assertEquals(PasswordResetUiState.Success, vm.passwordResetUiState.value)
    }

    @Test
    fun `requestPasswordReset fallido mantiene passwordResetUiState en Idle`() = runTest {
        coEvery { requestPasswordResetUseCase(any()) } returns Result.failure(VirtualClubException(ErrorType.USER_NOT_FOUND))
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.requestPasswordReset("noexiste@test.com")
        advanceUntilIdle()

        assertEquals(PasswordResetUiState.Idle, vm.passwordResetUiState.value)
    }
}
