# VirtualClubs Android

[![CI](https://github.com/RaulGL422/VirtualClubs-FrontAndroid/actions/workflows/ci.yml/badge.svg)](https://github.com/RaulGL422/VirtualClubs-FrontAndroid/actions/workflows/ci.yml)
![Min SDK](https://img.shields.io/badge/min%20SDK-30-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)

> [Read in English](README.md)

Cliente Android para **VirtualClubs** — una plataforma para crear y gestionar clubes de aficionados deportivos. Los usuarios pueden unirse a clubes por deporte, conectar con otros miembros, organizar eventos, gestionar asistencia y seguir la actividad del club, todo desde el móvil.

> **Estado:** Alpha v0.1.2 — Autenticación completa. Gestión de clubes en desarrollo activo.  
> Repositorio backend: [VirtualClubs-BackEnd](https://github.com/RaulGL422/VirtualClubs-BackEnd)

---

## Funcionalidades

### Autenticación (completa)
- Login y registro con email y contraseña
- Google Sign-In mediante **CredentialManager** (API moderna — no la legacy `play-services-auth`)
- Auto-login al iniciar usando refresh token — no hay que volver a iniciar sesión al cerrar la app
- Restablecimiento de contraseña por email + deep link
- Verificación de email por deep link
- Logout seguro con limpieza completa de tokens y sesión

### Seguridad
- Tokens de acceso y refresco cifrados con **AES/GCM + AndroidKeyStore** — nunca en `SharedPreferences` plano
- **Certificate pinning** en builds de producción (OkHttp) — previene ataques MITM
- Comprobación local de expiración JWT en `AuthInterceptor` antes de cada petición — evita round-trips de 401 innecesarios
- Refresco automático de token en 401 con protección de concurrencia — peticiones paralelas no lanzan múltiples refreshes

### UI e Infraestructura
- Gestor de estado global de UI (`GlobalUIManager`) — loading, diálogos de error y snackbars desde una única fuente de verdad, inyectable en cualquier lugar sin pasar callbacks por el árbol
- **Stadium Design System** — capa de tokens M3 personalizada con tipografía Poppins + Roboto, paleta de 10 niveles (Electric Blue / Fire Orange / Stadium Green / Cool Blue-Gray) y tokens semánticos de espaciado, elevación, forma, movimiento y gradientes en 6 esquemas de color (claro, oscuro, alto/medio contraste por modo)
- Tema dinámico Material 3: modos claro / oscuro / alto contraste
- Deep links gestionados en Activities dedicadas para evitar contaminar el NavGraph
- Build flavors: `dev` (backend `api-vc.rgal.dev`) / `prod` (certificate pinning activo)
- Selector de URL de servidor solo en debug en Ajustes — configura cualquier IP de backend local en tiempo de ejecución sin recompilar

---

## Decisiones técnicas destacadas

**Refresco de token sin condiciones de carrera** — `SafeResponse` intercepta las respuestas 401 y dispara el refresco de token. Si varias peticiones fallan a la vez, solo se hace una llamada de refresco. Las demás esperan y reintentan con el nuevo token.

**Sin `runBlocking` en la ruta caliente** — Los tokens de acceso se cachean en memoria (`UserSession`) tras la primera lectura. `AuthInterceptor` lee síncronamente desde la caché, evitando `runBlocking` y latencia extra en cada petición. La única llamada `runBlocking` está en `NetworkModule` en el momento de construcción del grafo Hilt (solo en builds de debug, para leer la URL del servidor de desarrollo desde DataStore antes de que se haga ninguna petición).

**Interfaz `ErrorDispatcher`** — `GlobalUIManager` implementa una interfaz `ErrorDispatcher` definida en la capa de dominio. Los casos de uso despachan errores sin saber nada de la UI, manteniendo la dirección correcta de dependencias.

**Fakes de repositorio en lugar de mocks en tests** — Los fakes son implementaciones reales y configurables. Los tests son más legibles, los refactors no rompen el setup de tests y el comportamiento es más cercano a producción que lo que ofrecen los mocks.

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Arquitectura | Clean Architecture + MVVM |
| DI | Hilt 2.59.2 |
| Navegación | Jetpack Navigation Compose + deep links |
| Red | Retrofit + OkHttp (certificate pinning) |
| Auth | Google CredentialManager |
| Almacenamiento seguro | AndroidKeyStore + cifrado AES/GCM |
| Almacenamiento local | DataStore Preferences |
| Testing | MockK + Kotlin Fakes + Coroutines Test |
| Lenguaje | Kotlin 2.3.21 |
| Min SDK | 30 (Android 11) |

---

## Arquitectura

Clean Architecture en tres capas. Las dependencias apuntan hacia adentro — `presentation` depende de `domain`, `data` depende de `domain`, nada depende de `presentation`.

```
presentation/   Pantallas Compose, ViewModels, navegación, GlobalUIManager
domain/         Casos de uso, interfaces de repositorio, modelos de dominio, ErrorType, ErrorDispatcher
data/           Implementaciones de repositorio, APIs Retrofit, DTOs, almacenamiento local cifrado
```

```
es.virtualclubs/
├── data/
│   ├── local/          # Preferencias DataStore + almacenamiento cifrado AES/GCM de tokens
│   ├── managers/       # SafeCall (despacho de errores), SafeResponse (red + refresco)
│   ├── remote/         # Interfaces API Retrofit + DTOs
│   └── repository/     # Implementaciones de repositorio
├── di/                 # Módulos Hilt
├── domain/
│   ├── model/          # AuthInterceptor, ErrorType (27 tipos), ErrorDispatcher, constantes Endpoint
│   ├── repository/     # Interfaces de repositorio
│   └── usecase/        # Un archivo por caso de uso — la lógica de negocio vive aquí, no en los ViewModels
└── presentation/
    ├── components/     # Componentes Compose reutilizables (botones, campos de texto, scaffold, diálogos)
    ├── managers/       # GlobalUIManager — loading, errores, diálogos como singleton inyectable
    ├── navigation/     # NavGraph, clase sellada Screen, AppNavigator, SessionManager
    ├── screens/        # una carpeta por pantalla — cada carpeta puede contener subcarpetas components/ y dialogs/
│   │   ├── auth/           # AuthPage, AuthViewModel
│   │   │   ├── components/ # AuthDivider, AuthToggle, SocialButtons (internos a auth)
│   │   │   └── dialogs/    # ForgotPasswordDialog
│   │   ├── home/           # HomePage, HomeViewModel
│   │   ├── settings/       # SettingsPage, SettingsViewModel
│   │   ├── resetPassword/  # ResetPasswordPage, ResetPasswordViewModel
│   │   └── verifyemailresult/ # VerifyEmailResultPage, VerifyEmailResultViewModel
    └── theme/          # Colores M3, tipografía, espaciado, formas
```

---

## Tests

Cobertura de tests unitarios para toda la lógica de negocio principal:

```
data/managers/    SafeCall — propagación de errores al ErrorDispatcher
                  SafeResponse — happy path, 401+refresh, IOException, fallback 4xx/5xx
domain/usecase/   Auth, Register, Google Sign-In, Logout, RefreshToken, GetUserInfo
                  SaveTokens, GetAccessToken, GetRefreshToken, ClearTokens
```

**Estrategia:**
- **Fakes** para repositorios — implementaciones reales configurables en `test/fakes/`, sin mocks
- **MockK** solo para clases con dependencias Android (`SecureUserPreferences`, `GlobalUIManager`)
- `MainDispatcherRule` en todos los tests que usen coroutines o `StateFlow`

```bash
./gradlew testDevDebugUnitTest
```

---

## API

Base URL: `https://virtualclubs-backend.onrender.com/` (producción) · `https://api-vc.rgal.dev/` (flavor dev)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/v1/auth/login` | Login con email y contraseña |
| POST | `/v1/auth/register` | Registro de usuario |
| DELETE | `/v1/auth/logout` | Cerrar sesión |
| POST | `/v1/auth/google` | Google Sign-In |
| POST | `/v1/auth/requestPasswordReset` | Solicitar email de restablecimiento de contraseña |
| POST | `/v1/auth/resetPassword` | Aplicar restablecimiento con token |
| POST | `/v1/auth/requestVerify` | Solicitar verificación de email |
| POST | `/v1/auth/refresh` | Refrescar token de acceso |
| GET | `/v1/user/getUserInfo` | Info del usuario autenticado |

---

## Configuración

### Requisitos previos
- Android Studio **Hedgehog** o posterior
- JDK 17+
- Un proyecto en Google Cloud con un Client ID OAuth2 de tipo Web

### 1. Clonar el repositorio

```bash
git clone https://github.com/RaulGL422/VirtualClubs-FrontAndroid.git
cd VirtualClubs-FrontAndroid
```

### 2. Configurar secretos

```bash
cp app/secrets.properties.example app/secrets.properties
```

Edita `app/secrets.properties`:

```properties
GOOGLE_CLIENT_ID=tu_client_id_web_oauth2_aqui
```

> Usa el tipo **Web client** de Google Cloud Console, no el cliente Android. El cliente Android se vincula automáticamente mediante el nombre del paquete y el fingerprint SHA-1.

### 3. Ejecutar

Abre el proyecto en Android Studio, selecciona la variante `devDebug`, conecta un dispositivo o emulador (API 30+) y pulsa **Run**.

### Backend local (opcional)

Para apuntar un build de debug a un backend local sin recompilar, abre **Ajustes → Servidor de desarrollo**, introduce la dirección (p. ej. `192.168.1.50:3000` — `http://` y la `/` final se añaden automáticamente) y pulsa **Guardar y reiniciar**.

- **Emulador:** usa `10.0.2.2` para llegar al `localhost` de tu máquina
- **Dispositivo físico:** usa la IP LAN de tu máquina; ejecuta `adb reverse tcp:PUERTO tcp:PUERTO` si el dispositivo está conectado por USB

---

## Variantes de build

| Variante | Descripción |
|---------|-------------|
| `devDebug` | Desarrollo — backend `api-vc.rgal.dev`, sin certificate pinning |
| `prodRelease` | Producción — `virtualclubs-backend.onrender.com`, certificate pinning activo |

```bash
./gradlew compileDevDebugKotlin   # Verificar errores Kotlin
./gradlew assembleDevDebug        # Compilar APK de debug
./gradlew testDevDebugUnitTest    # Ejecutar tests unitarios
./gradlew clean
```

---

## Roadmap

| Funcionalidad | Estado |
|---|---|
| Autenticación (login, registro, Google, reset, verificación) | ✅ Completo |
| Almacenamiento seguro de tokens + auto-refresco | ✅ Completo |
| Tests unitarios — casos de uso y capa de red | ✅ Completo |
| Pantalla home — listado de clubes | 🔄 En progreso |
| Creación y gestión de clubes | ⏳ Planificado |
| Programación de eventos | ⏳ Planificado |
| Gestión de miembros | ⏳ Planificado |
| Control de asistencia | ⏳ Planificado |
| Tests de UI / instrumentación | ⏳ Planificado |

---

## Autor

**Raul Galindo Lopez**  
[LinkedIn](https://www.linkedin.com/in/raul-gldev/) · [GitHub](https://github.com/RaulGL422)

---

## Licencia

© 2025 Raul Galindo Lopez. Todos los derechos reservados.  
Visible públicamente con fines de portfolio. No autorizado para redistribución ni uso comercial.
