package es.virtualclubs.session

import es.virtualclubs.data.models.User
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class UserSession @Inject constructor() {
  private val _currentUser = MutableStateFlow(User())
  val currentUser: StateFlow<User> = _currentUser.asStateFlow()

  fun updateUser(user: User) {
    _currentUser.value = user
  }

  fun clearUser() {
    _currentUser.value = User()
  }
}
