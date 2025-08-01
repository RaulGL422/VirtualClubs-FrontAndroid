package es.virtualclubs.data.remote.dto

enum class ResponseType {
    NONE,
    ERROR;

    companion object {
        fun from(value: String): ResponseType {
            return try {
                valueOf(value.uppercase())
            } catch (e: Exception) {
                ERROR
            }
        }
    }
}