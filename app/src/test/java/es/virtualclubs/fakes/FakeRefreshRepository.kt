package es.virtualclubs.fakes

import es.virtualclubs.domain.repository.RefreshRepository

/**
 * Implementación fake de [RefreshRepository] para tests unitarios.
 */
class FakeRefreshRepository : RefreshRepository {

    var refreshResult: Result<Unit> = Result.success(Unit)
    var refreshCallCount = 0

    override suspend fun refresh(): Result<Unit> {
        refreshCallCount++
        return refreshResult
    }
}
