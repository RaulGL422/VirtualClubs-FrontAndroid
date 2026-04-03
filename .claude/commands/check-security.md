# /check-security — Auditoría de Seguridad Android

Realiza una auditoría de seguridad completa del proyecto, enfocada en las vulnerabilidades más comunes en apps Android.

## Uso
- `/check-security` — audita todo el proyecto
- `/check-security AuthViewModel` — audita un archivo específico

---

## Paso 1: Recopilar información

Lee los archivos clave de seguridad del proyecto:
- `data/local/secure/SecureUserPreferences.kt` — almacenamiento de tokens
- `data/local/secure/EncryptionUtils.kt` — implementación de cifrado
- `di/NetworkModule.kt` — configuración de red
- `domain/model/AuthInterceptor.kt` — manejo de tokens en requests
- `data/managers/SafeResponse.kt` — manejo de errores de red
- `AndroidManifest.xml` — permisos y configuración
- `app/build.gradle.kts` — configuración de build (minify, debuggable)

Si se especificó un archivo concreto, léelo también.

---

## Paso 2: Auditoría por categorías

### A1 — Almacenamiento Inseguro de Datos
- [ ] ¿Los tokens de acceso y refresh se guardan en `SecureUserPreferences` (AES/GCM con AndroidKeyStore)?
- [ ] ¿No hay tokens ni contraseñas en `SharedPreferences` sin cifrar?
- [ ] ¿No hay datos sensibles en archivos sin cifrar o en la caché?
- [ ] ¿Las claves criptográficas se generan con `AndroidKeyStore` y no se exportan?

### A2 — Comunicación Insegura
- [ ] ¿Se usa HTTPS en todas las llamadas a la API (`BASE_URL` con `https://`)?
- [ ] ¿Hay certificate pinning configurado en OkHttp? (actualmente NO — deuda técnica conocida)
- [ ] ¿El `HttpLoggingInterceptor` está en modo `HEADERS` o `NONE` en release? (no `BODY` que expone tokens)
- [ ] ¿`isDebuggable` está a `false` en release?

### A3 — Autenticación Insegura
- [ ] ¿El `AuthInterceptor` añade el token Bearer correctamente?
- [ ] ¿El refresh automático de token funciona sin exponer el refresh token en logs?
- [ ] ¿Los tokens tienen tiempo de expiración manejado correctamente?
- [ ] ¿El logout limpia todos los tokens almacenados (`ClearTokensUseCase`)?

### A4 — Exposición de Datos Sensibles
- [ ] ¿No hay tokens, contraseñas o PII en `Log.d`, `Log.e` o `println`?
- [ ] ¿No hay datos sensibles en los extras de Intents o argumentos de navegación?
- [ ] ¿No hay credenciales hardcodeadas en el código fuente?
- [ ] ¿El `GOOGLE_CLIENT_ID` en `prod` viene de `secrets.properties` y no está en el repositorio?

### A5 — Deep Links Inseguros
- [ ] ¿Los deep links (`virtualclubs://`) validan los parámetros recibidos antes de usarlos?
- [ ] ¿El token de reset de contraseña se valida en el backend antes de aceptarlo?
- [ ] ¿No es posible inyectar rutas o datos maliciosos via deep links?

### A6 — Permisos Excesivos
- [ ] ¿El `AndroidManifest.xml` solo declara los permisos estrictamente necesarios?
- [ ] ¿No hay `uses-permission android:name="android.permission.READ_PHONE_STATE"` u otros permisos sensibles innecesarios?

### A7 — Configuración de Build
- [ ] ¿`isMinifyEnabled = true` y ProGuard configurado en el flavor `release`? (actualmente NO — deuda técnica)
- [ ] ¿`isDebuggable = false` en release builds?
- [ ] ¿No hay `allowBackup="true"` en Manifest si contiene datos sensibles?

### A8 — Uso de Componentes con Vulnerabilidades Conocidas
- [ ] ¿Las dependencias están actualizadas? (ejecutar `/update-deps` si hay dudas)
- [ ] ¿Se usa `androidx.security:security-crypto` en versión reciente?

---

## Paso 3: Buscar en el código

Usa Grep para buscar patrones problemáticos:

```bash
# Tokens o contraseñas en logs
grep -rn "Log\." app/src/main/java --include="*.kt" | grep -i "token\|password\|secret\|key"

# SharedPreferences sin cifrar con datos sensibles
grep -rn "getSharedPreferences\|SharedPreferences" app/src/main/java --include="*.kt"

# Credenciales hardcodeadas
grep -rn "password\s*=\|secret\s*=\|apikey\s*=" app/src/main/java --include="*.kt" -i

# println o System.out
grep -rn "println\|System\.out" app/src/main/java --include="*.kt"

# HTTP en lugar de HTTPS
grep -rn "http://" app/src/main/java --include="*.kt"
```

---

## Paso 4: Generar reporte

```
## Auditoría de Seguridad — Virtual Clubs Android — [fecha]

### 🔴 Crítico (corregir inmediatamente)
[lista o "Ninguno encontrado"]

### 🟡 Importante (corregir antes del release)
[lista con referencia a archivo:línea]

### 🟢 Informativo (mejoras recomendadas)
[lista]

### ✅ Correcto
[lo que está bien implementado]

---

### Deuda de Seguridad Conocida (del CLAUDE.md)
- ProGuard/R8 no configurado en release
- Certificate pinning ausente en OkHttp
[otras]

### Próximas acciones recomendadas
1. [acción concreta con prioridad]
2. [acción concreta]
```

Si se encuentran problemas críticos, propón el fix concreto con código.
