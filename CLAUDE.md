# CLAUDE.md — Virtual Clubs Android

## Contexto del Proyecto

App Android en **Jetpack Compose** para gestión de **clubes deportivos virtuales**. Actualmente implementa el sistema de autenticación completo y la estructura base de navegación. Es un proyecto en desarrollo activo por un desarrollador junior.

- **Versión app:** 0.1.2v Alpha | **Kotlin:** 2.2.10 | **AGP:** 9.1.0
- **Compose BOM:** 2026.03.00 | **Hilt:** 2.57
- **Min SDK:** 30 | **Target SDK:** 36 | **Compile SDK:** 36
- **API Backend:** https://virtualclubs-backend.onrender.com/
- **Rama de trabajo habitual:** ramas `feature/*` o `fix/*` (nunca main/development directamente)
- **Nombres de rama:** inglés o español pero **solo ASCII** — sin tildes, sin ñ (ej: `feature/email-verification`, no `feature/verificación-email`)

---

## Arquitectura del Proyecto

Clean Architecture + MVVM. Tres capas:

```
es/virtualclubs/
├── App.kt                              HiltAndroidApp — punto de entrada
├── VirtualClubsMainApp.kt              Composable raíz (NavHost) + enum ScreenType (Small/Medium)
│
├── data/                               CAPA DE DATOS
│   ├── local/
│   │   ├── datastore/
│   │   │   ├── AppPreferences.kt       Tema oscuro, contraste, tamaño fuente (DataStore)
│   │   │   └── UserPreferences.kt      Email, autologin flag (DataStore)
│   │   └── secure/
│   │       ├── EncryptionUtils.kt      AES/GCM para tokens
│   │       └── SecureUserPreferences.kt Almacenamiento encriptado de tokens (AndroidKeyStore)
│   ├── managers/
│   │   ├── SafeCall.kt                 Wrapper que propaga errores al ErrorDispatcher (UI global)
│   │   └── SafeResponse.kt             Manejo de errores HTTP + refresh automático de token
│   ├── models/
│   │   ├── Club.kt                     Modelo de datos de club
│   │   └── User.kt                     User(email: String?)
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApi.kt              Endpoints de autenticación
│   │   │   ├── RefreshApi.kt           Endpoint de refresh de token
│   │   │   └── UserApi.kt              Endpoint de info de usuario
│   │   └── dto/                        DTOs de request/response (ApiResponse, AuthRequest, etc.)
│   ├── repository/
│   │   ├── AuthRepositoryImpl.kt
│   │   ├── RefreshRepositoryImpl.kt
│   │   └── UserRepositoryImpl.kt
│   └── session/
│       └── UserSession.kt              Estado en memoria del usuario (StateFlow + cachedAccessToken)
│
├── di/                                 INYECCIÓN DE DEPENDENCIAS (Hilt)
│   ├── DispatcherModule.kt             Vincula GlobalUIManager como ErrorDispatcher
│   ├── GlobalUIEntryPoint.kt           EntryPoint para VirtualClubsMainApp (GlobalUIManager + AppNavigator)
│   ├── NetworkModule.kt                Retrofit + OkHttp + AuthInterceptor + certificate pinning (prod)
│   ├── PreferencesModule.kt            DataStore providers (user_prefs, app_prefs) + SecureUserPreferences
│   ├── SessionModule.kt                UserSession singleton
│   └── UseCaseModule.kt                (vacío — use cases usan @Singleton @Inject constructor directamente)
│
├── domain/                             CAPA DE DOMINIO
│   ├── dialogs/
│   │   ├── EmailNotVerified.kt         Modelo de datos para el diálogo de email no verificado
│   │   └── VCDialog.kt                 Modelo genérico de diálogo
│   ├── model/
│   │   ├── AuthInterceptor.kt          Inyecta Bearer token + detecta expiración local
│   │   ├── AuthTokens.kt               accessToken + refreshToken + email?
│   │   ├── Endpoint.kt                 Constantes de rutas HTTP agrupadas por módulo
│   │   ├── ErrorDispatcher.kt          Interfaz para despachar errores a la UI (implementada por GlobalUIManager)
│   │   ├── ErrorType.kt                Enum con 27 tipos de error (códigos 1-27)
│   │   └── VirtualClubException.kt     Excepción custom del proyecto
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── RefreshRepository.kt
│   │   └── UserRepository.kt
│   └── usecase/                        Casos de uso (lógica de negocio)
│       ├── AuthUseCase.kt              Login con email/contraseña
│       ├── GetUserInfoUseCase.kt       Obtiene info usuario + sincroniza UserSession
│       ├── GoogleUseCase.kt            Login con Google (CredentialManager)
│       ├── LogoutUserUseCase.kt        Logout backend + clearUser() en sesión
│       ├── RefreshTokenUseCase.kt      Refresca access token con control de logout
│       ├── RegisterUseCase.kt          Registro con email/contraseña
│       └── token/
│           ├── ClearTokensUseCase.kt   Limpia tokens de SecureUserPrefs + caché
│           ├── GetAccessTokenUseCase.kt Lee access token encriptado
│           ├── GetRefreshTokenUseCase.kt Lee refresh token encriptado
│           └── SaveTokensUseCase.kt    Guarda tokens + actualiza caché en UserSession
│
├── presentation/                       CAPA DE PRESENTACIÓN
│   ├── MainActivity.kt                 Activity principal (launcher, singleTask)
│   ├── ResetPasswordActivity.kt        Activity de deep link reset-password
│   ├── VerifyEmailActivity.kt          Activity de deep link verify-email
│   ├── components/                     Componentes reutilizables
│   │   ├── AppBar.kt
│   │   ├── Buttons.kt
│   │   ├── Scaffold.kt
│   │   ├── TextFields.kt
│   │   └── dialogs/
│   │       └── EmailNotVerifiedDialog.kt
│   ├── handlers/
│   │   └── ErrorHandler.kt             ErrorType → string resource
│   ├── dialogs/
│   │   └── VCDialog.kt                 Interfaz de diálogos del sistema
│   ├── managers/
│   │   └── GlobalUIManager.kt          @Singleton inyectable: loading, errores, diálogos. En composables: LocalGlobalUIManager.current
│   ├── navigation/
│   │   ├── AppNavigator.kt             @Singleton inyectable de navegación
│   │   ├── NavGraph.kt                 NavHost con rutas + deep links
│   │   ├── Screen.kt                   Rutas selladas del proyecto
│   │   └── SessionManager.kt           Logout coordinado: DataStore + SecurePrefs + navegación
│   ├── screens/
│   │   ├── auth/                       AuthPage.kt + AuthViewModel.kt
│   │   ├── home/                       HomePage.kt + HomeViewModel.kt
│   │   ├── resetPassword/              ResetPasswordPage.kt + ResetPasswordViewModel.kt
│   │   ├── settings/                   SettingsPage.kt + SettingsViewModel.kt
│   │   └── verifyemailresult/          VerifyEmailResultPage.kt + VerifyEmailResultViewModel.kt
│   └── theme/
│       ├── Color.kt                    Paleta Material 3 (light/dark/contrast)
│       ├── Shape.kt
│       ├── Sizes.kt                    Dimensiones reutilizables
│       ├── Spacing.kt                  Espaciado reutilizable
│       ├── Theme.kt                    VirtualClubsTheme composable
│       └── Type.kt                     Tipografía
```

