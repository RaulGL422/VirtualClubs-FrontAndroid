# CLAUDE.md — VirtualClubs Android

## Project Overview

Android client for **VirtualClubs** — a platform to create and manage sports fan clubs. Users can join clubs by sport, connect with members, organize events, and manage attendance, all from their phone.

- **Platform:** Android (Kotlin + Jetpack Compose)
- **Architecture:** Clean Architecture + MVVM
- **Version:** 0.2.0 Alpha | **AGP:** 9.2.1 | **Kotlin:** 2.3.21
- **Compose BOM:** 2026.05.00 | **Hilt:** 2.59.2
- **Min SDK:** 30 (Android 11) | **Target SDK:** 37 | **Compile SDK:** 37
- **Backend:** https://api-vc.rgal.dev (dev) / https://virtualclubs-backend.onrender.com (prod)

---

## Package Structure

```
es/virtualclubs/
├── App.kt                          HiltAndroidApp entry point
├── ScreenType.kt                   Small/Medium enum for responsive layouts
├── VirtualClubsMainApp.kt          Root composable (theme + NavHost)
│
├── data/                           DATA LAYER
│   ├── local/
│   │   ├── datastore/
│   │   │   ├── AppPreferences.kt   Dark theme, contrast, font size
│   │   │   └── UserPreferences.kt  Email (plain DataStore)
│   │   └── secure/
│   │       ├── EncryptionUtils.kt          AES/GCM for tokens
│   │       └── SecureUserPreferences.kt    Encrypted token storage
│   ├── managers/
│   │   ├── AuthInterceptor.kt      Injects Bearer token + proactive refresh on expiry
│   │   ├── SafeCall.kt             API call wrapper with error dispatching
│   │   └── SafeResponse.kt         Response handler + automatic token refresh
│   ├── remote/
│   │   ├── api/
│   │   │   ├── Endpoint.kt         Constantes de rutas API (/v1/auth/*, /v1/user/*)
│   │   │   ├── AuthApi.kt          Authentication endpoints
│   │   │   ├── RefreshApi.kt       Token refresh endpoint
│   │   │   └── UserApi.kt          User info endpoint
│   │   └── dto/                    Request/response DTOs
│   ├── repository/                 Repository implementations
│   └── session/
│       └── UserSession.kt          sessionState: StateFlow<SessionState> + cachedAccessToken
│
├── di/                             DEPENDENCY INJECTION (Hilt)
│   ├── DispatcherModule.kt         Binds ErrorDispatcher → GlobalUIManager
│   ├── GlobalUIEntryPoint.kt       Hilt EntryPoint for Activities
│   ├── NetworkModule.kt            Retrofit + OkHttp + interceptors
│   └── PreferencesModule.kt        DataStore providers
│
├── domain/                         DOMAIN LAYER
│   ├── model/
│   │   │   ── Entidades de dominio ──────────────────────────────────
│   │   ├── Club.kt                 Club(id, name, sport, memberCount)
│   │   ├── User.kt                 User(email: String?)
│   │   │   ── Autenticación / sesión ──────────────────────────────
│   │   ├── AuthTokens.kt           accessToken + refreshToken (+ email opcional del backend)
│   │   ├── SessionState.kt         sealed: LoggedOut | LoggedIn(user: User)
│   │   │   ── Errores ─────────────────────────────────────────────
│   │   ├── ErrorDispatcher.kt      Interfaz para despachar errores → implementada por GlobalUIManager
│   │   ├── ErrorType.kt            Enum con 27 tipos (códigos 1-13 del backend, 14-27 solo Android)
│   │   └── VirtualClubException.kt Excepción del proyecto que encapsula ErrorType
│   ├── repository/                 Repository interfaces
│   └── usecase/                    Use cases (business logic)
│       ├── AuthUseCase.kt
│       ├── GetSessionStateUseCase.kt   sessionState: Flow<SessionState> — used by ViewModels
│       ├── GetUserInfoUseCase.kt
│       ├── GoogleUseCase.kt
│       ├── LogoutUserUseCase.kt
│       ├── RefreshTokenUseCase.kt
│       ├── RegisterUseCase.kt
│       ├── RequestPasswordResetUseCase.kt
│       └── token/                  GetAccessToken, GetRefreshToken, SaveTokens, ClearTokens
│
├── presentation/                   PRESENTATION LAYER
│   ├── components/                 Reusable composables
│   │   ├── AppBar.kt
│   │   ├── Buttons.kt
│   │   ├── ListItems.kt                VCListItem + VCListToggleItem (icon, title, subtitle, arrow/toggle)
│   │   ├── Scaffold.kt
│   │   ├── TextFields.kt
│   │   └── dialogs/
│   │       ├── VCDialog.kt             Interface for all system dialogs (shown by GlobalUIManager)
│   │       └── EmailNotVerifiedDialog.kt   Global dialog triggered on EMAIL_NOT_VERIFIED error
│   ├── managers/
│   │   ├── ErrorHandler.kt         ErrorType → R.string resource ID mapping
│   │   ├── GlobalUIManager.kt      Loading, errors, dialogs — implements ErrorDispatcher
│   │   └── SessionManager.kt       Clears session data and navigates to login on logout
│   ├── navigation/
│   │   ├── AppNavigator.kt         Static navigation control
│   │   ├── NavGraph.kt             NavHost with all routes
│   │   └── Screen.kt               Sealed class with all routes
│   ├── screens/
│   │   ├── splash/                 Splash + auto-login (SplashPage + SplashViewModel)
│   │   ├── auth/                   Login/Register (AuthPage + AuthViewModel)
│   │   │   ├── components/         AuthDivider, AuthToggle, SocialButtons (internal)
│   │   │   └── dialogs/            ForgotPasswordDialog
│   │   ├── home/                   Main home (HomePage + HomeViewModel)
│   │   ├── resetPassword/          Password reset flow
│   │   ├── settings/               Theme/font/language/notifications settings
│   │   │   ├── components/         AppearanceSection, DebugServerSection, SettingRow, SettingToggleRow (internal)
│   │   │   └── dialogs/            LanguagePickerDialog
│   │   └── verifyemailresult/      Email verification result
│   ├── MainActivity.kt             Entry point, sets up NavController + theme
│   ├── ResetPasswordActivity.kt    Handles reset-password deep link
│   ├── VerifyEmailActivity.kt      Handles email-verification deep link
│   └── theme/
│       ├── Color.kt                Stadium DS — 10-level palette, 6 color schemes
│       ├── Shape.kt
│       ├── Theme.kt                VirtualClubsTheme composable
│       └── Type.kt                 Poppins + Roboto typography
```

