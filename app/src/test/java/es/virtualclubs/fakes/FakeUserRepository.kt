package es.virtualclubs.fakes

import es.virtualclubs.domain.model.User
import es.virtualclubs.domain.repository.UserRepository

/**
 * Implementación fake de [UserRepository] para tests unitarios.
 */
class FakeUserRepository : UserRepository {

    var getUserInfoResult: Result<User> = Result.success(User(email = "test@test.com"))

    var getUserInfoCallCount = 0

    override suspend fun getUserInfo(): Result<User> {
        getUserInfoCallCount++
        return getUserInfoResult
    }
}
