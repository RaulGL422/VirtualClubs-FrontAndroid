# CHANGELOG — Virtual Clubs Android

Todas las versiones notables del proyecto. Formato basado en [Keep a Changelog](https://keepachangelog.com/es/1.0.0/).

---

## [Unreleased]

---

## [0.1.2] — Alpha — 2026-04-08

### Implementado
- VC-58: Implementar VerifyEmailResultPage y ViewModel completos (🛠️ Funcionalidad)
- VC-59: Reescribir SettingsPage y SettingsViewModel desde cero para VirtualClubs (✏️ Diseño)
- VC-60: Implementar flujo completo de getUserInfo con modelo de usuario real (🛠️ Funcionalidad)
- VC-62: Refactorizar UserSession para exponer estado reactivo con StateFlow (🛠️ Funcionalidad)
- VC-63: Reemplazar runBlocking en NetworkModule por caché de token en memoria (📱 Android)
- VC-66: Añadir certificate pinning en OkHttp para la API de producción (🔒 Autenticación)
- VC-67: Crear suite de tests unitarios base para ViewModels y UseCases (🔎 Testing)
- VC-68: Implementar pantalla Home con contenido real de clubes del usuario (🛠️ Funcionalidad)
- VC-71: Verificar expiración JWT localmente en AuthInterceptor antes de la request (🛠️ Funcionalidad)
- VC-76: Migrar Google Sign-In a CredentialManager (eliminar legacy play-services-auth) (📱 Android)
- VC-81: Usar campo email de RegisterResponse para poblar UserSession (🛠️ Funcionalidad)
- VC-85: Configurar ProGuard/R8 para build release (📱 Android)

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
