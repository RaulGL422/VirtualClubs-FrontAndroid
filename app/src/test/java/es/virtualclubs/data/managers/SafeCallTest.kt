package es.virtualclubs.data.managers

import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SafeCallTest {

    private lateinit var errorDispatcher: ErrorDispatcher
    private lateinit var safeCall: SafeCall

    @Before
    fun setUp() {
        errorDispatcher = mockk(relaxed = true)
        safeCall = SafeCall(errorDispatcher)
    }

    // ─── Happy path ───────────────────────────────────────────────────────────

    @Test
    fun `llamada exitosa devuelve Result success sin notificar ErrorDispatcher`() = runTest {
        val result = safeCall.safeCall { Result.success("datos") }

        assertTrue(result.isSuccess)
        assertEquals("datos", result.getOrNull())
        verify(exactly = 0) { errorDispatcher.handleError(any()) }
    }

    // ─── Propagación de errores ───────────────────────────────────────────────

    @Test
    fun `llamada con fallo notifica al ErrorDispatcher`() = runTest {
        val exception = VirtualClubException(ErrorType.INTERNAL_ERROR)

        safeCall.safeCall<String> { Result.failure(exception) }

        verify(exactly = 1) { errorDispatcher.handleError(exception) }
    }

    @Test
    fun `llamada con fallo devuelve el mismo Result failure`() = runTest {
        val exception = VirtualClubException(ErrorType.INVALID_CREDENTIALS)

        val result = safeCall.safeCall<String> { Result.failure(exception) }

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `fallo con excepcion generica notifica al ErrorDispatcher con esa excepcion`() = runTest {
        val exception = RuntimeException("algo fallo")

        safeCall.safeCall<String> { Result.failure(exception) }

        verify(exactly = 1) { errorDispatcher.handleError(exception) }
    }

    // ─── Múltiples llamadas ───────────────────────────────────────────────────

    @Test
    fun `multiples fallos notifican al ErrorDispatcher en cada llamada`() = runTest {
        val exception = VirtualClubException(ErrorType.CANT_CONNECT_SERVER)

        safeCall.safeCall<String> { Result.failure(exception) }
        safeCall.safeCall<String> { Result.failure(exception) }

        verify(exactly = 2) { errorDispatcher.handleError(exception) }
    }

    @Test
    fun `exito seguido de fallo notifica al ErrorDispatcher solo una vez`() = runTest {
        val exception = VirtualClubException(ErrorType.USER_NOT_FOUND)

        safeCall.safeCall { Result.success("ok") }
        safeCall.safeCall<String> { Result.failure(exception) }

        verify(exactly = 1) { errorDispatcher.handleError(any()) }
    }
}
