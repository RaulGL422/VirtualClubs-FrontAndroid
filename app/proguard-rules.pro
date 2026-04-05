# =====================================================================
# VirtualClubs Android — ProGuard/R8 rules
# =====================================================================

# Conservar información de línea para stack traces legibles en producción
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# =====================================================================
# DTOs de red — Gson necesita los nombres reales de los campos
# =====================================================================
-keep class es.virtualclubs.data.remote.dto.** { *; }

# =====================================================================
# Modelos de dominio usados en serialización
# =====================================================================
-keep class es.virtualclubs.domain.model.AuthTokens { *; }
-keep class es.virtualclubs.domain.model.ErrorType { *; }
-keep class es.virtualclubs.domain.model.VirtualClubException { *; }

# Preservar todos los valores de los enums (accedidos por nombre en Gson)
-keepclassmembers enum * { *; }

# =====================================================================
# Hilt — inyección de dependencias
# =====================================================================
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keepclasseswithmembernames class * {
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <init>(...);
}
-keepclasseswithmembernames class * {
    @jakarta.inject.Inject <fields>;
    @jakarta.inject.Inject <init>(...);
}

# =====================================================================
# Retrofit — interfaces de API y anotaciones HTTP
# =====================================================================
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}
-keep interface es.virtualclubs.data.remote.api.** { *; }

# =====================================================================
# Gson — serialización/deserialización JSON
# =====================================================================
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }

# =====================================================================
# OkHttp — cliente HTTP
# =====================================================================
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# =====================================================================
# Kotlin Coroutines
# =====================================================================
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# =====================================================================
# Jetpack DataStore
# =====================================================================
-keep class androidx.datastore.** { *; }

# =====================================================================
# AndroidX Security (EncryptedSharedPreferences / EncryptedFile)
# =====================================================================
-keep class androidx.security.crypto.** { *; }
