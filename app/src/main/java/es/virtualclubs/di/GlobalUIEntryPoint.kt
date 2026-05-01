package es.virtualclubs.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.presentation.managers.GlobalUIManager
import es.virtualclubs.presentation.navigation.AppNavigator

/**
 * Entry point para acceder a los singletons de presentación desde
 * contextos no-inyectables (composables en [VirtualClubsMainApp]).
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface GlobalUIEntryPoint {
    fun globalUIManager(): GlobalUIManager
    fun appNavigator(): AppNavigator
}