---

## Navigation Routes

Sealed class `Screen` — all routes:

| Route | Screen | Deep Link |
|-------|---------|-----------|
| `splash` | Splash / auto-login (**start destination**) | — |
| `auth/{message}` | Login / Registration | — |
| `home` | Main home | — |
| `settings` | Settings | — |
| `reset-password/{token}` | Password reset | `virtualclubs://pass/reset-password?token={token}` |
| `verify_email/{status}` | Email verification result | `virtualclubs://email/verify-email?status={status}` |

---

## API Endpoints

Base URL: `https://api-vc.rgal.dev/` (dev) · `https://virtualclubs-backend.onrender.com/` (prod)

| Method | Route | Description | Interface |
|--------|-------|-------------|-----------|
| POST | `/v1/auth/login` | Login with email/password | AuthApi |
| POST | `/v1/auth/register` | Register new user | AuthApi |
| DELETE | `/v1/auth/logout` | Logout | AuthApi |
| POST | `/v1/auth/google` | Google Sign-In | AuthApi |
| POST | `/v1/auth/requestPasswordReset` | Request password reset | AuthApi |
| POST | `/v1/auth/resetPassword` | Apply reset with token | AuthApi |
| POST | `/v1/auth/requestVerify` | Request email verification | AuthApi |
| POST | `/v1/auth/refresh` | Refresh access token | RefreshApi |
| GET | `/v1/user/getUserInfo` | Authenticated user info | UserApi |

---

## Error Types

`ErrorType` enum — 27 valores en `domain/model/ErrorType.kt`.

Códigos **1–13** alineados con el backend (fuente de verdad):

| Código | Tipo |
|--------|------|
| 1 | `INTERNAL_ERROR` |
| 2 | `INVALID_CREDENTIALS` |
| 3 | `INVALID_REFRESH_TOKEN` |
| 4 | `INVALID_TOKEN` |
| 5 | `USER_NOT_FOUND` |
| 6 | `EMAIL_ALREADY_EXISTS` |
| 7 | `FIELD_BLANK` |
| 8 | `INVALID_EMAIL` |
| 9 | `PASSWORD_TOO_SHORT` |
| 10 | `PASSWORD_TOO_WEAK` |
| 11 | `EMAIL_NOT_VERIFIED` |
| 12 | `NO_LOCAL_PROVIDER` |
| 13 | `RATE_LIMIT_EXCEEDED` |

