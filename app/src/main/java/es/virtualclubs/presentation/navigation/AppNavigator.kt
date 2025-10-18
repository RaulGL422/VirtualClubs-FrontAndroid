package es.virtualclubs.presentation.navigation

interface AppNavigator {
    fun navigateToLoginAndClearStack()
    fun navigateToLoginAndClearStackWithMessage(message: String)
    fun navigateToHome()
    fun navigateBack()
    fun navigateToSettings()
    fun navigateToResetPassword(token: String)
    fun navigateToVerifyEmail(token: String)
}