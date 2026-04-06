package es.virtualclubs.data.managers

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.domain.repository.RefreshRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SafeResponseTest {

    private lateinit var refreshRepository: RefreshRepository
    private lateinit var safeResponse: SafeResponse

    @Before
    fun setUp() {
        refreshRepository = mockk()
        safeResponse = SafeResponse(refreshRepository)
    }

    // ─── Happy path ───────────────────────────────────────────────────────────

    @Test
    fun `llamada exitosa devuelve Result success con el dato correcto`() = runTest {
        val result = safeResponse.safeResponse { Result.success("datos") }
        assertTrue(result.isSuccess)
        assertEquals("datos", result.getOrNull())
    }

    // ─── IOException ─────────────────────────────────────────────────────────

    @Test
    fun `IOException devuelve CANT_CONNECT_SERVER`() = runTest {
        val result = safeResponse.safeResponse<String> { throw IOException() }
        assertErrorType(result, ErrorType.CANT_CONNECT_SERVER)
    }

    // ─── HTTP 401 — refresh ───────────────────────────────────────────────────

    @Test
    fun `HTTP 401 dispara refresh y reintenta devolviendo el resultado correcto`() = runTest {
        var intentos = 0
        coEvery { refreshRepository.refresh(true) } returns Result.success(Unit)

        val result = safeResponse.safeResponse {
            intentos++
            if (intentos == 1) throw httpException(401)
            Result.success("reintentado")
        }

        assertTrue(result.isSuccess)
        assertEquals("reintentado", result.getOrNull())
        coVerify(exactly = 1) { refreshRepository.refresh(true) }
    }

    @Test
    fun `HTTP 401 con refresh fallido devuelve INVALID_REFRESH_TOKEN`() = runTest {
        coEvery { refreshRepository.refresh(true) } throws
            VirtualClubException(ErrorType.INVALID_REFRESH_TOKEN)

        val result = safeResponse.safeResponse<String> { throw httpException(401) }
        assertErrorType(result, ErrorType.INVALID_REFRESH_TOKEN)
    }

    // ─── HTTP 4xx — errores de negocio ────────────────────────────────────────

    @Test
    fun `HTTP 403 con body EMAIL_NOT_VERIFIED devuelve EMAIL_NOT_VERIFIED`() = runTest {
        val body = errorJson(ErrorType.EMAIL_NOT_VERIFIED.code)
        val result = safeResponse.safeResponse<String> { throw httpException(403, body) }
        assertErrorType(result, ErrorType.EMAIL_NOT_VERIFIED)
    }

    @Test
    fun `HTTP 409 con body EMAIL_ALREADY_EXISTS devuelve EMAIL_ALREADY_EXISTS`() = runTest {
        val body = errorJson(ErrorType.EMAIL_ALREADY_EXISTS.code)
        val result = safeResponse.safeResponse<String> { throw httpException(409, body) }
        assertErrorType(result, ErrorType.EMAIL_ALREADY_EXISTS)
    }

    @Test
    fun `HTTP 403 sin body parseable usa fallback y devuelve EMAIL_NOT_VERIFIED`() = runTest {
        val result = safeResponse.safeResponse<String> { throw httpException(403, "") }
        assertErrorType(result, ErrorType.EMAIL_NOT_VERIFIED)
    }

    @Test
    fun `HTTP 404 sin body parseable usa fallback y devuelve USER_NOT_FOUND`() = runTest {
        val result = safeResponse.safeResponse<String> { throw httpException(404, "") }
        assertErrorType(result, ErrorType.USER_NOT_FOUND)
    }

    // ─── HTTP 5xx ────────────────────────────────────────────────────────────

    @Test
    fun `HTTP 500 devuelve INTERNAL_ERROR sin intentar parsear body`() = runTest {
        val result = safeResponse.safeResponse<String> {
            throw httpException(500, "<html>Internal Server Error</html>")
        }
        assertErrorType(result, ErrorType.INTERNAL_ERROR)
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private fun httpException(code: Int, body: String = ""): HttpException =
        HttpException(Response.error<Any>(code, body.toResponseBody()))

    private fun errorJson(code: Int): String =
        """{"success":false,"type":"ERROR","message":$code,"data":null}"""

    private fun assertErrorType(result: Result<*>, expected: ErrorType) {
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue("Expected VirtualClubException", exception is VirtualClubException)
        assertEquals(expected, (exception as VirtualClubException).errorType)
    }
}
