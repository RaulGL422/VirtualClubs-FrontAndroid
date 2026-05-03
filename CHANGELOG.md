# Changelog

All notable changes to VirtualClubs Android. Format based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

---

## [Unreleased]

---

## [0.1.2] — Alpha — 2026-04-08

### Added
- Email verification result screen (`VerifyEmailResultPage` + `VerifyEmailResultViewModel`) with deep link handling
- Home screen with real club listing from the authenticated user's data
- Unit test suite for all ViewModels, use cases, and the network layer
  - Fakes for repositories (not mocks) for more reliable and refactor-proof tests
  - `MainDispatcherRule` for coroutine-based tests
- Certificate pinning in OkHttp for the production flavor — prevents MITM attacks
- Local JWT expiration check in `AuthInterceptor` before every request — avoids unnecessary 401 round-trips
- `ErrorDispatcher` interface in the domain layer — use cases dispatch errors without coupling to UI
- Debug-only server URL switcher in Settings — configure any local backend IP at runtime without recompiling

### Changed
- `SettingsPage` and `SettingsViewModel` fully rewritten with theme, contrast, font size, email display, and logout
- `getUserInfo` flow redesigned with a real User domain model populated from the API response
- `UserSession` refactored to expose reactive state via `StateFlow`
- `runBlocking` in `NetworkModule` replaced with in-memory token cache in `UserSession` — eliminates blocking on every request
- Google Sign-In migrated from legacy `play-services-auth` to `CredentialManager` (modern API)
- `RegisterResponse` now provides the email field to populate `UserSession` directly
- ProGuard / R8 enabled and configured for release builds (`isMinifyEnabled = true`, `isShrinkResources = true`)

---

## [0.1.1] — Alpha — 2025-12-26

### Added
- Complete authentication system
  - Email/password login and registration
  - Google Sign-In (now migrated to CredentialManager — see 0.1.2)
  - Logout with full token and session cleanup
  - Password reset via email + deep link
  - Email verification via deep link
  - Auto-login on startup using refresh token
- Secure token storage — AES/GCM encryption with AndroidKeyStore
- Jetpack Compose Navigation with deep links
- Dynamic theme (light / dark / high-contrast) with Material 3
- DataStore for user and app preferences
- Clean Architecture + MVVM + Hilt project structure
- `GlobalUIManager` for global UI state (loading, errors, dialogs)
- Build flavors `dev` / `prod` with `BuildConfig` fields

---

## [0.1.0] — Alpha — 2025-06-30

### Added
- Initial Android project setup
- Clean Architecture package structure
- Hilt DI integration
- Retrofit + OkHttp with `AuthInterceptor`
- Material 3 base theme
