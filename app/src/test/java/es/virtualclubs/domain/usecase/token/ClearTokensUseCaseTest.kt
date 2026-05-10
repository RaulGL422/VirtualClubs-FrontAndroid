package es.virtualclubs.domain.usecase.token

import es.virtualclubs.data.local.secure.SecureUserPreferences
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearTokensUseCaseTest {

    private lateinit var securePrefs: SecureUserPreferences
    private lateinit var useCase: ClearTokensUseCase

    @Before
    fun setUp() {
        securePrefs = mockk(relaxed = true)
        useCase = ClearTokensUseCase(securePrefs)
    }

    @Test
    fun `limpia todos los datos en SecureUserPreferences`() = runTest {
        useCase()

        coVerify(exactly = 1) { securePrefs.clearAll() }
    }

    @Test
    fun `es idempotente si se llama varias veces`() = runTest {
        useCase()
        useCase()

        coVerify(exactly = 2) { securePrefs.clearAll() }
    }
}
