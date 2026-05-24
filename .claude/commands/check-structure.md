# /check-structure — Architecture & Structure Compliance Check

Checks that the changes in the current branch comply with the project's architecture rules, naming conventions, and structural patterns defined in CLAUDE.md.

## Usage
- `/check-structure` — checks all changes in the current branch vs `development`
- `/check-structure main` — checks vs `main` instead

---

## Step 1: Identify changed files

Run in parallel:
```bash
git branch --show-current
git diff development...HEAD --name-only   # or vs the base passed as argument
git diff development...HEAD --stat
```

If there are no changed files, inform the user and stop:
> "No changes detected vs `development`. Make sure you are on a feature branch."

---

## Step 2: Read the diff and classify files

Run:
```bash
git diff development...HEAD
```

For each changed or new file, classify it into one of these categories:
- **Screen** — files in `presentation/screens/*/`
- **ViewModel** — files named `*ViewModel.kt`
- **UseCase** — files in `domain/usecase/`
- **Repository** — files in `domain/repository/` or `data/repository/`
- **Composable** — files in `presentation/components/` or `*Page.kt`
- **Navigation** — `Screen.kt`, `NavGraph.kt`, `AppNavigator.kt`
- **DI** — files in `di/`
- **Test** — files in `src/test/` or `src/androidTest/`
- **Config/CI** — `*.gradle.kts`, `*.yml`, `AndroidManifest.xml`
- **Other** — everything else

---

## Step 3: Run checks by category

Apply only the checks relevant to the files that actually changed.

---

### 🏗️ Layer Boundaries (always check)

Scan the diff for illegal cross-layer imports:

```bash
# Composables importing ViewModels from other screens (should pass lambdas)
# UseCases importing from presentation/
# Repositories containing business logic (if/when chains beyond mapping)
```

Check rules:
- [ ] No file in `domain/` imports from `presentation/`
- [ ] No file in `presentation/` imports directly from `data/repository/` (must go through UseCases)
- [ ] No `runBlocking` added outside `NetworkModule.kt`
- [ ] No `Context`, `Activity`, or `Composable` types referenced inside a ViewModel

---

### 📱 New Screens

If new files appear in `presentation/screens/*/`:

- [ ] The screen folder contains at minimum `[Name]Page.kt` and `[Name]ViewModel.kt`
- [ ] The screen's route is added to `Screen.kt`
- [ ] The screen's route is added to `NavGraph.kt` inside `AppNavHost`
- [ ] If the screen has internal composables, they are in a `components/` subfolder with `internal` visibility
- [ ] If the screen has dialogs, they are in a `dialogs/` subfolder
- [ ] All files in the screen folder use the **same package declaration** (e.g. `package es.virtualclubs.presentation.screens.auth`)

---

### 🧠 ViewModels

If `*ViewModel.kt` files changed or were created:

- [ ] Annotated with `@HiltViewModel` and `@Inject constructor`
- [ ] State exposed as `val uiState: StateFlow<...>` (not `MutableStateFlow` directly)
- [ ] `MutableStateFlow` is private (`private val _uiState`)
- [ ] Side effects use `SharedFlow` or `Channel`, not direct composable calls
- [ ] No references to `Context`, `Activity`, or any Composable type in the constructor or body
- [ ] Uses `viewModelScope.launch` for coroutines, not custom scopes

---

### ⚙️ Use Cases

If files in `domain/usecase/` changed or were created:

- [ ] Class annotated with `@Singleton` and uses `@Inject constructor`
- [ ] Has `operator fun invoke(...)` as the entry point
- [ ] Contains business logic — not just a pass-through to the repository
- [ ] Does **not** import from `presentation/`

---

### 🗄️ Repositories

If files in `domain/repository/` or `data/repository/` changed or were created:

- [ ] Interface lives in `domain/repository/`
- [ ] Implementation lives in `data/repository/`
- [ ] Implementation only does data mapping — no `if/when` business logic chains
- [ ] Returns `Result<T>` or delegates to `SafeResponse`

---

### 🧭 Navigation

If `Screen.kt` or `NavGraph.kt` changed:

- [ ] Every new route in `Screen.kt` has a corresponding `composable(...)` in `NavGraph.kt`
- [ ] Every new `composable(...)` in `NavGraph.kt` has its route defined in `Screen.kt`
- [ ] Deep link `uriPattern` in `NavGraph.kt` matches the `<data>` element in `AndroidManifest.xml`
- [ ] Navigation arguments use `NavType.StringType` or appropriate type — no raw objects passed
- [ ] CLAUDE.md navigation table is updated with new routes

---

### 🎨 Composables

If new composable functions appear (`@Composable` annotation in diff):

- [ ] New **reusable** composables (in `presentation/components/`) have a `@Preview`
- [ ] `modifier: Modifier = Modifier` is the first parameter after state/callbacks
- [ ] Composables do **not** call `hiltViewModel()` if they are child composables — state is passed via parameters
- [ ] Screen-specific composables are marked `internal`

---

### ❌ Error Types

If `ErrorType.kt` changed (new enum values added):

- [ ] Each new `ErrorType` value has a corresponding `else ->` or explicit `when` branch in `ErrorHandler.kt`
- [ ] The new type has a string resource in `res/values/strings.xml`

---

### 🔐 Security (quick pass)

Always run a quick grep on the diff:

```bash
# Logs with sensitive data
grep -n "Log\." [changed_files] | grep -i "token\|password\|secret"

# runBlocking outside NetworkModule
grep -n "runBlocking" [changed_files]

# Hardcoded secrets
grep -n "http://" [changed_files]
```

- [ ] No tokens, passwords, or secrets in logs
- [ ] No new `runBlocking` outside `NetworkModule.kt`
- [ ] No HTTP (non-HTTPS) URLs hardcoded

---

### 🧪 Tests

For every new ViewModel or UseCase in the diff:

- [ ] A corresponding test file exists in `src/test/java/...`
- [ ] If no test exists, flag it as missing (do not block, but warn)

---

## Step 4: Generate report

```
## Structure Check — [branch-name] vs [base]
**Files changed:** [N] | **New files:** [N]

---

### 🔴 Violations (must fix before merge)
[list with file:line — or "None"]

### 🟡 Warnings (recommended to fix)
[list — or "None"]

### ✅ Rules passed
[summary of what was checked and found correct]

### ⏭️ Not applicable
[rules that did not apply because no relevant files changed]

---

### Verdict
[PASS / PASS WITH WARNINGS / FAIL]

### Next steps:
1. [concrete fix if violations exist]
2. [concrete fix]
```

If there are violations, show the exact problematic code and the corrected version.