---

## Rutas de Navegación

Clase sellada `Screen` — todas las rutas del app:

| Ruta | Pantalla | Deep Link |
|------|---------|-----------|
| `auth/{message}` | Login / Registro | — |
| `home` | Home principal | — |
| `settings` | Configuración | — |
| `reset-password/{token}` | Resetear contraseña | `virtualclubs://pass/reset-password?token={token}` |
| `verify_email/{status}` | Resultado verificación | `virtualclubs://email/verify-email?status={status}` |

Los deep links `reset-password` y `verify-email` se reciben en actividades separadas (`ResetPasswordActivity`, `VerifyEmailActivity`) declaradas en el `AndroidManifest.xml` con `android:scheme="virtualclubs"`.

---

## Endpoints API Consumidos

Base URL: `https://virtualclubs-backend.onrender.com/`

| Método | Ruta | Descripción | API interface |
|--------|------|-------------|---------------|
| POST | `/v1/auth/login` | Login con email/contraseña | AuthApi |
| POST | `/v1/auth/register` | Registro de usuario | AuthApi |
| DELETE | `/v1/auth/logout` | Cerrar sesión | AuthApi |
| POST | `/v1/auth/google` | Login con Google | AuthApi |
| POST | `/v1/auth/requestPasswordReset` | Solicitar reset de contraseña | AuthApi |
| POST | `/v1/auth/resetPassword` | Aplicar reset con token | AuthApi |
| POST | `/v1/auth/requestVerify` | Solicitar verificación de email | AuthApi |
| POST | `/v1/auth/refresh` | Refrescar access token | RefreshApi |
| GET | `/v1/user/getUserInfo` | Info del usuario autenticado | UserApi |

