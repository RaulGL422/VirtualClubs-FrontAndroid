package es.virtualclubs.data.remote.dto

import es.virtualclubs.domain.model.ErrorType
import es.virtualclubs.domain.model.VirtualClubException

data class ApiResponse<T>(
    val success: Boolean,
    val type: ResponseType,
    val message: Int,
    val data: T? = null
)

fun <T> ApiResponse<T>.getOrThrow(): T {
    if (!success) throw VirtualClubException(ErrorType.fromCode(message))
    return data ?: throw VirtualClubException(ErrorType.fromCode(message))
}