Códigos **14–27** solo en Android (sin equivalente backend):

`FAILED_SEND_EMAIL`, `EMAIL_NOT_FOUND`, `INVALID_ACCESS_TOKEN`, `CANT_CONNECT_SERVER`, `MISSING_TOKENS`, `GOOGLE_SIGN_IN_FAILED`, `PASSWORD_NOT_EQUALS`, `GOOGLE_SIGN_IN_NO_TOKEN`, `GOOGLE_LOGIN_EXCEPTION`, `INVALID_GOOGLE_TOKEN`, `USERNAME_NOT_FOUND`, `EMAIL_REQUIRED`, `PASSWORD_REQUIRED`, `USERNAME_REQUIRED`

Cada nuevo tipo requiere un caso en `ErrorHandler.kt` (presentación) para el mensaje de usuario.

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Hilt 2.57 |
| Navigation | Jetpack Navigation Compose + deep links |
| Networking | Retrofit 3 + OkHttp 5 (certificate pinning in prod) |
| Auth | Google CredentialManager (modern API) |
| Secure storage | AndroidKeyStore + AES/GCM encryption |
| Local storage | DataStore Preferences |
| Camera / QR | CameraX + ML Kit Barcode Scanning |
| Testing | JUnit 4 + MockK + Coroutines Test + Fakes |
| Language | Kotlin 2.2.10 |

---

## Build Flavors

| Flavor | Backend URL | Certificate pinning |
|--------|------------|---------------------|
| `dev` | `api-vc.rgal.dev` | Disabled |
| `prod` | `virtualclubs-backend.onrender.com` | Enabled |

Both flavors read `GOOGLE_CLIENT_ID` from `secrets.properties` (never hardcoded).

```bash
./gradlew compileDevDebugKotlin      # Verify Kotlin errors
./gradlew assembleDevDebug           # Full debug build
./gradlew testDevDebugUnitTest       # Run unit tests
./gradlew clean
```

---

## Secrets Configuration

Copy `app/secrets.properties.example` to `app/secrets.properties` (never committed):

```properties
GOOGLE_CLIENT_ID=your_google_oauth2_web_client_id_here
```

Use the **Web client** type from Google Cloud Console. The Android client is automatically linked via package name + SHA-1 fingerprint.

---

## Patterns & Conventions

### New screen structure

Each screen lives in its own folder under `presentation/screens/`. The minimum structure is:

1. `[Name]Page.kt` — root screen Composable
2. `[Name]ViewModel.kt` — ViewModel with StateFlow
3. Route added to `Screen.kt`
4. Entry added to `NavGraph.kt`

When a screen grows, split it into subfolders within the same folder — **all files keep the same package declaration** (e.g. `package es.virtualclubs.presentation.screens.auth`), so no imports change:

```
screens/[name]/
├── [Name]Page.kt       root composable + main content composable
├── [Name]ViewModel.kt
├── components/         composables used only by this screen (visibility: internal)
└── dialogs/            dialogs triggered from this screen (visibility: public or internal)
```

Use `internal` for composables that must not be called from outside the screen folder.

### ViewModels

- State via immutable `StateFlow` / `MutableStateFlow` (never expose `MutableStateFlow` publicly)
- Side effects (navigation, toasts) via `SharedFlow` / `Channel`
- Inject with `@HiltViewModel` + `@Inject constructor`
- Coroutines via `viewModelScope.launch`
- No references to `Context`, `Activity`, or `Composable` types

### Use Cases

- One file per use case in `domain/usecase/`
- `@Singleton` registered in `UseCaseModule`
- `operator fun invoke(...)` as entry point
- All business logic lives here, not in ViewModels or repositories

### Repositories

- Interface in `domain/repository/`
- Implementation in `data/repository/`
- Only data mapping — no business logic
- Return `Result<T>` or delegate to `SafeResponse`

### Error handling

- `VirtualClubException(type: ErrorType)` for business errors
- `SafeResponse` catches network exceptions and delegates token refresh
- `GlobalUIManager` implements `ErrorDispatcher` from the domain layer — use cases dispatch errors without knowing about UI
- `ErrorHandler` (in `presentation/managers/`) maps `ErrorType` → `R.string` resource ID for display. Must stay separate from `ErrorType` because `domain/` cannot import Android resources.