---

## Tipos de Error (ErrorType enum — 27 tipos)

Códigos 1-13 alineados con el backend (fuente de verdad). Códigos 14+ son errores solo de Android.

`INTERNAL_ERROR(1)`, `INVALID_CREDENTIALS(2)`, `INVALID_REFRESH_TOKEN(3)`, `INVALID_TOKEN(4)`, `USER_NOT_FOUND(5)`, `EMAIL_ALREADY_EXISTS(6)`, `FIELD_BLANK(7)`, `INVALID_EMAIL(8)`, `PASSWORD_TOO_SHORT(9)`, `PASSWORD_TOO_WEAK(10)`, `EMAIL_NOT_VERIFIED(11)`, `NO_LOCAL_PROVIDER(12)`, `RATE_LIMIT_EXCEEDED(13)`, `FAILED_SEND_EMAIL(14)`, `EMAIL_NOT_FOUND(15)`, `INVALID_ACCESS_TOKEN(16)`, `CANT_CONNECT_SERVER(17)`, `MISSING_TOKENS(18)`, `GOOGLE_SIGN_IN_FAILED(19)`, `PASSWORD_NOT_EQUALS(20)`, `GOOGLE_SIGN_IN_NO_TOKEN(21)`, `GOOGLE_LOGIN_EXCEPTION(22)`, `INVALID_GOOGLE_TOKEN(23)`, `USERNAME_NOT_FOUND(24)`, `EMAIL_REQUIRED(25)`, `PASSWORD_REQUIRED(26)`, `USERNAME_REQUIRED(27)`

---

## Build Flavors y Comandos

| Flavor | Descripción |
|--------|-------------|
| `dev` | BASE_URL apunta a Render, Google Client ID desde `secrets.properties` |
| `prod` | BASE_URL + Google Client ID desde `secrets.properties` + certificate pinning activo |

**Comandos de build habituales:**
```bash
# Compilar (verificar errores Kotlin)
./gradlew compileDevDebugKotlin

# Build completo debug
./gradlew assembleDevDebug

# Tests unitarios
./gradlew test

# Tests unitarios del flavor dev
./gradlew testDevDebugUnitTest

# Limpiar
./gradlew clean
```

---

## Tests Unitarios

**Estructura de tests en `app/src/test/java/es/virtualclubs/`:**

