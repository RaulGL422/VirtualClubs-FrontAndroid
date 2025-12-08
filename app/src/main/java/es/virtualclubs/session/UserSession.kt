package es.virtualclubs.session

import es.virtualclubs.data.models.User
import javax.inject.Inject

class UserSession @Inject constructor() {
  var currentUser: User = User()
}