### Token refresh without race conditions

`SafeResponse` intercepts 401 responses and triggers a refresh. If multiple requests fail simultaneously, only one refresh call is made — others wait and retry with the new token.

### Secure storage

- Tokens always in `SecureUserPreferences` (AES/GCM with AndroidKeyStore)
- Never in plain `SharedPreferences`, never logged
- Email and session flags in `UserPreferences` (plain DataStore)

### Session management

`UserSession` (@Singleton) is the single in-memory source of truth for auth state:

```kotlin
sealed class SessionState {
    data object LoggedOut : SessionState()
    data class LoggedIn(val user: User) : SessionState()
}
```

| Method | Effect |
|--------|--------|
| `login(user)` | emits `LoggedIn` |
| `logout()` | emits `LoggedOut` **and** clears `cachedAccessToken` |
| `cacheAccessToken(token)` | stores token in memory for fast interceptor reads |

All logout flows go through `LogoutUserUseCase(notifyBackend: Boolean)` — single source of truth for cleanup:

```kotlin
// notifyBackend = true  → voluntary logout (user taps "Sign out")
// notifyBackend = false → forced logout (invalid/expired token)
suspend operator fun invoke(notifyBackend: Boolean = true) {
    if (notifyBackend) runCatching { repository.logout() } // best-effort
    userSession.logout()        // clears sessionState + cachedAccessToken
    clearTokensUseCase()        // clears tokens from disk (SecurePrefs)
    userPreferences.clearUser() // clears email from DataStore
}
```

**Voluntary logout** (user taps "Sign out" — UI pending VC-92):
```
SettingsViewModel.logout()
  ├── logoutUserUseCase(notifyBackend = true)
  └── appNavigator.navigateToLoginAndClearStack()
```

**Forced logout** (invalid/missing token, triggered by `GlobalUIManager`):
```
SessionManager.logout()
  ├── logoutUserUseCase(notifyBackend = false)
  └── appNavigator.navigateToLoginAndClearStack()
```

### In-memory token cache

Access tokens are cached in `UserSession.cachedAccessToken` after the first successful auth. `AuthInterceptor` reads synchronously from this cache to avoid `runBlocking` on every request. `UserSession.logout()` atomically clears both `sessionState` and the token cache. The only `runBlocking` in the project is in `NetworkModule` at Hilt graph construction time (dev builds only).

### Composables

- PascalCase names
- `@Preview` on all reusable composables
- `modifier: Modifier = Modifier` as first parameter after state
- Do not call ViewModels directly from child composables — pass lambdas down

### VCDialog — dialogs del sistema

`VCDialog` es una interfaz de clase (no composable) que `GlobalUIManager` muestra mediante `VCScaffold`. Para añadir un diálogo:

1. Crear una clase que implemente `VCDialog` en `presentation/screens/[screen]/dialogs/`.
2. Implementar los campos obligatorios: `titleRes`, `confirmTextRes`, `onConfirm`.
3. Implementar `ColumnScope.Content()` con el cuerpo del diálogo.
4. Mostrar con `globalUIManager.showDialog(MyDialog(...))`.

**Botón cancelar opcional:** sobrescribir `dismissTextRes` con un `R.string` — `VCScaffold` lo renderiza automáticamente y llama a `globalUIManager.hideDialog()`:
```kotlin
override val dismissTextRes = R.string.cancel
```
Por defecto es `null` (sin botón cancelar).

**Estado reactivo en el diálogo:** usar `mutableStateOf(...)` a nivel de constructor de la clase — el snapshot system de Compose rastrea las lecturas dentro del bloque `@Composable Content()` y provoca recomposiciones correctamente.

### Testing strategy

- **Fakes** for repositories — configurable real implementations in `test/fakes/`, not mocks
- **MockK** only for Android-dependent classes (`SecureUserPreferences`, `GlobalUIManager`)
- `MainDispatcherRule` on every test using coroutines or `StateFlow`

---

## Known Technical Debt

| Item | Detail |
|------|--------|
| Home screen incomplete | Club listing implemented but management features are placeholders |
| UserApi limited | `getUserInfo` only returns email — needs expansion for clubs, config, etc. |
| `prod` flavor backend | Both flavors point to Render — a dedicated production URL is needed |
| UI/instrumentation tests | No Compose UI tests implemented yet |
| Apple / Facebook login | Buttons render correctly but `onApple` and `onFacebook` are `{ /* TODO */ }` — not yet implemented |
| Privacy Policy / Terms of Service | `SettingsPage` has `onClick = null` placeholders for Privacy Policy and Terms rows — URLs not yet implemented |
| `SettingsViewModel` missing tests | No unit tests for the 6-flow combine and session state mapping logic |
| Logout removed from Settings | `SettingsViewModel.logout()` and its UI button were removed in VC-92 — no logout path exists in the app until a replacement is implemented |

