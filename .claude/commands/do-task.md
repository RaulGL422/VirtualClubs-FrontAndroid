# /do-task — Resolver una Tarea Completa de Plane

Ejecuta el ciclo completo de desarrollo para una tarjeta de Plane: crea la rama, implementa todos los checkboxes, hace commit, crea el PR y lo revisa. Modo autónomo — solo pide confirmación una vez al inicio.

## Uso
- `/do-task VC-5` — resuelve la tarea VC-5 de Plane
- `/do-task 5` — equivalente a VC-5

---

## Paso 1: Validaciones previas

Ejecuta `git status`. Si hay cambios sin commitear, detente:
> "⛔ Tienes cambios sin commitear. Haz `/commit` o stash antes de continuar."

---

## Paso 2: Obtener la tarea de Plane

Usa `mcp__plane__retrieve_work_item_by_identifier`:
- `project_identifier`: `"VC"`
- `issue_identifier`: el número N

Guarda el `id` (UUID) para actualizaciones posteriores.

Extrae:
- `name`, `priority`, `point`, `description_html`, `id`
- Lista de checkboxes `☐` del `description_html`

Si la tarjeta no existe, detente y avisa al usuario.

---

## Paso 2B: Generar checklist si la tarea no tiene checkboxes

Si el `description_html` **no contiene ningún `☐`**, genera un checklist antes de continuar.

Analiza el `name` y descripción para inferir pasos concretos. El checklist debe cubrir según el tipo de tarea:

- **Nueva pantalla:** leer contexto de navigation, crear ViewModel + Page + ruta en Screen.kt + NavGraph.kt, añadir use cases necesarios, preview
- **Nuevo use case:** crear interfaz de repositorio si aplica, implementación, registro en DI
- **Nuevo endpoint API:** Endpoint.kt, API interface, DTO, Repository interface, implementación, UseCase
- **Fix de bug:** reproducir el bug, identificar causa raíz, fix, test que previene regresión
- **Siempre:** `./gradlew compileDevDebugKotlin`, `./gradlew testDevDebugUnitTest`, actualizar CLAUDE.md si hay rutas/endpoints nuevos

Actualiza la tarjeta: `mcp__plane__update_work_item` (`project_id: "ee9b3862-a518-4a58-a39a-40ec993204e3"`, `work_item_id: [id]`, `description_html: "[HTML con ☐ items]"`).

---

## Paso 3: Mostrar resumen y pedir confirmación ÚNICA

```
Tarea encontrada:
  VC-[N] — [name]
  Prioridad: [priority] | Esfuerzo: [point] pts

Pasos de implementación:
  ☐ [checkbox 1]
  ☐ [checkbox 2]
  ...

Flujo que se ejecutará:
  1. Crear rama [nombre-rama] desde development
  2. Implementar cada paso de la lista
  3. Compilar y pasar tests
  4. Commit semántico
  5. Crear PR hacia development
  6. Revisión automática del PR

¿Ejecutar todo el flujo? (sí/no)
```

---

## Paso 4: Crear la rama

**Prefijo según el nombre de la tarea:**

| Contexto | Prefijo |
|----------|---------|
| Bug / Error / Fix | `fix/` |
| Testing | `test/` |
| Feature / Pantalla / UseCase / API | `feature/` |
| Diseño / Chore / Config | `chore/` |

**Nombre de rama:** kebab-case ASCII, sin tildes. Formato: `[prefijo]/vc-[N]-[nombre-kebab]`. Máximo 55 caracteres.

```bash
git fetch origin development
git checkout development
git pull origin development
git checkout -b [nombre-rama]
git push --set-upstream origin [nombre-rama]
```

Actualiza el estado en Plane a **En progreso**:
`mcp__plane__update_work_item` (`project_id: "ee9b3862-a518-4a58-a39a-40ec993204e3"`, `work_item_id: [id]`, `state_id: "52e0ee6e-f973-4826-b511-661bcb8f0e29"`)

---

## Paso 5: Implementar los checkboxes

Lee el `description_html` actual y extrae los ítems `☐` pendientes.

Para cada `☐`:

