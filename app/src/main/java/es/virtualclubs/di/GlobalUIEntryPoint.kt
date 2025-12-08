package es.virtualclubs.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.domain.repository.AuthRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface GlobalUIEntryPoint {
  fun authRepository(): AuthRepository
}
