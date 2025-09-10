package es.virtualclubs.presentation.navigation

interface AppNavigator {
    fun navigateToLoginAndClearStack()
    fun navigateToHome()
    fun navigateBack()
    fun navigateToSettings()
}