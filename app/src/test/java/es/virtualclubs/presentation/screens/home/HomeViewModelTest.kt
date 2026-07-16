package es.virtualclubs.presentation.screens.home

import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.User
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.usecase.GetSessionStateUseCase
import es.virtualclubs.fakes.FakeUserRepository
import es.virtualclubs.presentation.managers.GlobalUIManager
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeUserRepository
    private lateinit var userSession: UserSession
    private lateinit var safeCall: SafeCall
    private lateinit var globalUIManager: GlobalUIManager

    @Before
    fun setUp() {
        repository = FakeUserRepository()
        userSession = UserSession()
        safeCall = SafeCall(mockk<ErrorDispatcher>(relaxed = true))
        globalUIManager = mockk(relaxed = true)
        // withLoading envuelve la logica real de loadUserInfo: un mock relajado por
        // defecto NO ejecuta el bloque, hay que forzarlo explicitamente.
        coEvery { globalUIManager.withLoading<Unit>(any()) } coAnswers {
            firstArg<suspend () -> Unit>().invoke()
        }
    }

    private fun buildViewModel() = HomeViewModel(
        repository = repository,
        getSessionState = GetSessionStateUseCase(userSession),
        safeCall = safeCall,
        globalUIManager = globalUIManager
    )

    // ─── loadUserInfo (init) ────────────────────────────────────────────────────

    @Test
    fun `estado inicial carga el email del usuario logueado tras exito`() = runTest {
        userSession.login(User(email = "user@test.com"))
        repository.getUserInfoResult = Result.success(User(email = "user@test.com"))

        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals("user@test.com", vm.uiState.value.userEmail)
    }

    @Test
    fun `si getUserInfo falla el userEmail permanece null`() = runTest {
        userSession.login(User(email = "user@test.com"))
        repository.getUserInfoResult = Result.failure(VirtualClubException(ErrorType.INTERNAL_ERROR))

        val vm = buildViewModel()
        advanceUntilIdle()

        assertNull(vm.uiState.value.userEmail)
    }

    @Test
    fun `si la sesion no es LoggedIn userEmail permanece null aunque getUserInfo tenga exito`() = runTest {
        repository.getUserInfoResult = Result.success(User(email = "user@test.com"))

        val vm = buildViewModel()
        advanceUntilIdle()

        assertNull(vm.uiState.value.userEmail)
    }
}
