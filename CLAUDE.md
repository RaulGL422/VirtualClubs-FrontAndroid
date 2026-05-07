# CLAUDE.md — VirtualClubs Android

## Project Overview

Android client for **VirtualClubs** — a platform to create and manage sports fan clubs. Users can join clubs by sport, connect with members, organize events, and manage attendance, all from their phone.

- **Platform:** Android (Kotlin + Jetpack Compose)
- **Architecture:** Clean Architecture + MVVM
- **Version:** 0.1.2 Alpha | **AGP:** 9.2.0 | **Kotlin:** 2.2.10
- **Compose BOM:** 2026.03.00 | **Hilt:** 2.57
- **Min SDK:** 30 (Android 11) | **Target SDK:** 37 | **Compile SDK:** 37
- **Backend:** https://api-vc.rgal.dev (dev) / https://virtualclubs-backend.onrender.com (prod)

---

## Package Structure

```
es/virtualclubs/
├── App.kt                          HiltAndroidApp entry point
├── Activities.kt                   MainActivity, ResetPasswordActivity, VerifyEmailActivity
├── VirtualClubsMainApp.kt          Root composable (theme + NavHost)
│
├── data/                           DATA LAYER
│   ├── local/
│   │   ├── datastore/
│   │   │   ├── AppPreferences.kt   Dark theme, contrast, font size
│   │   │   └── UserPreferences.kt  Email, auto-login flag
│   │   └── secure/
│   │       ├── EncryptionUtils.kt          AES/GCM for tokens
│   │       └── SecureUserPreferences.kt    Encrypted token storage
│   ├── managers/
│   │   ├── GlobalUIManager.kt      Global state: loading, errors, dialogs
│   │   ├── SafeCall.kt             API call wrapper with error dispatching
│   │   └── SafeResponse.kt         Response handler + automatic token refresh
│   ├── models/
│   │   └── User.kt                 User(email: String?)
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApi.kt          Authentication endpoints
│   │   │   ├── RefreshApi.kt       Token refresh endpoint
│   │   │   └── UserApi.kt          User info endpoint
│   │   └── dto/                    Request/response DTOs
│   └── repository/                 Repository implementations
│
├── di/                             DEPENDENCY INJECTION (Hilt)
│   ├── NetworkModule.kt            Retrofit + OkHttp + interceptors
│   ├── PreferencesModule.kt        DataStore providers
│   ├── SessionModule.kt
│   ├── UseCaseModule.kt            All use cases (@Singleton)
│   └── GlobalUIEntryPoint.kt
│
├── domain/                         DOMAIN LAYER
│   ├── model/
│   │   ├── AuthInterceptor.kt      Injects Bearer token into requests
│   │   ├── AuthTokens.kt           accessToken + refreshToken
│   │   ├── ErrorType.kt            Enum with 27 error types
│   │   ├── ErrorDispatcher.kt      Interface for dispatching errors (implemented by GlobalUIManager)
│   │   └── VirtualClubException.kt Custom project exception
│   ├── repository/                 Repository interfaces
│   └── usecase/                    Use cases (business logic)
│       ├── AuthUseCase.kt
│       ├── GoogleUseCase.kt
│       ├── LogoutUserUseCase.kt
│       ├── RefreshTokenUseCase.kt
│       ├── RegisterUseCase.kt
│       └── token/                  GetAccessToken, GetRefreshToken, SaveTokens, ClearTokens
│
├── presentation/                   PRESENTATION LAYER
│   ├── components/                 Reusable composables
│   │   ├── AppBar.kt
│   │   ├── Buttons.kt
│   │   ├── Scaffold.kt
│   │   ├── TextFields.kt
│   │   └── dialogs/
│   ├── handlers/
│   │   └── ErrorHandler.kt         ErrorType → string resource
│   ├── navigation/
│   │   ├── AppNavigator.kt         Static navigation control
│   │   ├── NavGraph.kt             NavHost with all routes
│   │   ├── Screen.kt               Sealed class with all routes
│   │   └── SessionManager.kt       Global logout handling
│   ├── screens/
│   │   ├── auth/                   Login/Register (AuthPage + AuthViewModel)
│   │   │   ├── components/         AuthDivider, AuthToggle, SocialButtons (internal)
│   │   │   └── dialogs/            ForgotPasswordDialog
│   │   ├── home/                   Main home (HomePage + HomeViewModel)
│   │   ├── resetPassword/          Password reset flow
│   │   ├── settings/               Theme/font/server settings
│   │   └── verifyemailresult/      Email verification result
│   └── theme/
│       ├── Color.kt                Stadium DS — 10-level palette, 6 color schemes
│       ├── Shape.kt
│       ├── Theme.kt                VirtualClubsTheme composable
│       └── Type.kt                 Poppins + Roboto typography
│
└── session/
    └── UserSession.kt              In-memory user state (token cache + user info)
```

