# /add-api-endpoint — Consumir Nuevo Endpoint del Backend

Integra un nuevo endpoint de la API de extremo a extremo: desde `Endpoint.kt` hasta el use case, pasando por la capa de red, DTOs y repositorio.

## Uso
- `/add-api-endpoint GET /v1/clubs/list getClubs` — endpoint de listado de clubs
- `/add-api-endpoint POST /v1/clubs/join joinClub` — endpoint para unirse a un club
- `/add-api-endpoint GET /v1/user/profile getProfile` — endpoint de perfil

Formato: `/add-api-endpoint [METHOD] [/path] [functionName]`

---

## Paso 1: Leer contexto

Lee en paralelo:
- `data/remote/api/Endpoint.kt` — constantes de rutas existentes
- Una API interface existente como referencia (ej: `AuthApi.kt`)
- `data/remote/dto/` — DTOs existentes
- `data/managers/SafeCall.kt` y `SafeResponse.kt` — cómo se hacen las llamadas
- `domain/repository/` — repositorios existentes
- `CLAUDE.md` tabla API Endpoints

---

## Paso 2: Determinar qué crear

Propón la lista antes de crear nada:

```
Archivos a crear/modificar para [METHOD] [path]:

  data/remote/api/Endpoint.kt              añadir constante [NAME]
  data/remote/api/[Context]Api.kt          añadir suspend fun [functionName](...)
  [si POST/PUT] data/remote/dto/request/[Name]Request.kt   ← nuevo
  [si respuesta no trivial] data/remote/dto/response/[Name]Response.kt  ← nuevo
  domain/repository/[Context]Repository.kt añadir método
  data/repository/[Context]RepositoryImpl.kt implementar con safeCall
  domain/usecase/[FunctionName]UseCase.kt  ← nuevo
  di/UseCaseModule.kt                      añadir @Provides
  CLAUDE.md                                actualizar tabla API Endpoints

¿Continuar con este plan? (sí/ajustar/cancelar)
```

---

## Paso 3: Implementar en orden (bottom-up)

### 3.1 — DTO/s

**Request** (si aplica):
```kotlin
data class [Name]Request(
    val [campo]: [Tipo]
)
```

**Response** (si aplica, usar `ApiResponse<T>` del backend):
```kotlin
data class [Name]Response(
    val [campo]: [Tipo]
)
```

### 3.2 — Constante en `Endpoint.kt`

```kotlin
const val [NAME_UPPER] = "/v1/[path]"
```

### 3.3 — API interface

```kotlin
@[GET|POST|DELETE|PUT]([Endpoint].[NAME_UPPER])
suspend fun [functionName](
    @Header("Authorization") token: String? = null,
    @Body request: [Name]Request? = null  // si aplica
): ApiResponse<[Name]Response>
```

### 3.4 — Repository interface (en `domain/`)

```kotlin
suspend fun [functionName]([params]: [Tipos]): [ReturnType]
```

### 3.5 — Repository implementation (en `data/`)

```kotlin
override suspend fun [functionName]([params]): [ReturnType] {
    return safeCall { api.[functionName]([params]) }
}
```

### 3.6 — UseCase

```kotlin
@Singleton
class [FunctionName]UseCase @Inject constructor(
    private val repository: [Context]Repository
) {
    suspend operator fun invoke([params]): [ReturnType] {
        return repository.[functionName]([params])
    }
}
```

### 3.7 — DI

```kotlin
@Provides
@Singleton
fun provide[FunctionName]UseCase(repository: [Context]Repository): [FunctionName]UseCase {
    return [FunctionName]UseCase(repository)
}
```

---

## Paso 4: Verificar compilación

```bash
./gradlew compileDevDebugKotlin -q
```

---

## Paso 5: Actualizar CLAUDE.md

Añade el endpoint a la tabla **API Endpoints**:

```
| [METHOD] | [path] | [descripción] | [ContextApi] |
```

---

## Paso 6: Confirmar

```
✅ Endpoint [METHOD] [path] integrado:
  [lista de archivos creados/modificados]

Próximo paso: inyectar [FunctionName]UseCase en el ViewModel que lo necesite.
```
