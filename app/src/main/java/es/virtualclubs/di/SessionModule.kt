package es.virtualclubs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.data.session.UserSession
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
  @Provides
  @Singleton
  fun provideUserSession(): UserSession = UserSession()
}