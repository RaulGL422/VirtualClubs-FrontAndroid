# /add-test — Generate Tests for a Class or Composable

Generates unit or UI tests for any class, ViewModel, UseCase, or Composable in the project.

## Usage
- `/add-test AuthViewModel` — generates unit tests for the auth ViewModel
- `/add-test AuthUseCase` — generates tests for the use case
- `/add-test AuthPage` — generates UI tests with Compose Testing
- `/add-test SafeResponse` — generates tests for the response wrapper

---

## Step 1: Locate and read the target file

Find the file in the project. Read its full content to understand:
- What the class does
- What dependencies it has (to mock)
- What use cases it covers (happy path + error cases)
- What `ErrorType` it can throw

---

## Step 2: Determine the test type

**Unit Tests** (in `app/src/test/`) for:
- ViewModels — test state logic with `StateFlow`
- UseCases — test business logic
- Repositories — test data transformations
- Utilities — test pure logic

**UI Tests with Compose** (in `app/src/androidTest/`) for:
- Composables — test rendering and events
- Navigation flows — test that screens connect correctly

---

## Step 3: Unit test structure for a ViewModel

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class [Name]ViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mock[Dependency] = mockk<[Type]>()
    private lateinit var viewModel: [Name]ViewModel

    @Before
    fun setUp() {
        viewModel = [Name]ViewModel(mock[Dependency])
    }

    @Test
    fun `[description in English, human-readable]`() = runTest {
        // Arrange
        coEvery { mock[Dependency].invoke(any()) } returns Result.success([value])

        // Act
        viewModel.[action]([parameters])
        advanceUntilIdle()

        // Assert
        assertEquals([expected], viewModel.[state].value)
    }

    @Test
    fun `[description of the error case]`() = runTest {
        // Arrange
        coEvery { mock[Dependency].invoke(any()) } throws VirtualClubException(ErrorType.INVALID_CREDENTIALS)

        // Act
        viewModel.[action]([parameters])
        advanceUntilIdle()

        // Assert
        // Verify the error state
    }
}
```

**Rules:**
- Use `mockk` for mocks (not Mockito — this is Android/Kotlin)
- Use `coEvery`/`coVerify` for suspend functions
- Use `runTest` for coroutines in tests
- Include `MainDispatcherRule` to replace `Dispatchers.Main`
- Name tests in English, human-readable: `` `when credentials are invalid, returns error` ``
- Cover: happy path + at least 2 relevant error cases

---

## Step 4: UseCase test structure

```kotlin
class [Name]UseCaseTest {

    private val mock[Repo] = mockk<[Repo]Interface>()
    private lateinit var useCase: [Name]UseCase

    @Before
    fun setUp() {
        useCase = [Name]UseCase(mock[Repo])
    }

    @Test
    fun `[description]`() = runTest {
        // Arrange - Act - Assert (AAA)
    }
}
```

---

## Step 5: Composable UI test structure

```kotlin
@RunWith(AndroidJUnit4::class)
class [Name]PageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `[description of the visual case]`() {
        composeTestRule.setContent {
            VirtualClubsTheme {
                [Name]Page(
                    // composable parameters
                )
            }
        }

        // Assert an element exists
        composeTestRule.onNodeWithText("[visible text]").assertIsDisplayed()

        // Simulate a click
        composeTestRule.onNodeWithText("[button]").performClick()

        // Assert the result
        composeTestRule.onNodeWithText("[expected result]").assertIsDisplayed()
    }
}
```

---

## Step 6: Write and place the tests

- Unit tests → `app/src/test/java/es/virtualclubs/[same package as the class]/`
- UI tests → `app/src/androidTest/java/es/virtualclubs/[same package]/`

Write the test file, then ask the user if they want to run it:
```bash
./gradlew testDevDebugUnitTest --tests "[ClassName]Test"
```
