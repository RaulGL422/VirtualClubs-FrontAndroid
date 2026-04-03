# CHANGELOG — Virtual Clubs Android

Todas las versiones notables del proyecto. Formato basado en [Keep a Changelog](https://keepachangelog.com/es/1.0.0/).

---

## [Unreleased]

### Por implementar
- Pantalla VerifyEmailResult completa
- Pantalla Settings reescrita desde cero
- Flujo completo getUserInfo (UserRepository → UserSession → HomeViewModel)
- Tests unitarios base (ViewModels, UseCases, SafeResponse)
- ProGuard/R8 configurado para release
- Certificate pinning en OkHttp

### Bugs conocidos
- `userSession.currentUser.copy()` no asigna — email nunca actualizado en sesión
- `SafeResponse` descarta resultado real de `block()` tras refresh token exitoso
- `NavGraph` crash: `getBoolean("status")` en parámetro declarado `StringType`
- Imports `jakarta.inject` en lugar de `javax.inject` en 4 archivos
- `SafeCall.exceptionOrNull()!!` NPE potencial

---

## [0.1.1] — Alpha — 2025

### Implementado
- Sistema de autenticación completo
  - Login con email/contraseña
  - Registro con email/contraseña
  - Google Sign-In (legacy API)
  - Logout con limpieza de tokens
  - Solicitud de reset de contraseña por email
  - Reset de contraseña con token via deep link
  - Verificación de email via deep link (resultado pendiente)
  - Auto-login con refresh token al arrancar
- Almacenamiento seguro de tokens (AES/GCM con AndroidKeyStore)
- Navegación con Jetpack Compose Navigation + deep links
- Tema dinámico (light/dark/contrast) con Material 3
- DataStore para preferencias de usuario y app
- Estructura Clean Architecture + MVVM + Hilt completa
- GlobalUIManager para estado global de UI (loading, errores, diálogos)
- Build flavors dev/prod con BuildConfig
- Dos Activities adicionales para deep links (ResetPassword, VerifyEmail)

---

## [0.1.0] — Alpha — Initial Setup

### Implementado
- Setup inicial del proyecto Android
- Estructura de paquetes Clean Architecture
- Integración Hilt DI
- Retrofit + OkHttp con AuthInterceptor
- Material 3 theme base
