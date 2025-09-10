package es.virtualclubs.domain.model

sealed class AppError : Exception() {
    object Network : AppError() // no hay conexión
    object Unauthorized : AppError() // 401 → token inválido
    object Forbidden : AppError() // 403 → sin permisos
    object NotFound : AppError() // 404
    object Server : AppError() // 500
    object Unknown : AppError() // error no identificado

    data class Validation(val reason: String) : AppError() // 400 con mensaje del backend
}