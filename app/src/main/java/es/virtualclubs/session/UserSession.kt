package es.virtualclubs.session

import es.virtualclubs.data.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSession @Inject constructor() {
  var currentUser: User = User()

  @Volatile var cachedAccessToken: String? = null
    private set

  fun cacheAccessToken(token: String?) {
    cachedAccessToken = token
  }
}
