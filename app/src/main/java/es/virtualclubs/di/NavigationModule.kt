package es.virtualclubs.di

import es.virtualclubs.presentation.navigation.AppNavigator
import es.virtualclubs.presentation.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindAppNavigator(
        impl: AppNavigatorImpl
    ): AppNavigator
}