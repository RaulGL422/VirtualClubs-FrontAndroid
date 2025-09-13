package es.virtualclubs.di

import es.virtualclubs.presentation.navigation.AppNavigator
import es.virtualclubs.presentation.navigation.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigatorImpl {
        return AppNavigatorImpl()
    }
}