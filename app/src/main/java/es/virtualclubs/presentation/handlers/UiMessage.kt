package es.virtualclubs.presentation.handlers

sealed class UiMessage {
    data class Error(val messageKey: Int) : UiMessage()
    data class Notification(val messageKey: Int) : UiMessage()
    object None : UiMessage()
}