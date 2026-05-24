package es.virtualclubs.presentation.managers

import es.virtualclubs.domain.usecase.LogoutUserUseCase
import es.virtualclubs.presentation.navigation.AppNavigator
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SessionManagerTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var logoutUserUseCase: LogoutUserUseCase
    private lateinit var appNavigator: AppNavigator
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        logoutUserUseCase = mockk()
        appNavigator = mockk(relaxed = true)
        coEvery { logoutUserUseCase(notifyBackend = false) } just runs
        sessionManager = SessionManager(logoutUserUseCase, appNavigator)
    }

    @Test
    fun `logout llama a logoutUserUseCase sin notificar al backend`() = runTest {
        sessionManager.logout()

        coVerify { logoutUserUseCase(notifyBackend = false) }
    }

    @Test
    fun `logout navega a login tras limpiar sesion`() = runTest {
        sessionManager.logout()

        coVerify { appNavigator.navigateToLoginAndClearStack() }
    }

    @Test
    fun `logout limpia la sesion antes de navegar`() = runTest {
        sessionManager.logout()

        coVerifyOrder {
            logoutUserUseCase(notifyBackend = false)
            appNavigator.navigateToLoginAndClearStack()
        }
    }
}
