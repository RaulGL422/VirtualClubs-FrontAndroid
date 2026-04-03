# /add-test — Generar Tests para una Clase o Composable

Genera tests unitarios o de UI para cualquier clase, ViewModel, UseCase o Composable del proyecto.

## Uso
- `/add-test AuthViewModel` — genera tests unitarios para el ViewModel de auth
- `/add-test AuthUseCase` — genera tests para el caso de uso
- `/add-test AuthPage` — genera tests de UI con Compose Testing
- `/add-test SafeResponse` — genera tests para el wrapper de respuestas

---

## Paso 1: Localizar y leer el archivo objetivo

Busca el archivo en el proyecto. Lee su contenido completo para entender:
- Qué hace la clase
- Qué dependencias tiene (para mockear)
- Qué casos de uso tiene (happy path + casos de error)
- Qué ErrorType puede lanzar

---

## Paso 2: Determinar el tipo de test

**Tests Unitarios** (en `app/src/test/`) para:
- ViewModels — testear lógica de estado con `StateFlow`
- UseCases — testear lógica de negocio
- Repositorios — testear transformaciones de datos
- Utilidades — testear lógica pura

**Tests de UI con Compose** (en `app/src/androidTest/`) para:
- Composables — testear renderizado y eventos
- Flujos de navegación — testear que las pantallas se conectan correctamente

---

## Paso 3: Estructura de un test unitario de ViewModel

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class [Nombre]ViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule() // Reemplaza Dispatchers.Main en tests

    private val mock[Dependencia] = mockk<[Tipo]>()
    private lateinit var viewModel: [Nombre]ViewModel

    @Before
    fun setUp() {
        viewModel = [Nombre]ViewModel(mock[Dependencia])
    }

    @Test
    fun `[descripción del caso en español]`() = runTest {
        // Arrange
        coEvery { mock[Dependencia].invoke(any()) } returns Result.success([valor])

        // Act
        viewModel.[accion]([parametros])
        advanceUntilIdle()

        // Assert
        assertEquals([esperado], viewModel.[estado].value)
    }

    @Test
    fun `[descripción del caso de error]`() = runTest {
        // Arrange
        coEvery { mock[Dependencia].invoke(any()) } throws VirtualClubException(ErrorType.INVALID_CREDENTIALS)

        // Act
        viewModel.[accion]([parametros])
        advanceUntilIdle()

        // Assert
        // Verifica el estado de error
    }
}
```

**Reglas:**
- Usa `mockk` para los mocks (no Mockito — es Android/Kotlin)
- Usa `coEvery`/`coVerify` para funciones suspend
- Usa `runTest` para coroutines en tests
- Incluye `MainDispatcherRule` para reemplazar `Dispatchers.Main`
- Nombra los tests en español descriptivo: `` `cuando hay credenciales inválidas, devuelve error` ``
- Cubre: happy path + al menos 2 casos de error relevantes

---

## Paso 4: Estructura de un test de UseCase

```kotlin
class [Nombre]UseCaseTest {

    private val mock[Repo] = mockk<[Repo]Interface>()
    private lateinit var useCase: [Nombre]UseCase

    @Before
    fun setUp() {
        useCase = [Nombre]UseCase(mock[Repo])
    }

    @Test
    fun `[descripción]`() = runTest {
        // Arrange - Act - Assert (AAA)
    }
}
```

---

## Paso 5: Estructura de un test de Composable (UI)

```kotlin
@RunWith(AndroidJUnit4::class)
class [Nombre]PageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `[descripción del caso visual]`() {
        composeTestRule.setContent {
            VirtualClubsTheme {
                [Nombre]Page(
                    // parámetros del composable
                )
            }
        }

        // Verificar que un elemento existe
        composeTestRule.onNodeWithText("[texto visible]").assertIsDisplayed()

        // Simular click
        composeTestRule.onNodeWithText("[botón]").performClick()

        // Verificar resultado
        composeTestRule.onNodeWithText("[resultado esperado]").assertIsDisplayed()
    }
}
```

---

## Paso 6: Escribir y colocar los tests

- Tests unitarios → `app/src/test/java/es/virtualclubs/[mismo package que la clase]/`
- Tests de UI → `app/src/androidTest/java/es/virtualclubs/[mismo package]/`

Escribe el archivo de test y luego confirma con el usuario si quiere ejecutarlos:
```bash
./gradlew testDevDebugUnitTest --tests "[NombreClase]Test"
```
