# CLAUDE.md — Virtual Clubs Android

## Contexto del Proyecto

App Android en **Jetpack Compose** para gestión de **clubes deportivos virtuales**. Actualmente implementa el sistema de autenticación completo y la estructura base de navegación. Es un proyecto en desarrollo activo por un desarrollador junior.

- **Versión app:** 0.1.1v Alpha | **Kotlin:** 2.2.10 | **AGP:** 9.1.0
- **Compose BOM:** 2025.07.00 | **Hilt:** 2.57
- **Min SDK:** 30 | **Target SDK:** 35 | **Compile SDK:** 36
- **API Backend:** https://virtualclubs-backend.onrender.com/
- **Rama de trabajo habitual:** ramas `feature/*` o `fix/*` (nunca main/development directamente)
- **Nombres de rama:** inglés o español pero **solo ASCII** — sin tildes, sin ñ (ej: `feature/email-verification`, no `feature/verificación-email`)

---

## Arquitectura del Proyecto

Clean Architecture + MVVM. Tres capas:

```
es/virtualclubs/
├── App.kt                          HiltAndroidApp — punto de entrada
├── Activities.kt                   MainActivity, ResetPasswordActivity, VerifyEmailActivity
├── VirtualClubsMainApp.kt          Composable raíz (tema + NavHost)
│
├── data/                           CAPA DE DATOS
│   ├── local/
│   │   ├── datastore/
│   │   │   ├── AppPreferences.kt   Tema oscuro, contraste, tamaño fuente
│   │   │   └── UserPreferences.kt  Email, autologin flag
│   │   └── secure/
│   │       ├── EncryptionUtils.kt          AES/GCM para tokens
│   │       └── SecureUserPreferences.kt    Almacenamiento encriptado de tokens
│   ├── managers/
│   │   ├── GlobalUIManager.kt      Estado global: loading, errores, diálogos
│   │   ├── SafeCall.kt             Wrapper para llamadas API
│   │   └── SafeResponse.kt         Manejo de respuestas + refresh automático de token
│   ├── models/
│   │   └── User.kt                 User(email: String?)
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApi.kt          Endpoints de autenticación
│   │   │   ├── RefreshApi.kt       Endpoint de refresh de token
│   │   │   └── UserApi.kt          Endpoint de info de usuario
│   │   └── dto/                    DTOs de request/response
│   └── repository/                 Implementaciones de repositorios
│
├── di/                             INYECCIÓN DE DEPENDENCIAS (Hilt)
│   ├── NetworkModule.kt            Retrofit + OkHttp + interceptores
│   ├── PreferencesModule.kt        DataStore providers
│   ├── SessionModule.kt
│   ├── UseCaseModule.kt            Todos los use cases (@Singleton)
│   └── GlobalUIEntryPoint.kt
│
├── domain/                         CAPA DE DOMINIO
│   ├── model/
│   │   ├── AuthInterceptor.kt      Inyecta Bearer token en requests
│   │   ├── AuthTokens.kt           accessToken + refreshToken
│   │   ├── Endpoint.kt             Constantes de rutas HTTP agrupadas por módulo
│   │   ├── ErrorType.kt            Enum con 26 tipos de error
│   │   └── VirtualClubException.kt Excepción custom del proyecto
│   ├── repository/                 Interfaces de repositorios
│   └── usecase/                    Casos de uso (lógica de negocio)
│       ├── AuthUseCase.kt
│       ├── GoogleUseCase.kt
│       ├── LogoutUserUseCase.kt
│       ├── RefreshTokenUseCase.kt
│       ├── RegisterUseCase.kt
│       └── token/                  GetAccessToken, GetRefreshToken, SaveTokens, ClearTokens
│
├── presentation/                   CAPA DE PRESENTACIÓN
│   ├── components/                 Componentes reutilizables
│   │   ├── AppBar.kt
│   │   ├── Buttons.kt
│   │   ├── Scaffold.kt
│   │   ├── TextFields.kt
│   │   └── dialogs/
│   │       └── EmailNotVerifiedDialog.kt
│   ├── handlers/
│   │   └── ErrorHandler.kt         ErrorType → string resource
│   ├── navigation/
│   │   ├── AppNavigator.kt         Control estático de navegación
│   │   ├── NavGraph.kt             NavHost con todas las rutas
│   │   ├── Screen.kt               Rutas selladas del proyecto
│   │   └── SessionManager.kt       Manejo de logout global
│   ├── screens/
│   │   ├── auth/                   Login/Register (AuthPage + AuthViewModel)
│   │   ├── home/                   Home principal (HomePage + HomeViewModel)
│   │   ├── resetPassword/          Reset de contraseña
│   │   ├── settings/               Configuración de tema/fuente
│   │   └── verifyemailresult/      Resultado de verificación de email
│   └── theme/
│       ├── Color.kt                Paleta Material 3 (light/dark/contrast)
│       ├── Shape.kt
│       ├── Theme.kt                VirtualClubsTheme composable
│       └── Type.kt                 Tipografía
│
└── session/
    └── UserSession.kt              Estado en memoria del usuario actual
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

## Tipos de Error (ErrorType enum — 26 tipos)

`INTERNAL_ERROR`, `INVALID_CREDENTIALS`, `USERNAME_NOT_FOUND`, `EMAIL_ALREADY_EXISTS`, `EMAIL_REQUIRED`, `PASSWORD_REQUIRED`, `USERNAME_REQUIRED`, `PASSWORD_MIN_LENGTH_ERROR`, `INVALID_EMAIL_FORMAT`, `FIELD_NULL`, `TOKEN_BLANK`, `INVALID_REFRESH_TOKEN`, `INVALID_GOOGLE_TOKEN`, `FAILED_SEND_EMAIL`, `INVALID_TOKEN`, `NO_LOCAL_PROVIDER`, `EMAIL_NOT_FOUND`, `INVALID_ACCESS_TOKEN`, `CANT_CONNECT_SERVER`, `MISSING_TOKENS`, `GOOGLE_SIGN_IN_FAILED`, `PASSWORD_NOT_EQUALS`, `GOOGLE_SIGN_IN_NO_TOKEN`, `GOOGLE_LOGIN_EXCEPTION`, `USER_NOT_FOUND`, `EMAIL_NOT_VERIFIED`

---

## Build Flavors y Comandos

| Flavor | Descripción |
|--------|-------------|
| `dev` | BASE_URL apunta a Render (dev), Google Client ID hardcodeado |
| `prod` | BASE_URL + Google Client ID desde `secrets.properties` |

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

**Suite base implementada en `app/src/test/java/es/virtualclubs/`:**

| Archivo | Qué cubre |
|---------|-----------|
| `data/managers/SafeResponseTest.kt` | Happy path, 401+refresh, IOException, 4xx/5xx fallback |
| `domain/usecase/AuthUseCaseTest.kt` | Login exitoso, credenciales inválidas, delegación al repositorio |
| `domain/usecase/RefreshTokenUseCaseTest.kt` | Refresh exitoso, token inválido, `canLogout` flag |
| `presentation/screens/auth/AuthViewModelTest.kt` | loginUser, autoLogin con/sin token válido |
| `utils/MainDispatcherRule.kt` | Rule para reemplazar `Dispatchers.Main` en tests con coroutines |

**Dependencias de test:**
- `mockk` 1.13.13 — mocks en Kotlin
- `kotlinx-coroutines-test` 1.9.0 — coroutines en tests
- `testOptions.unitTests.isReturnDefaultValues = true` — evita crash de stubs Android (e.g. `Log.d`) en JVM

**Nota:** `AuthViewModel` usa `by lazy` para `googleSignInClient` para que el test pueda instanciarse sin Play Services en JVM. Los tests de flujo Google Sign-In deben ir en `androidTest/`.

```bash
```

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
- Siempre retornan `Result<T>` o usan `SafeResponse`

### Use Cases
- Un archivo por use case en `domain/usecase/`
- `@Singleton` en `UseCaseModule`
- `operator fun invoke(...)` como punto de entrada

### Gestión de errores
- `VirtualClubException(type: ErrorType)` para errores de negocio
- `SafeResponse` captura excepciones de red y delega refresh de token
- `GlobalUIManager` maneja el estado de loading/error/diálogos a nivel global
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
| Home screen vacía | HomePage actual es placeholder, falta contenido real de clubes |
| ProGuard no configurado | `isMinifyEnabled = false` en release — falta configurar R8/ProGuard |
| Certificate pinning ausente | No hay pinning de certificados SSL en OkHttp |
| Tests UI sin cobertura | No hay tests de UI/instrumentación implementados (solo tests unitarios) |
| UserApi limitada | `getUserInfo` solo devuelve email — falta expandir para clubs, config, etc. |
| Credentials API migración | Se usa `play-services-auth` legacy + nuevo `credentials` — consolidar en uno |
| `prod` flavor sin URL real | Ambos flavors apuntan a Render — falta URL de producción propia |
