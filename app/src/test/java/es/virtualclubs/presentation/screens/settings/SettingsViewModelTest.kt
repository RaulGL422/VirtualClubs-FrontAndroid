package es.virtualclubs.presentation.screens.settings

import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.User
import es.virtualclubs.domain.usecase.GetSessionStateUseCase
import es.virtualclubs.utils.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var appPreferences: AppPreferences
    private lateinit var userSession: UserSession

    @Before
    fun setUp() {
        appPreferences = mockk(relaxed = true)
        userSession = UserSession()
        every { appPreferences.darkThemeFlow } returns flowOf(null)
        every { appPreferences.contrastTypeFlow } returns flowOf(0)
        every { appPreferences.fontSizeMultiplierFlow } returns flowOf(1.0)
        every { appPreferences.notificationsEnabledFlow } returns flowOf(true)
        every { appPreferences.appLanguageFlow } returns flowOf("en")
        every { appPreferences.debugServerUrlFlow } returns flowOf("")
    }

    private fun buildViewModel() = SettingsViewModel(
        appPreferences = appPreferences,
        getSessionState = GetSessionStateUseCase(userSession)
    )

    // ─── Estado inicial ───────────────────────────────────────────────────────

    @Test
    fun `estado inicial combina preferencias y sesion correctamente`() = runTest {
        every { appPreferences.darkThemeFlow } returns flowOf(true)
        every { appPreferences.contrastTypeFlow } returns flowOf(2)
        every { appPreferences.fontSizeMultiplierFlow } returns flowOf(1.2)
        every { appPreferences.notificationsEnabledFlow } returns flowOf(false)
        every { appPreferences.appLanguageFlow } returns flowOf("es")
        every { appPreferences.debugServerUrlFlow } returns flowOf("http://debug")

        val vm = buildViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(true, state.isDarkTheme)
        assertEquals(2, state.contrastType)
        assertEquals(1.2, state.fontSizeMultiplier, 0.0)
        assertEquals(false, state.notificationsEnabled)
        assertEquals("es", state.appLanguage)
        assertEquals("http://debug", state.debugServerUrl)
    }

    @Test
    fun `email refleja el usuario logueado, null si LoggedOut`() = runTest {
        val vm = buildViewModel()
        advanceUntilIdle()
        assertNull(vm.uiState.value.email)

        userSession.login(User(email = "user@test.com"))
        advanceUntilIdle()

        assertEquals("user@test.com", vm.uiState.value.email)
    }

    @Test
    fun `isLoggedIn es true solo si hay email`() = runTest {
        val vm = buildViewModel()
        advanceUntilIdle()
        assertFalse(vm.uiState.value.isLoggedIn)

        userSession.login(User(email = "user@test.com"))
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isLoggedIn)
    }

    // ─── Setters ──────────────────────────────────────────────────────────────

    @Test
    fun `los setters delegan en appPreferences con el valor correcto`() = runTest {
        val vm = buildViewModel()
        advanceUntilIdle()

        vm.setTheme(true)
        vm.setContrast(3)
        vm.setFontSize(1.5)
        vm.setNotificationsEnabled(false)
        vm.setAppLanguage("fr")
        advanceUntilIdle()

        coVerify { appPreferences.saveThemeStyle(true) }
        coVerify { appPreferences.saveContrastType(3) }
        coVerify { appPreferences.saveFontSizeMultiplier(1.5) }
        coVerify { appPreferences.setNotificationsEnabled(false) }
        coVerify { appPreferences.setAppLanguage("fr") }
    }

    // ─── saveDebugServerUrl ───────────────────────────────────────────────────

    @Test
    fun `saveDebugServerUrl hace trim y emite restartSignal`() = runTest {
        val vm = buildViewModel()
        advanceUntilIdle()
        var restartReceived = false
        val collector = launch { vm.restartSignal.collect { restartReceived = true } }

        vm.saveDebugServerUrl("  http://debug.test  ")
        advanceUntilIdle()

        coVerify { appPreferences.setDebugServerUrl("http://debug.test") }
        assertTrue(restartReceived)
        collector.cancel()
    }
}
