package es.virtualclubs.presentation.screens.verifyemailresult

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Test

class VerifyEmailResultViewModelTest {

    @Test
    fun `estado inicial es Success cuando status es success`() {
        val vm = VerifyEmailResultViewModel(SavedStateHandle(mapOf("status" to "success")))

        assertEquals(VerifyEmailResultUiState.Success, vm.uiState.value)
    }

    @Test
    fun `estado inicial es Error cuando status es distinto de success`() {
        val vm = VerifyEmailResultViewModel(SavedStateHandle(mapOf("status" to "expired")))

        assertEquals(VerifyEmailResultUiState.Error, vm.uiState.value)
    }

    @Test
    fun `estado inicial es Error cuando status no esta presente en el SavedStateHandle`() {
        val vm = VerifyEmailResultViewModel(SavedStateHandle())

        assertEquals(VerifyEmailResultUiState.Error, vm.uiState.value)
    }
}
