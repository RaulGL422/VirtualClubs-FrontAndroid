# Contributing to VirtualClubs Android

Thank you for your interest in contributing. This document explains the conventions we follow so that the codebase stays consistent.

---

## Branch naming

Branches must be created from `development` (never from `main`):

| Type | Prefix | Example |
|------|--------|---------|
| New feature | `feature/` | `feature/club-event-management` |
| Bug fix | `fix/` | `fix/refresh-token-on-network-change` |
| Tests | `test/` | `test/auth-viewmodel-coverage` |
| Maintenance / config | `chore/` | `chore/update-compose-bom` |
| Documentation | `docs/` | `docs/readme-setup-section` |

**Rules:**
- ASCII only — no accents, no `ñ`, no special characters
- Use hyphens, not underscores or spaces
- Max 55 characters total
- Never commit directly to `main` or `development`

---

## Commit format

We use **Conventional Commits** in **English**, imperative mood:

```
type(scope): short description

[optional body]
```

**Types:** `feat`, `fix`, `refactor`, `chore`, `docs`, `test`, `style`, `perf`

**Scopes:** `auth`, `navigation`, `theme`, `home`, `settings`, `user`, `network`, `di`, `deps`, `components`

**Examples:**
```
feat(auth): add email verification result screen
fix(network): handle 401 race condition in SafeResponse
chore(deps): update Compose BOM to 2026.03.00
test(usecase): add RefreshTokenUseCase failure cases
```

---

## Opening a pull request

1. Create a branch from `development` following the naming convention above
2. Make your changes — keep each PR focused on one concern
3. Verify the build passes: `./gradlew compileDevDebugKotlin`
4. Run the tests: `./gradlew testDevDebugUnitTest`
5. Push and open a PR targeting `development`
6. Fill in all sections of the PR template — empty descriptions will be rejected

---

## Code style

This project follows the official [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html). A few project-specific rules:

- **Layer boundaries must be respected.** `Composable → ViewModel → UseCase → Repository`. No skipping.
- **Business logic lives in use cases**, not in ViewModels or repositories.
- **ViewModels** must not hold references to `Context`, `Activity`, or any `Composable` type.
- **Expose state** as immutable `StateFlow`, never as public `MutableStateFlow`.
- **Side effects** (navigation, toasts) use `SharedFlow` / `Channel`, not direct calls from composables.
- **Tokens** are always stored in `SecureUserPreferences` — never in plain `SharedPreferences` or logs.
- **BuildConfig fields** (`BASE_URL`, `GOOGLE_CLIENT_ID`) are the only way to reference environment-specific values in code.
- **New composables** in `presentation/components/` must include a `@Preview`.
- **New screens** must add their route to both `Screen.kt` and `NavGraph.kt`.
- **New error types** must have a corresponding case in `ErrorHandler.kt`.
- **New use cases** must be registered as `@Singleton` in `UseCaseModule`.

---

## Security

If you find a security vulnerability, please **do not open a public issue**. Read our [Security Policy](SECURITY.md) for responsible disclosure instructions.
