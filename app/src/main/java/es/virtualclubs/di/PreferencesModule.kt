package es.virtualclubs.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.virtualclubs.data.local.datastore.AppPreferences
import es.virtualclubs.data.local.datastore.UserPreferences
import es.virtualclubs.data.local.secure.SecureUserPreferences
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    @Named("user_prefs")
    fun provideUserDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.dataStoreFile("user_prefs.preferences_pb")
        }
    }

    @Provides
    @Singleton
    @Named("app_prefs")
    fun provideAppDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.dataStoreFile("app_prefs.preferences_pb")
        }
    }

    @Provides
    @Singleton
    fun provideUserPreferences(
        @Named("user_prefs") dataStore: DataStore<Preferences>
    ): UserPreferences {
        return UserPreferences(dataStore)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(
        @Named("app_prefs") dataStore: DataStore<Preferences>
    ): AppPreferences {
        return AppPreferences(dataStore)
    }

    @Provides
    @Singleton
    fun provideSecurePreferences(
        @ApplicationContext context: Context
    ): SecureUserPreferences {
        return SecureUserPreferences(context)
    }
}