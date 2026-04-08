package es.virtualclubs.domain.usecase

import es.virtualclubs.data.models.User
import es.virtualclubs.domain.repository.UserRepository
import es.virtualclubs.session.UserSession
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
  private val userRepository: UserRepository,
  private val userSession: UserSession
) {
  suspend operator fun invoke(): Result<User> {
    return userRepository.getUserInfo().also { result ->
      result.getOrNull()?.let { user ->
        userSession.currentUser = user
      }
    }
  }
}
