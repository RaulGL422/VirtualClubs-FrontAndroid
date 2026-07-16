# VirtualClubs Android

[![CI](https://github.com/RaulGL422/VirtualClubs-FrontAndroid/actions/workflows/ci.yml/badge.svg)](https://github.com/RaulGL422/VirtualClubs-FrontAndroid/actions/workflows/ci.yml)
![Min SDK](https://img.shields.io/badge/min%20SDK-30-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)

> [Leer en español](README.es.md)

Android client for **VirtualClubs** — a platform to create and manage sports fan clubs. Users can join clubs by sport, connect with other members, organize events, manage attendance, and track club activity, all from their phone.

> **Status:** Alpha v0.1.2 — Authentication complete. Club management in active development.  
> Backend repo: [VirtualClubs-BackEnd](https://github.com/RaulGL422/VirtualClubs-BackEnd)

---

## Features

### Authentication (complete)
- Email/password login and registration
- Google Sign-In via **CredentialManager** (modern API — not legacy `play-services-auth`)
- Auto-login on startup using refresh token — no re-login after closing the app
- Password reset via email + deep link
- Email verification via deep link
- Secure logout with full token and session cleanup

### Security
- Access and refresh tokens encrypted with **AES/GCM + AndroidKeyStore** — never in plain `SharedPreferences`
- **Certificate pinning** in production builds (OkHttp) — prevents MITM attacks
- Local JWT expiration check in `AuthInterceptor` before every request — avoids unnecessary 401 round-trips
- Automatic token refresh on 401 with concurrency protection — parallel requests don't trigger multiple refresh calls

### UI & Infrastructure
- Global UI state manager (`GlobalUIManager`) — loading, error dialogs, and snackbars from a single source of truth, injectable anywhere without passing callbacks down the tree
- **Stadium Design System** — custom M3 token layer with Poppins + Roboto typography, a 10-level color palette (Electric Blue / Fire Orange / Stadium Green / Cool Blue-Gray), and semantic spacing, elevation, shape, motion, and gradient tokens across 6 color schemes (light, dark, high/medium contrast per mode)
- Material 3 dynamic theme: light / dark / high-contrast modes
- Deep links handled via dedicated Activities to avoid NavGraph contamination
- Build flavors: `dev` (`api-vc.rgal.dev` backend) / `prod` (certificate pinning active)
- Debug-only server URL switcher in Settings — configure any local backend IP at runtime without recompiling

---

## Technical Highlights

A few design decisions worth noting:

**Token refresh without race conditions** — `SafeResponse` intercepts 401 responses and triggers a token refresh. If multiple requests fail simultaneously, only one refresh call is made. The others wait and retry with the new token.

**No `runBlocking` on the hot path** — Access tokens are cached in memory (`UserSession`) after the first read. `AuthInterceptor` reads synchronously from the cache, avoiding `runBlocking` and extra latency on every request. The only `runBlocking` call is in `NetworkModule` at Hilt graph construction time (debug builds only, reads the dev server URL from DataStore before any request is made).

**`ErrorDispatcher` interface** — `GlobalUIManager` implements an `ErrorDispatcher` interface defined in the domain layer. Use cases dispatch errors without knowing anything about the UI, keeping the dependency direction correct.

**Fake repositories over mocks in tests** — Repository fakes are real configurable implementations. Tests are more readable, refactors don't break test setup, and the behavior is closer to production than what mocks provide.

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Hilt 2.59.2 |
| Navigation | Jetpack Navigation Compose + deep links |
| Networking | Retrofit + OkHttp (certificate pinning) |
| Auth | Google CredentialManager |
| Secure storage | AndroidKeyStore + AES/GCM encryption |
| Local storage | DataStore Preferences |
| Testing | MockK + Kotlin Fakes + Coroutines Test |
| Language | Kotlin 2.3.21 |
| Min SDK | 30 (Android 11) |

---

## Architecture

Three-layer Clean Architecture. Dependencies point inward — `presentation` depends on `domain`, `data` depends on `domain`, nothing depends on `presentation`.

```
presentation/   Compose screens, ViewModels, navigation, GlobalUIManager
domain/         Use cases, repository interfaces, domain models, ErrorType, ErrorDispatcher
data/           Repository implementations, Retrofit APIs, DTOs, encrypted local storage
```

```
es.virtualclubs/
├── data/
│   ├── local/          # DataStore preferences + AES/GCM encrypted token storage
│   ├── managers/       # SafeCall (error dispatch), SafeResponse (network + refresh)
│   ├── remote/         # Retrofit API interfaces + DTOs
│   └── repository/     # Repository implementations
├── di/                 # Hilt modules
├── domain/
│   ├── model/          # AuthInterceptor, ErrorType (27 types), ErrorDispatcher, Endpoint constants
│   ├── repository/     # Repository interfaces
│   └── usecase/        # One file per use case — business logic lives here, not in ViewModels
└── presentation/
    ├── components/     # Reusable Compose components (buttons, text fields, scaffold, dialogs)
    ├── managers/       # GlobalUIManager — loading, errors, dialogs as injectable singleton
    ├── navigation/     # NavGraph, Screen sealed class, AppNavigator, SessionManager
    ├── screens/        # one folder per screen — each folder may contain components/ and dialogs/ subfolders
│   │   ├── auth/           # AuthPage, AuthViewModel
│   │   │   ├── components/ # AuthDivider, AuthToggle, SocialButtons (internal to auth)
│   │   │   └── dialogs/    # ForgotPasswordDialog
│   │   ├── home/           # HomePage, HomeViewModel
│   │   ├── settings/       # SettingsPage, SettingsViewModel
│   │   ├── resetPassword/  # ResetPasswordPage, ResetPasswordViewModel
│   │   └── verifyemailresult/ # VerifyEmailResultPage, VerifyEmailResultViewModel
    └── theme/          # Material 3 colors, typography, spacing, shapes
```

---

## Tests

Unit test coverage for all core business logic:

```
data/managers/    SafeCall — error propagation to ErrorDispatcher
                  SafeResponse — happy path, 401+refresh, IOException, 4xx/5xx fallback
domain/usecase/   Auth, Register, Google Sign-In, Logout, RefreshToken, GetUserInfo
                  SaveTokens, GetAccessToken, GetRefreshToken, ClearTokens
```

**Strategy:**
- **Fakes** for repositories — real configurable implementations in `test/fakes/`, not mocks
- **MockK** only for Android-dependent classes (`SecureUserPreferences`, `GlobalUIManager`)
- `MainDispatcherRule` on every test that uses coroutines or `StateFlow`

```bash
./gradlew testDevDebugUnitTest
```

---

## API

Base URL: `https://virtualclubs-backend.onrender.com/` (production) · `https://api-vc.rgal.dev/` (dev flavor)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/auth/login` | Login with email/password |
| POST | `/v1/auth/register` | Register new user |
| DELETE | `/v1/auth/logout` | Logout |
| POST | `/v1/auth/google` | Google Sign-In |
| POST | `/v1/auth/requestPasswordReset` | Request password reset email |
| POST | `/v1/auth/resetPassword` | Apply reset with token |
| POST | `/v1/auth/requestVerify` | Request email verification |
| POST | `/v1/auth/refresh` | Refresh access token |
| GET | `/v1/user/getUserInfo` | Authenticated user info |

---

## Setup

### Prerequisites
- Android Studio **Hedgehog** or newer
- JDK 17+
- A Google Cloud project with a Web OAuth2 client ID

### 1. Clone the repository

```bash
git clone https://github.com/RaulGL422/VirtualClubs-FrontAndroid.git
cd VirtualClubs-FrontAndroid
```

### 2. Configure secrets

```bash
cp app/secrets.properties.example app/secrets.properties
```

Edit `app/secrets.properties`:

```properties
GOOGLE_CLIENT_ID=your_google_oauth2_web_client_id_here
```

> Use the **Web client** type from Google Cloud Console, not the Android client. The Android client is linked automatically via package name + SHA-1 fingerprint.

### 3. Run

Open in Android Studio, select the `devDebug` variant, connect a device or emulator (API 30+), and press **Run**.

### Local backend (optional)

To point a debug build at a local backend without recompiling, open **Settings → Development server**, enter the address (e.g. `192.168.1.50:3000` — `http://` and trailing `/` are added automatically), and tap **Save and restart**.

- **Emulator:** use `10.0.2.2` to reach your machine's `localhost`
- **Physical device:** use your machine's LAN IP; run `adb reverse tcp:PORT tcp:PORT` if the device is connected via USB

---

## Build Variants

| Variant | Description |
|---------|-------------|
| `devDebug` | Development — `api-vc.rgal.dev` backend, no certificate pinning |
| `prodRelease` | Production — `virtualclubs-backend.onrender.com`, certificate pinning active |

```bash
./gradlew compileDevDebugKotlin   # Check for Kotlin errors
./gradlew assembleDevDebug        # Build debug APK
./gradlew testDevDebugUnitTest    # Run unit tests
./gradlew clean
```

---

## Roadmap

| Feature | Status |
|---|---|
| Authentication (login, register, Google, reset, verify) | ✅ Complete |
| Secure token storage + auto-refresh | ✅ Complete |
| Unit tests — use cases and network layer | ✅ Complete |
| Home screen — club listing | 🔄 In progress |
| Club creation and management | ⏳ Planned |
| Event scheduling | ⏳ Planned |
| Member management | ⏳ Planned |
| Attendance tracking | ⏳ Planned |
| UI / instrumentation tests | ⏳ Planned |

---

## Author

**Raul Galindo Lopez**  
[LinkedIn](https://www.linkedin.com/in/raul-gldev/) · [GitHub](https://github.com/RaulGL422)

---

## License

© 2025 Raul Galindo Lopez. All rights reserved.  
Publicly visible for portfolio purposes. Not licensed for redistribution or commercial use.
