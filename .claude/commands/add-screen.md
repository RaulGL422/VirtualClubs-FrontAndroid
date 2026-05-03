# /add-screen — Add New Screen

Adds a complete new screen to the app following the project conventions (Clean Architecture + MVVM + Hilt).

## Usage
- `/add-screen ClubDetail` — creates all files for the ClubDetail screen
- `/add-screen Settings` — creates a screen with its ViewModel

---

## Step 1: Read project context

Read the following files to understand the current state:
- `presentation/navigation/Screen.kt` — existing routes
- `presentation/navigation/NavGraph.kt` — existing navigation entries
- An existing screen (e.g., `presentation/screens/home/`) as reference

---

## Step 2: Determine what to create

For a screen named `[Name]`, the required files are:

| File | Location | Description |
|------|----------|-------------|
| `[Name]Page.kt` | `presentation/screens/[name]/` | Root Composable |
| `[Name]ViewModel.kt` | `presentation/screens/[name]/` | ViewModel with StateFlow |
| Route in `Screen.kt` | `presentation/navigation/` | Sealed class entry |
| Entry in `NavGraph.kt` | `presentation/navigation/` | `composable(Screen.[Name].route)` |

If the screen needs a new UseCase or repository, list them before creating files and confirm with the user.

---

## Step 3: Create the ViewModel

```kotlin
@HiltViewModel
class [Name]ViewModel @Inject constructor(
    private val [useCase]: [UseCase]
) : ViewModel() {

    private val _uiState = MutableStateFlow([Name]UiState())
    val uiState: StateFlow<[Name]UiState> = _uiState.asStateFlow()

    private val _events = Channel<[Name]Event>()
    val events = _events.receiveAsFlow()

    fun [action]() {
        viewModelScope.launch {
            // business logic delegated to use case
        }
    }
}

data class [Name]UiState(
    val isLoading: Boolean = false
)

sealed class [Name]Event {
    data object NavigateBack : [Name]Event()
}
```

**ViewModel rules:**
- No references to `Context`, `Activity`, or any Composable type
- State as immutable `StateFlow` — never expose `MutableStateFlow` publicly
- Side effects (navigation, toasts) via `Channel` / `SharedFlow`, not direct calls
- All business logic in use cases, not here

---

## Step 4: Create the Composable

```kotlin
@Composable
fun [Name]Page(
    viewModel: [Name]ViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is [Name]Event.NavigateBack -> { /* handle */ }
            }
        }
    }

    // UI content
}

@Preview(showBackground = true)
@Composable
private fun [Name]PagePreview() {
    VirtualClubsTheme {
        // preview state
    }
}
```

**Composable rules:**
- PascalCase name
- `@Preview` on the screen composable
- `modifier: Modifier = Modifier` on reusable child composables
- Do not call ViewModels from child composables — pass lambdas down
- Use `collectAsStateWithLifecycle()`, not `collectAsState()`

---

## Step 5: Add the route

In `Screen.kt`:
```kotlin
data object [Name] : Screen("[name-kebab]")
```

In `NavGraph.kt`:
```kotlin
composable(Screen.[Name].route) {
    [Name]Page()
}
```

If the screen receives parameters (e.g., an ID):
```kotlin
// Screen.kt
data object [Name] : Screen("[name]/{id}") {
    fun route(id: String) = "[name]/$id"
}

// NavGraph.kt
composable(
    route = Screen.[Name].route,
    arguments = listOf(navArgument("id") { type = NavType.StringType })
) { backStackEntry ->
    val id = backStackEntry.arguments?.getString("id") ?: return@composable
    [Name]Page(id = id)
}
```

---

## Step 6: Update CLAUDE.md

Add the new route to the **Navigation Routes** table in `CLAUDE.md`.

If the screen consumes a new API endpoint, add it to the **API Endpoints** table as well.

---

## Step 7: Offer to generate tests

After creating the screen, ask the user:
> "Do you want me to generate unit tests for `[Name]ViewModel`? (yes/no)"

If yes, follow the `/add-test` skill to generate the test file.
