# /add-usecase — Crear Use Case

Crea un nuevo use case siguiendo Clean Architecture + Hilt, con todos los archivos necesarios en el orden correcto.

## Uso
- `/add-usecase GetClubsByUser` — recupera clubs del usuario autenticado
- `/add-usecase JoinClub` — permite a un usuario unirse a un club
- `/add-usecase UpdateUserProfile` — actualiza el perfil del usuario

---

## Paso 1: Analizar el use case

Extrae del nombre y contexto:
- **Nombre en PascalCase** con sufijo `UseCase` (ej: `GetClubsByUserUseCase`)
- **Tipo de operación:** lectura (solo query) vs escritura (mutación)
- **Repositorio que necesita:** ¿existe ya en `domain/repository/` o hay que crearlo?
- **Parámetros de entrada** y **tipo de retorno**
- **Lógica de negocio** si aplica (validaciones, transformaciones)

---

## Paso 2: Leer contexto

Lee en paralelo:
- `domain/repository/` — interfaces existentes
- `domain/usecase/` — use cases existentes como referencia de estilo
- `di/` — módulos Hilt para entender cómo se registran (buscar `UseCaseModule` o similar)
- `domain/model/` — entidades disponibles

---

## Paso 3: Proponer estructura

```
Archivos a crear/modificar:

✅ SIEMPRE:
  domain/usecase/[Nombre]UseCase.kt          use case principal
  di/[Module].kt                              añadir @Provides o @Binds

⚠️ SOLO SI se necesita un repositorio nuevo:
  domain/repository/[Nombre]Repository.kt    interfaz
  data/repository/[Nombre]RepositoryImpl.kt  implementación
  di/RepositoryModule.kt                      añadir binding

¿Continuar? (sí/cancelar)
```

---

## Paso 4: Crear el use case

```kotlin
@Singleton
class [Nombre]UseCase @Inject constructor(
    private val [repository]: [Repository]
) {
    suspend operator fun invoke([params]: [Tipos]): [ReturnType] {
        return [repository].[method]([params])
    }
}
```

**Reglas:**
- `@Singleton` siempre
- `operator fun invoke` como punto de entrada (no nombrar el método)
- Toda la lógica de negocio aquí, no en el ViewModel
- Nunca referenciar `Context`, `Activity`, o clases Android
- Devolver `Result<T>` si puede fallar, o el tipo directo si siempre tiene éxito

---

## Paso 5: Registrar en DI

Añade en el módulo correspondiente (o crea `di/UseCaseModule.kt` si no existe):

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provide[Nombre]UseCase(repository: [Repository]): [Nombre]UseCase {
        return [Nombre]UseCase(repository)
    }
}
```

Si el módulo ya existe, añade solo el nuevo `@Provides`.

---

## Paso 6: Crear repositorio si es necesario

**Interfaz** en `domain/repository/[Nombre]Repository.kt`:
```kotlin
interface [Nombre]Repository {
    suspend fun [method]([params]): [ReturnType]
}
```

**Implementación** en `data/repository/[Nombre]RepositoryImpl.kt`:
```kotlin
@Singleton
class [Nombre]RepositoryImpl @Inject constructor(
    private val api: [Api]
) : [Nombre]Repository {

    override suspend fun [method]([params]): [ReturnType] {
        return safeCall { api.[method]([params]) }
    }
}
```

Registrar en `di/RepositoryModule.kt`:
```kotlin
@Binds
@Singleton
abstract fun bind[Nombre]Repository(impl: [Nombre]RepositoryImpl): [Nombre]Repository
```

---

## Paso 7: Verificar compilación

```bash
./gradlew compileDevDebugKotlin -q
```

---

## Paso 8: Ofrecer test

> "¿Quieres que genere el test unitario para `[Nombre]UseCase`? Si el repositorio es nuevo, crearé también el Fake. (sí/no)"

Si sí:
- Crear `test/fakes/Fake[Nombre]Repository.kt` con estado configurable
- Crear `test/usecase/[Nombre]UseCaseTest.kt` con `MainDispatcherRule` y casos de éxito + fallo

---

## Paso 9: Confirmar

```
✅ Use case creado:
  [Nombre]UseCase.kt
  [Nombre]Repository.kt + Impl (si nuevo)
  DI: registrado en [Module]

Próximo paso: inyectar [Nombre]UseCase en el ViewModel que lo necesite.
```