1. **Analiza** qué cambio requiere el paso
2. **Implementa** el cambio en los archivos correspondientes
3. **Verifica** leyendo el archivo modificado
4. **Marca** el checkbox: actualiza `description_html` cambiando ese `☐` por `☑` y llama a `mcp__plane__update_work_item`

**Tipos de pasos:**
- Leer código existente → `Grep`, `Read`, `Glob`
- Crear/modificar Kotlin → `Write` o `Edit`
- Compilar → `./gradlew compileDevDebugKotlin -q`
- Tests → `./gradlew testDevDebugUnitTest`
- Verificar layer boundaries: no `@Inject` de repositorios en ViewModels, no ViewModels en Composables

Si un paso falla, detente y avisa con el error concreto.

---

## Paso 6: Verificación final

Si los tests no se ejecutaron como parte de los checkboxes:
```bash
./gradlew testDevDebugUnitTest
```

Si hay fallos, detente. No hacer commit con tests en rojo.

---

## Paso 7: Commit semántico

Tipo según los cambios: `feat` / `fix` / `refactor` / `chore` / `perf` / `test` / `docs`

```
[type]([scope]): concise description in English

Ref: VC-[N]

Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>
```

Muestra el mensaje y espera confirmación antes de hacer el commit.

```bash
git add [archivos modificados — listar antes de confirmar, nunca git add -A sin mostrar]
git commit -m "[mensaje]"
git push origin [rama-actual]
```

---

## Paso 8: Crear el Pull Request

Título (máx 70 chars) y descripción en inglés con secciones:
- **What does this PR do?** — 2-3 puntos basados en los checkboxes completados
- **Main changes** — archivos modificados
- **How to test** — pasos concretos para verificar el cambio
- **Checklist** — ítems marcados como `☑`

```bash
gh pr create --title "[title]" --body "[description]" --base development
```

---

## Paso 9: Revisión automática del PR

Analiza el diff evaluando:

### 🔴 Critical Errors
Excepciones no manejadas, NullPointerException potenciales, lógica rota, `TODO`/`FIXME` sin ticket, `Log.d`/`println` olvidados.

### 🔐 Security (OWASP Mobile Top 10)
Tokens o credenciales en logs, secretos hardcodeados (usar `BuildConfig`), tokens fuera de `SecureUserPreferences`, `runBlocking` fuera de `NetworkModule`.

### 🏗️ Architecture
Capa respetada (Composable → ViewModel → UseCase → Repository), no ViewModels en Composables (pasar lambdas), no repositorios inyectados directamente en ViewModels, interfaces no implementaciones, MutableStateFlow no expuesto, nuevos errores tienen case en `ErrorHandler.kt`, nuevas rutas en `Screen.kt` y `NavGraph.kt`.

### 🎨 UI
Design System: no hex hardcodeados fuera de `Color.kt`, no `fontSize`/`fontWeight` directos (usar `MaterialTheme.typography`), `@Preview` en composables nuevos, `modifier: Modifier = Modifier` en composables reutilizables.

### ✅ Tests
Nuevos use cases y ViewModels tienen tests, `MainDispatcherRule` en tests con corrutinas, Fakes para repositorios (no Mocks).

**Actualizar Plane según veredicto:**

| Veredicto | Estado | `state_id` |
|-----------|--------|------------|
| APPROVED | `Pendiente debug` | `a4a1e67d-1f00-456c-abca-dfb498d0c0dd` |
| APPROVED WITH SUGGESTIONS | `Pendiente debug` | `a4a1e67d-1f00-456c-abca-dfb498d0c0dd` |
| CHANGES REQUIRED | `Cambios solicitados` | `c9213f04-9c35-42d2-83fd-fb292da879a8` |

Llama a `mcp__plane__update_work_item` (`project_id: "ee9b3862-a518-4a58-a39a-40ec993204e3"`, `work_item_id: [id]`, `state_id: [según veredicto]`).

---

## Paso 10: Confirmar resultado final

```
✅ Tarea VC-[N] completada:
  Rama:   [nombre-rama]
  PR:     #[numero] — [URL]
  Plane:  [estado final]
  Tests:  [N] tests, 0 fallos
```