---

## Rules for Claude

### Security
- **Prioritize secure code** (OWASP Mobile Top 10) — this app handles tokens, credentials, and the full authentication flow
- **Before changing any auth, token, or session logic**, explain the security impact first
- **Never hardcode** `GOOGLE_CLIENT_ID` or any secret in source files — always read from `BuildConfig`
- **Tokens always in `SecureUserPreferences`** (AES/GCM) — never in plain `SharedPreferences`, never logged

### Branch & commit discipline
- **Never commit directly** to `main` or `development` — always use feature/fix branches
- **Branch names: ASCII only** — no accented characters or special symbols (e.g. `fix/vc-3-token` not `fix/vc-3-tóken`)
- **Documented exception**: `.github/workflows/graphify.yml` auto-commits `graphify-out/` to `development` on every push, authored by the `graphify-bot` identity. This is the only allowed exception — it touches only generated, non-executable graph artifacts, never application code.

### Architecture
- **Keep layer boundaries**: Composable → ViewModel → UseCase → Repository. No layer skipping.
- **Inject via interfaces**: use cases receive repository interfaces (not `*RepositoryImpl`) — never concrete implementations; ViewModels receive use cases, never repositories directly
- **New error types** must have a corresponding case in `ErrorHandler.kt`
- **New routes** must be added to both `Screen.kt` and `NavGraph.kt`
- **New use cases** must be registered as `@Singleton` in `UseCaseModule`
- **Models** should be `data class` — no logic in data classes
- **Do not add** `runBlocking` outside of `NetworkModule` — use coroutines properly
- **Domain model growth**: plain entities and value objects stay in `domain/model/`; create a subpackage only when a coherent group reaches 4+ closely related files. Current groups: auth/session (`AuthTokens`, `SessionState`, `User`), errors (`ErrorType`, `ErrorDispatcher`, `VirtualClubException`).
- **Session state**: always read auth state from `UserSession.sessionState`, never from `UserPreferences` or other DataStore flows — those are for persistence, not runtime truth

### API versioning
- **Prefer the current active version** when consuming backend endpoints
- **When the backend deprecates an endpoint** (response header `Deprecation: true`), open a Plane work item to migrate before the `Sunset` date — never keep consuming a sunsetted endpoint
- **Additive changes** (new optional fields, new endpoints) do not require an Android release; breaking changes (field renamed/removed) do

### Pending features
- **Do not delete commented-out code** that marks pending features (`/* TODO */` stubs, `onClick = null` placeholders) — leave the context intact until the feature is implemented

### Documentation
- **Update this file** when adding new routes, endpoints, or patterns
- **When adding a new API endpoint** consumed from the app, update the API Endpoints table above

## graphify

This project has a knowledge graph at graphify-out/ with god nodes, community structure, and cross-file relationships.

Rules:
- For codebase questions, first run `graphify query "<question>"` when graphify-out/graph.json exists. Use `graphify path "<A>" "<B>"` for relationships and `graphify explain "<concept>"` for focused concepts. These return a scoped subgraph, usually much smaller than GRAPH_REPORT.md or raw grep output.
- If graphify-out/wiki/index.md exists, use it for broad navigation instead of raw source browsing.
- Read graphify-out/GRAPH_REPORT.md only for broad architecture review or when query/path/explain do not surface enough context.
- After modifying code, run `graphify update .` to keep the graph current (AST-only, no API cost).
- **Cross-repo questions:** if the question involves both this Android app and the
  backend (`VirtualClubs-BackEnd`), or you're not sure which side something lives in,
  query the merged graph instead of the local one — it has both repos' nodes and edges
  together:
  1. `git -C ../graph-hub pull` (first time: `git clone
     https://github.com/RaulGL422/virtualclubs-graph-hub.git ../graph-hub`)
  2. `graphify query --graph ../graph-hub/cross-repo/graph.json "<question>"` (same
     `--graph` flag works with `path`/`explain`)
  The hub repo is private — if you don't have access, say so and fall back to querying
  this repo's graph and asking the user about the backend side, rather than guessing.
