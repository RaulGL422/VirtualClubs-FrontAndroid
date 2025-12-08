package es.virtualclubs

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    appContext = this
  }

  companion object {
    lateinit var appContext: Context
      private set
  }
}