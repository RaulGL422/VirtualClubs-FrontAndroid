package es.virtualclubs.presentation.screens.resetPassword

import es.virtualclubs.data.managers.SafeCall
import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.fakes.FakeAuthRepository
import es.virtualclubs.presentation.managers.GlobalUIManager
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResetPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeAuthRepository
    private lateinit var safeCall: SafeCall
    private lateinit var globalUIManager: GlobalUIManager

    @Before
    fun setUp() {
        repository = FakeAuthRepository()
        safeCall = SafeCall(mockk<ErrorDispatcher>(relaxed = true))
        globalUIManager = mockk(relaxed = true)
    }

    private fun buildViewModel() = ResetPasswordViewModel(
        repository = repository,
        safeCall = safeCall,
        globalUIManager = globalUIManager
    )

    // ─── resetPassword ──────────────────────────────────────────────────────────

    @Test
    fun `resetPassword exitoso actualiza estado a Success`() = runTest {
        repository.resetPasswordResult = Result.success(Unit)
        val vm = buildViewModel()

        vm.resetPassword("token-valido", "Pass123!", "Pass123!")
        advanceUntilIdle()

        assertEquals(ResetPasswordUiState.Success, vm.uiState.value)
    }

    @Test
    fun `resetPassword con contrasenas distintas mantiene estado en Idle y no llama al repositorio`() = runTest {
        val vm = buildViewModel()

        vm.resetPassword("token-valido", "Pass123!", "Diferente!")
        advanceUntilIdle()

        assertEquals(ResetPasswordUiState.Idle, vm.uiState.value)
        assertNull(repository.lastResetPasswordToken)
    }

    @Test
    fun `resetPassword con contrasenas distintas notifica error PASSWORD_NOT_EQUALS al globalUIManager`() = runTest {
        val vm = buildViewModel()

        vm.resetPassword("token-valido", "Pass123!", "Diferente!")
        advanceUntilIdle()

        verify { globalUIManager.setError(ErrorType.PASSWORD_NOT_EQUALS) }
    }

    @Test
    fun `resetPassword fallido en el repositorio mantiene estado en Idle`() = runTest {
        repository.resetPasswordResult = Result.failure(RuntimeException("network error"))
        val vm = buildViewModel()

        vm.resetPassword("token-invalido", "Pass123!", "Pass123!")
        advanceUntilIdle()

        assertEquals(ResetPasswordUiState.Idle, vm.uiState.value)
    }
}
