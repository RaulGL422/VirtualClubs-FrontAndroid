package es.virtualclubs.presentation.navigation

interface AppNavigator {
    fun navigateToLoginAndClearStack()
    fun navigateToHome()
    fun navigateBack()
    fun navigateToSettings()
    fun navigateToResetPassword(token: String)
    fun navigateToVerifyEmail(token: String)
}