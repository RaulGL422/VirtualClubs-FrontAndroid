package es.virtualclubs.data.remote.api

/**
 * Fusionada en [AuthApi]. Conservada para compatibilidad con el grafo de Hilt
 * hasta que se limpie [NetworkModule].
 *
 * @deprecated Usar [AuthApi.refresh] directamente.
 */
@Deprecated("Usar AuthApi.refresh")
interface RefreshApi
