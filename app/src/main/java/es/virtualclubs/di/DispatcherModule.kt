package es.virtualclubs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.domain.model.ErrorDispatcher
import es.virtualclubs.presentation.managers.GlobalUIManager
import javax.inject.Singleton

/**
 * Vincula [GlobalUIManager] como implementación de [ErrorDispatcher] en el grafo de Hilt.
 * Permite que [SafeCall] (capa de datos) notifique errores sin depender directamente
 * de la capa de presentación.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Singleton
    fun provideErrorDispatcher(): ErrorDispatcher = GlobalUIManager
}
