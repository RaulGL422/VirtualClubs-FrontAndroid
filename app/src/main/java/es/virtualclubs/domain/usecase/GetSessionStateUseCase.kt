package es.virtualclubs.domain.usecase

import es.virtualclubs.data.session.UserSession
import es.virtualclubs.domain.model.SessionState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSessionStateUseCase @Inject constructor(
    private val userSession: UserSession
) {
    operator fun invoke(): Flow<SessionState> = userSession.sessionState
}
