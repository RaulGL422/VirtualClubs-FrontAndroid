# /check-security — Android Security Audit

Performs a complete security audit of the project, focused on the most common vulnerabilities in Android apps.

## Usage
- `/check-security` — audits the entire project
- `/check-security AuthViewModel` — audits a specific file

---

## Step 1: Gather information

Read the key security files in the project:
- `data/local/secure/SecureUserPreferences.kt` — token storage
- `data/local/secure/EncryptionUtils.kt` — encryption implementation
- `di/NetworkModule.kt` — network configuration
- `data/managers/AuthInterceptor.kt` — token handling in requests
- `data/managers/SafeResponse.kt` — network error handling
- `AndroidManifest.xml` — permissions and configuration
- `app/build.gradle.kts` — build configuration (minify, debuggable)

If a specific file was passed, read it as well.

---

## Step 2: Audit by category

### A1 — Insecure Data Storage
- [ ] Are access and refresh tokens stored in `SecureUserPreferences` (AES/GCM with AndroidKeyStore)?
- [ ] Are there no tokens or passwords in unencrypted `SharedPreferences`?
- [ ] Are there no sensitive data in unencrypted files or in the cache?
- [ ] Are cryptographic keys generated with `AndroidKeyStore` and not exported?

### A2 — Insecure Communication
- [ ] Is HTTPS used in all API calls (`BASE_URL` with `https://`)?
- [ ] Is certificate pinning configured in OkHttp? (prod flavor only)
- [ ] Is `HttpLoggingInterceptor` in `HEADERS` or `NONE` mode in release? (not `BODY`, which exposes tokens)
- [ ] Is `isDebuggable` set to `false` in release?

### A3 — Insecure Authentication
- [ ] Does `AuthInterceptor` add the Bearer token correctly?
- [ ] Does the automatic token refresh work without exposing the refresh token in logs?
- [ ] Are token expiration times handled correctly?
- [ ] Does logout clear all stored tokens (`ClearTokensUseCase`)?

### A4 — Sensitive Data Exposure
- [ ] Are there no tokens, passwords, or PII in `Log.d`, `Log.e`, or `println`?
- [ ] Are there no sensitive data in Intent extras or navigation arguments?
- [ ] Are there no hardcoded credentials in the source code?
- [ ] Does `GOOGLE_CLIENT_ID` come from `secrets.properties` and is it not in the repository?

### A5 — Insecure Deep Links
- [ ] Do deep links (`virtualclubs://`) validate received parameters before using them?
- [ ] Is the password reset token validated in the backend before being accepted?
- [ ] Is it impossible to inject routes or malicious data via deep links?

### A6 — Excessive Permissions
- [ ] Does `AndroidManifest.xml` only declare strictly necessary permissions?
- [ ] Are there no unnecessary sensitive permissions (e.g., `READ_PHONE_STATE`)?

### A7 — Build Configuration
- [ ] Is `isMinifyEnabled = true` and ProGuard configured in the release build type?
- [ ] Is `isDebuggable = false` in release builds?
- [ ] Is there no `allowBackup="true"` in the Manifest if the app stores sensitive data?

### A8 — Components with Known Vulnerabilities
- [ ] Are dependencies up to date? (run `/update-deps` if in doubt)
- [ ] Is `androidx.security:security-crypto` at a recent version?

### A9 — Coroutine / Threading Hygiene
- [ ] Is there no `runBlocking` outside of `NetworkModule` (the only allowed exception, dev-build Hilt graph construction)? A stray `runBlocking` on the main thread can freeze the UI and, on auth-related paths, create races with token refresh.

---

## Step 3: Search in the code

Use Grep to search for problematic patterns:

```bash
# Tokens or passwords in logs
grep -rn "Log\." app/src/main/java --include="*.kt" | grep -i "token\|password\|secret\|key"

# Unencrypted SharedPreferences with sensitive data
grep -rn "getSharedPreferences\|SharedPreferences" app/src/main/java --include="*.kt"

# Hardcoded credentials
grep -rn "password\s*=\|secret\s*=\|apikey\s*=" app/src/main/java --include="*.kt" -i

# println or System.out
grep -rn "println\|System\.out" app/src/main/java --include="*.kt"

# HTTP instead of HTTPS
grep -rn "http://" app/src/main/java --include="*.kt"

# runBlocking outside NetworkModule (only allowed exception)
grep -rln "runBlocking" app/src/main/java --include="*.kt" | grep -v "di/NetworkModule.kt"
```

---

## Step 4: Generate report

```
## Security Audit — Virtual Clubs Android — [date]

### 🔴 Critical (fix immediately)
[list or "None found"]

### 🟡 Important (fix before release)
[list with file:line reference]

### 🟢 Informational (recommended improvements)
[list]

### ✅ Correct
[what is well implemented]

---

### Known Security Debt (from CLAUDE.md)
[items from the Known Technical Debt table]

### Recommended next steps
1. [concrete action with priority]
2. [concrete action]
```

If critical issues are found, propose the concrete fix with code.