---

## Navigation Routes

Sealed class `Screen` — all routes:

| Route | Screen | Deep Link |
|-------|---------|-----------|
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

`ErrorType` enum — 27 types:

`INTERNAL_ERROR`, `INVALID_CREDENTIALS`, `USERNAME_NOT_FOUND`, `EMAIL_ALREADY_EXISTS`, `EMAIL_REQUIRED`, `PASSWORD_REQUIRED`, `USERNAME_REQUIRED`, `PASSWORD_MIN_LENGTH_ERROR`, `INVALID_EMAIL_FORMAT`, `FIELD_NULL`, `TOKEN_BLANK`, `INVALID_REFRESH_TOKEN`, `INVALID_GOOGLE_TOKEN`, `FAILED_SEND_EMAIL`, `INVALID_TOKEN`, `NO_LOCAL_PROVIDER`, `EMAIL_NOT_FOUND`, `INVALID_ACCESS_TOKEN`, `CANT_CONNECT_SERVER`, `MISSING_TOKENS`, `GOOGLE_SIGN_IN_FAILED`, `PASSWORD_NOT_EQUALS`, `GOOGLE_SIGN_IN_NO_TOKEN`, `GOOGLE_LOGIN_EXCEPTION`, `USER_NOT_FOUND`, `EMAIL_NOT_VERIFIED`, `UNKNOWN`

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
- `ErrorHandler` maps `ErrorType` → string resource for display

### Token refresh without race conditions

`SafeResponse` intercepts 401 responses and triggers a refresh. If multiple requests fail simultaneously, only one refresh call is made — others wait and retry with the new token.

### Secure storage

- Tokens always in `SecureUserPreferences` (AES/GCM with AndroidKeyStore)
- Never in plain `SharedPreferences`, never logged
- Email and session flags in `UserPreferences` (plain DataStore)

### In-memory token cache

Access tokens are cached in `UserSession` after the first read. `AuthInterceptor` reads synchronously from cache to avoid `runBlocking` on every request. The only `runBlocking` is in `NetworkModule` at Hilt graph construction time (dev builds only).

### Composables

- PascalCase names
- `@Preview` on all reusable composables
- `modifier: Modifier = Modifier` as first parameter after state
- Do not call ViewModels directly from child composables — pass lambdas down

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
| `collectAsState()` → `collectAsStateWithLifecycle()` | All screens use `collectAsState()` which does not respect Android lifecycle — migrate to `collectAsStateWithLifecycle()` |
| Auth side effect in composition | `if (uiState is AuthUiState.Success) onLogged()` in `LoginPage` should be inside a `LaunchedEffect` |

---

## Rules for Claude

- **Never commit directly** to `main` or `development` — always use feature/fix branches
- **Never hardcode** `GOOGLE_CLIENT_ID` or any secret in source files — always read from `BuildConfig`
- **Keep layer boundaries**: Composable → ViewModel → UseCase → Repository. No layer skipping.
- **New error types** must have a corresponding case in `ErrorHandler.kt`
- **New routes** must be added to both `Screen.kt` and `NavGraph.kt`
- **New use cases** must be registered as `@Singleton` in `UseCaseModule`
- **Models** should be `data class` — no logic in data classes
- **Do not add** `runBlocking` outside of `NetworkModule` — use coroutines properly
- **Update this file** when adding new routes, endpoints, or patterns
