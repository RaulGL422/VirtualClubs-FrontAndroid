package es.virtualclubs.domain.usecase

import es.virtualclubs.domain.model.User
import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException
import es.virtualclubs.fakes.FakeUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUserInfoUseCaseTest {

    private lateinit var repository: FakeUserRepository
    private lateinit var userSession: UserSession
    private lateinit var useCase: GetUserInfoUseCase

    @Before
    fun setUp() {
        repository = FakeUserRepository()
        userSession = UserSession()
        useCase = GetUserInfoUseCase(repository, userSession)
    }

    @Test
    fun `info de usuario exitosa devuelve Result success`() = runTest {
        val user = User(email = "user@test.com")
        repository.getUserInfoResult = Result.success(user)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `info de usuario exitosa actualiza UserSession`() = runTest {
        val user = User(email = "user@test.com")
        repository.getUserInfoResult = Result.success(user)

        useCase()

        assertEquals(user, userSession.currentUser.value)
    }

    @Test
    fun `fallo al obtener info no actualiza UserSession`() = runTest {
        repository.getUserInfoResult = Result.failure(VirtualClubException(ErrorType.INTERNAL_ERROR))
        userSession.updateUser(User(email = "previous@test.com"))

        useCase()

        // UserSession no debe cambiar si hay error
        assertEquals(User(email = "previous@test.com"), userSession.currentUser.value)
    }

    @Test
    fun `fallo al obtener info devuelve Result failure`() = runTest {
        repository.getUserInfoResult = Result.failure(VirtualClubException(ErrorType.USER_NOT_FOUND))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(ErrorType.USER_NOT_FOUND, (result.exceptionOrNull() as VirtualClubException).errorType)
    }

    @Test
    fun `llama al repositorio exactamente una vez`() = runTest {
        useCase()

        assertEquals(1, repository.getUserInfoCallCount)
    }

    @Test
    fun `usuario sin email actualiza UserSession con email nulo`() = runTest {
        val userWithoutEmail = User(email = null)
        repository.getUserInfoResult = Result.success(userWithoutEmail)

        useCase()

        assertNull(userSession.currentUser.value.email)
    }
}
