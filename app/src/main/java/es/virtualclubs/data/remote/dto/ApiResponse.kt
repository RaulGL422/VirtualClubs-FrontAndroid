package es.virtualclubs.data.remote.dto

data class ApiResponse<T>(
    val success: Boolean,
    val type: ResponseType,
    val message: String,
    val data: T? = null
)
