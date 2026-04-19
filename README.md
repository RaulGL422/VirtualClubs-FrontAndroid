# VirtualClubs Android

Android app built with **Jetpack Compose** for managing virtual sports clubs — create clubs, organize events, handle classes, members, and attendance.

> **Status:** Alpha v0.1.2 — Authentication system complete. Club management in active development.

---

## Screenshots

> Coming soon — UI screenshots will be added in the next release.

---

## Features

### Authentication (complete)
- Email/password login and registration
- Google Sign-In via **CredentialManager** (modern API)
- Auto-login with refresh token on app startup
- Password reset via email deep link
- Email verification via deep link
- Secure logout with full token cleanup

### Security
- Tokens encrypted with **AES/GCM + AndroidKeyStore** — never in plain SharedPreferences
- **Certificate pinning** in production (OkHttp)
- Local JWT expiration check before network requests (avoids unnecessary round-trips)
- Automatic token refresh with race-condition protection

### Architecture & Infrastructure
- Global UI state manager (loading, errors, dialogs) — single source of truth
- Build flavors: `dev` (Render backend) / `prod` (certificate pinning active)
- Deep links for external flows (password reset, email verification) via separate Activities
- Material 3 dynamic theme: light / dark / high-contrast

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Hilt 2.57 |
| Navigation | Jetpack Compose Navigation + deep links |
| Networking | Retrofit + OkHttp (certificate pinning) |
| Auth | Google CredentialManager |
| Secure storage | AndroidKeyStore + AES/GCM encryption |
| Local storage | DataStore Preferences |
| Language | Kotlin 2.2.10 |
| Min SDK | 30 (Android 11) |

---

## Architecture

The project follows **Clean Architecture** with three layers:

```
presentation/       ← Compose screens, ViewModels, navigation, UI managers
domain/             ← Use cases, repository interfaces, domain models, error types
data/               ← Repository implementations, Retrofit APIs, DTOs, local storage
```

### Key patterns
- **StateFlow** for UI state in all ViewModels
- **Result<T>** returned by all repositories — errors never thrown across layer boundaries
- **SafeResponse** handles network errors + automatic token refresh centrally
- **SafeCall** dispatches use case errors to `GlobalUIManager` without boilerplate in ViewModels
- **ErrorDispatcher** interface decouples error handling from the UI manager implementation

```
es.virtualclubs/
├── data/
│   ├── local/          # DataStore (UserPreferences, AppPreferences) + SecureUserPreferences
│   ├── managers/       # SafeCall, SafeResponse
│   ├── remote/         # Retrofit API interfaces + DTOs
│   └── repository/     # Repository implementations
├── di/                 # Hilt modules
├── domain/
│   ├── model/          # AuthInterceptor, ErrorType, ErrorDispatcher, Endpoint constants
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic (one file per use case)
└── presentation/
    ├── components/     # Reusable Compose components
    ├── managers/       # GlobalUIManager (loading, errors, dialogs)
    ├── navigation/     # NavGraph, Screen sealed class, AppNavigator, SessionManager
    ├── screens/        # auth/, home/, settings/, resetPassword/, verifyemailresult/
    └── theme/          # Material 3 theme, colors, typography, spacing
```

---

## API

Base URL: `https://virtualclubs-backend.onrender.com/`

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
- A Google Cloud project with an Android OAuth2 client ID

### 1. Clone the repository

```bash
git clone https://github.com/RaulGL422/VirtualClubs-Android.git
cd VirtualClubs-Android
```

### 2. Configure secrets

Copy the example file and fill in your credentials:

```bash
cp app/secrets.properties.example app/secrets.properties
```

Edit `app/secrets.properties`:

```properties
GOOGLE_CLIENT_ID=your_google_oauth2_web_client_id_here
```

> The Google Client ID is the **Web client** type from Google Cloud Console — not the Android client. The Android client ID is linked automatically via the package name + SHA-1.

### 3. Run the app

Open the project in Android Studio, select the `dev` build variant, connect a device or emulator (API 30+), and press **Run**.

---

## Build Variants

| Variant | Description |
|---------|-------------|
| `devDebug` | Development build — points to Render backend |
| `prodRelease` | Production build — certificate pinning active |

```bash
# Check for Kotlin errors
./gradlew compileDevDebugKotlin

# Build debug APK
./gradlew assembleDevDebug

# Run unit tests
./gradlew testDevDebugUnitTest

# Clean
./gradlew clean
```

---

## Tests

Unit test coverage for the core business logic:

```
data/managers/      SafeCall, SafeResponse (error propagation, 401+refresh, network errors)
domain/usecase/     Auth, Register, Google Sign-In, Logout, RefreshToken, GetUserInfo
                    + all token use cases (Save, Get, Clear)
```

**Strategy:**
- **Fakes** for repositories — real configurable implementations, no mock frameworks
- **MockK** for Android-dependent classes (`SecureUserPreferences`, `GlobalUIManager`)
- `MainDispatcherRule` for all coroutine/StateFlow tests

```bash
./gradlew testDevDebugUnitTest
```

---

## Roadmap

- [ ] Club listing and management screens
- [ ] Event creation and scheduling
- [ ] Member management
- [ ] Attendance tracking
- [ ] UI tests (Compose testing)
- [ ] Production backend URL

---

## Author

**Raul Galindo Lopez**
[LinkedIn](https://www.linkedin.com/in/raul-gldev/) · [GitHub](https://github.com/RaulGL422)

---

## License

© 2025 Raul Galindo Lopez. All rights reserved.
This repository is publicly visible for portfolio purposes. The source code may not be copied, distributed, or used commercially without explicit permission.