```
es/virtualclubs/
├── data/
│   └── managers/
│       ├── SafeCallTest.kt             Propagación de errores al ErrorDispatcher
│       └── SafeResponseTest.kt         Happy path, 401+refresh, IOException, 4xx/5xx fallback
├── domain/
│   └── usecase/
│       ├── AuthUseCaseTest.kt          Login exitoso, credenciales inválidas, delegación
│       ├── GetUserInfoUseCaseTest.kt   Info usuario + sincronización de UserSession
│       ├── GoogleUseCaseTest.kt        Autenticación Google, token inválido
│       ├── LogoutUserUseCaseTest.kt    Logout + limpieza de sesión
│       ├── RefreshTokenUseCaseTest.kt  Refresh exitoso, token inválido, canLogout flag
│       ├── RegisterUseCaseTest.kt      Registro exitoso, email existente, delegación
│       └── token/
│           ├── ClearTokensUseCaseTest.kt   Limpieza de tokens + caché
│           ├── GetAccessTokenUseCaseTest.kt Lectura de access token
│           ├── GetRefreshTokenUseCaseTest.kt Lectura de refresh token
│           └── SaveTokensUseCaseTest.kt     Guardado de tokens + caché en UserSession
├── fakes/
│   ├── FakeAuthRepository.kt           Fake configurable de AuthRepository (sin mockk)
│   ├── FakeRefreshRepository.kt        Fake configurable de RefreshRepository
│   └── FakeUserRepository.kt           Fake configurable de UserRepository
└── utils/
    └── MainDispatcherRule.kt           Rule para reemplazar Dispatchers.Main en tests con coroutines
```

**Dependencias de test:**
- `mockk` 1.14.9 — mocks en Kotlin (para SecureUserPreferences y GlobalUIManager que dependen de Android)
- `kotlinx-coroutines-test` 1.10.2 — coroutines en tests
- `testOptions.unitTests.isReturnDefaultValues = true` — evita crash de stubs Android (e.g. `Log.d`) en JVM

**Estrategia de tests:**
- **Fakes** (`fakes/`) para repositorios — implementaciones reales configurables, más legibles que mockk
- **mockk** para clases con dependencias Android (`SecureUserPreferences`, `GlobalUIManager`)
- `MainDispatcherRule` en todos los tests que usen coroutines o StateFlow
- Tests de flujo Google Sign-In deben ir en `androidTest/` (requieren Play Services)

**Nota:** `AuthViewModel` usa `by lazy` para `googleSignInClient` para que el test pueda instanciarse sin Play Services en JVM.

---

## Patrones y Convenciones

### Estructura de un screen nuevo
Cada pantalla sigue el patrón:
1. `[Nombre]Page.kt` — Composable raíz de la pantalla
2. `[Nombre]ViewModel.kt` — ViewModel con StateFlow
3. Ruta en `Screen.kt`
4. Entrada en `NavGraph.kt`

### ViewModel
- Estado con `StateFlow` / `MutableStateFlow`
- Efectos secundarios con `SharedFlow` / `Channel`
- Inyección con `@HiltViewModel` + `@Inject constructor`
- Coroutines con `viewModelScope.launch`

### Repositorios
- Interfaz en `domain/repository/`
- Implementación en `data/repository/`
- Siempre retornan `Result<T>` — usan `SafeResponse` internamente

### Use Cases
- Un archivo por use case en `domain/usecase/`
- `@Singleton` provisto en `UseCaseModule`
- `operator fun invoke(...)` como punto de entrada

### Gestión de errores
- `VirtualClubException(type: ErrorType)` para errores de negocio
- `SafeResponse` captura excepciones de red y delega refresh de token
- `SafeCall` captura errores de use cases y los despacha a `ErrorDispatcher`
- `GlobalUIManager` (implementa `ErrorDispatcher`) maneja loading/error/diálogos a nivel global
- `ErrorHandler` mapea `ErrorType` → string resource para mostrar en UI

### Almacenamiento seguro
- Tokens siempre en `SecureUserPreferences` (AES/GCM con AndroidKeyStore)
- Nunca en `SharedPreferences` plano ni en logs
- Email y flags de sesión en `UserPreferences` (DataStore normal)

