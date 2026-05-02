# /review-pr — Pull Request Review

Reviews the current PR (or a specified one) exhaustively, focused on Android/Jetpack Compose.

## Usage
- `/review-pr` — reviews the PR for the current branch
- `/review-pr 42` — reviews PR number 42

## Step 1: Identify the PR

If a number was passed as argument, use that PR.

Otherwise, run:
```
git branch --show-current
gh pr list --head [current-branch] --json number,title,url,baseRefName
```

If no PR exists for the current branch, inform the user and stop.

## Step 2: Get PR information

Run:
```
gh pr view [number] --json title,body,baseRefName,headRefName,additions,deletions,changedFiles,commits
gh pr diff [number]
```

## Step 3: Review the code (exhaustive analysis)

Analyze the full diff evaluating **all** of the following categories:

---

### 🔴 CRITICAL ERRORS (block merge)

- Potential crash: NullPointerException in Composables, StateFlow collected before initialization
- Incorrect or broken business logic
- Broken navigation (malformed routes, undecodified arguments)
- `TODO`, `FIXME`, `HACK`, `Log.d`, `println` left in the diff
- Empty PR description or missing required sections
- Broken compilation (obvious syntax errors)
- Coroutines not cancelled or launched in wrong scope

---

### 🔐 ANDROID SECURITY

- [ ] **Token storage:** Are tokens stored in `SecureUserPreferences` (AES/GCM) and NOT in plain `SharedPreferences`?
- [ ] **Data in logs:** Are there tokens, passwords, or user data in `Log.d/e/i`?
- [ ] **Deep links:** Do deep links validate parameters before using them?
- [ ] **Manifest permissions:** Are only strictly necessary permissions added?
- [ ] **Hardcoded credentials:** Are there secrets, API keys, or passwords in the code?
- [ ] **BuildConfig correctly used:** Do URLs and keys use `BuildConfig.BASE_URL` and `BuildConfig.GOOGLE_CLIENT_ID`?
- [ ] **Sensitive data in navigation arguments:** Are tokens avoided in navigation arguments/Bundle?

---

### 📚 DOCUMENTATION

- [ ] Do new reusable composables have `@Preview`?
- [ ] Do new public or complex functions have KDoc?
- [ ] Was `CLAUDE.md` updated if new routes, endpoints, or patterns were added?
- [ ] Do new `ErrorType` values have their case in `ErrorHandler.kt`?

---

### 🏗️ ARCHITECTURE AND BAD PRACTICES

- Is layer separation respected? (Composable → ViewModel → UseCase → Repository — no layer skipping)
- Does the ViewModel have **no** references to Context, Activity, or Composables?
- Is ViewModel state exposed as immutable `StateFlow` (not public `MutableStateFlow`)?
- Are side effects (navigation, toasts) handled with `SharedFlow`/`Channel`, not directly from Composable?
- Is `hiltViewModel()` used correctly in composables?
- Do use cases have `operator fun invoke(...)` and contain the business logic?
- Do repositories only do data mapping, not business logic?
- Is there duplicated code that could be extracted to components or utilities?
- Are variable, function, and class names descriptive?
- Unused imports or unnecessary commented-out code?
- Are new reusable composables in `presentation/components/`, not inlined?

---

### ⚡ PERFORMANCE AND COMPOSE

- Are there unnecessary recompositions? (non-remembered lambdas, new objects on every recomposition)
- Is `remember { }` used correctly for values that should not be recalculated?
- Do `LazyColumn`/`LazyRow` use `key` correctly?
- Are heavy IO or CPU operations on `Dispatchers.IO` or `Dispatchers.Default`?
- Are Flows collected with `collectAsStateWithLifecycle()` (not `collectAsState()`) to respect lifecycle?
- Is `rememberCoroutineScope` only used when necessary (not to replace `viewModelScope`)?

---

### ✅ TESTS

- Does the new ViewModel or UseCase have unit tests in `src/test/`?
- Are error cases covered in addition to the happy path?
- Do new UI tests use the Compose Testing framework?
- **Note:** Tests cannot be run from the diff alone. If there are new tests, they must be run with `./gradlew testDevDebugUnitTest` before merge.

---

## Step 4: Generate report

Present the result in this format:

```
## PR Review #[number]: [title]

**Base:** [base-branch] ← [head-branch]
**Changes:** +[additions] / -[deletions] in [N] files

---

### 🔴 Critical Errors
[list or "None found"]

### 🔐 Security Issues
[list or "None found"]

### 📚 Missing Documentation
[list or "Complete"]

### 🏗️ Bad Practices
[list or "None found"]

### ⚡ Performance / Compose Issues
[list or "None found"]

### ✅ Test Status
[observations]

---

### Verdict
[APPROVED / APPROVED WITH SUGGESTIONS / CHANGES REQUIRED]

### Suggested next steps:
1. [concrete action]
2. [concrete action]
```

If there are critical errors or security issues, explain the risk and propose the concrete fix with code.

---

## Step 5: Update documentation if applicable

After generating the report, read `CLAUDE.md` and determine if the PR introduces changes that leave it outdated. Evaluate:

**Navigation Routes:** Does the PR add, remove, or modify routes in `Screen.kt`/`NavGraph.kt`? Update the table.

**API Endpoints Consumed:** Does the PR consume new endpoints or modify existing calls? Update the table.

**Known Technical Debt:** Does the PR resolve or introduce technical debt? Update the table.

**Patterns & Conventions:** Does the PR introduce a new pattern? Document it.

If nothing needs to be updated, state: `"CLAUDE.md is up to date, no changes required."`

If there are changes, apply them directly and show a summary:
```
### Documentation updated
- [section]: [what changed]
```