### Composables
- Nombres en PascalCase
- `@Preview` en todos los composables reutilizables
- `modifier: Modifier = Modifier` como primer parámetro después del estado
- No llamar ViewModels directamente desde composables hijos — pasar lambdas

### Commits y ramas
- Commits en español, formato semántico: `[tipo](scope): descripción`
- Scopes habituales: `auth`, `navigation`, `theme`, `home`, `settings`, `user`, `deps`, `network`, `di`
- Nunca commitear directamente en `main` o `development`

---

## Comandos Personalizados

| Comando | Descripción |
|---------|-------------|
| `/add-task` | Crea tarjeta en Notion |
| `/new-feature` | Crea rama desde tarjeta Notion o descripción libre |
| `/do-task` | Flujo completo autónomo: rama → implementa → commit → PR → review |
| `/commit` | Commit semántico con referencia Notion opcional |
| `/create-pr` | Crea PR hacia development + actualiza Notion |
| `/review-pr` | Revisión exhaustiva de PR (Android-specific) |
| `/project-status` | Estado del proyecto y deuda técnica |
| `/sync-main` | Sincroniza rama actual con main via rebase |
| `/update-deps` | Revisa y actualiza dependencias en libs.versions.toml |
| `/add-test` | Genera tests unitarios o de UI para una clase/composable |
| `/check-security` | Auditoría de seguridad Android |
| `/explain` | Explica un archivo o concepto del proyecto |
| `/release-debug` | Agrupa tareas debug, genera CHANGELOG, crea release PR |

---

## Integración con Notion

Misma base de datos del proyecto backend.

- **Database ID:** `276a7f5d-0a0f-80ab-9cd3-d4f3c733a260`
- **Collection ID:** `276a7f5d-0a0f-802c-8d6f-000b821853c1`
- **Search URL:** `collection://276a7f5d-0a0f-802c-8d6f-000b821853c1`

**Propiedades:**
- `Nombre de la tarea` (title)
- `userDefined:ID` (auto_increment, formato VC-N)
- `Tipo de tarea` (multi_select)
- `Estado` (status)
- `Prioridad` (select: Alta, Medio, Baja)
- `Descripción` (text)
- `Nivel de esfuerzo` (select: Pequeño, Medio, Grande)

**Flujo de estados:**
```
Sin Empezar → 💻 En curso → 📬 PR Abierto → 📦 Pendiente debug → ⌛🔎 Pendiente de testeo → 👁️ Pendiente de Publicar → ✅ Publicado
```

**Tipos de tarea disponibles (misma DB que el backend):**
- `🐞 Error` — bug fix
- `🔎 Testing` — tests unitarios o UI
- `✏️ Diseño` — trabajo en composables, pantallas, componentes UI
- `🛠️ Funcionalidad` — nueva feature del app
- `📱 Android` — configuración nativa, permisos, manifest, deep links, Gradle
- `🔒 Autenticación` — flujos de login/registro/tokens
- `⛓️ API` — cambios en la capa de red, DTOs, endpoints consumidos
- `📊 Base de datos` — DataStore, Room si se añade en el futuro

---

## Deuda Técnica Conocida

| Ítem | Detalle |
|------|---------|
| Home screen vacía | `HomePage` es placeholder — falta contenido real de clubes |
| `prod` flavor sin URL real | Ambos flavors apuntan a Render — falta URL de producción propia |
| Tests UI sin cobertura | No hay tests de UI/instrumentación (solo unitarios) |
| `UserApi` limitada | `getUserInfo` solo devuelve email — falta expandir para clubs, config, etc. |
| `domain/dialogs/VCDialog.kt` | Archivo físico aún en `domain/` — typealias de migración hacia `presentation/dialogs/VCDialog.kt`. Eliminar y mover cuando sea posible. |
| Certificate pinning solo en prod | Los pins SHA256 de Render pueden expirar; no hay alerta de rotación de pins |
