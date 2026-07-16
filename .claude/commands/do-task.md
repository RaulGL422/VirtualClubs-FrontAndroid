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

Con ese nombre ya calculado, ejecuta el mismo procedimiento de `/new-feature` Pasos 3-4 (fetch/checkout/pull de `development`, `checkout -b`, push con `--set-upstream`) — no reimplementes los comandos aquí, ese comando es la fuente única de verdad para la creación de ramas.

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

Ejecuta el flujo de `/commit` completo (incluyendo su guard de rama protegida del Step 1 y la confirmación del Step 5) para generar y confirmar el mensaje. Usa como cuerpo adicional:

```
Ref: VC-[N]
```

No reimplementes aquí los pasos de `git add`/`commit`/`push` — son responsabilidad de `/commit`.

---

## Paso 8: Crear el Pull Request

Ejecuta el flujo de `/create-pr` completo (incluyendo su verificación de `gh auth status` del Step 3), con:
- **Título** (máx 70 chars) en inglés
- **Descripción** con las secciones estándar de `/create-pr` — **What does this PR do?** (basado en los checkboxes completados), **Main changes**, **How to test**, **Checklist** (ítems marcados como `☑`)

No reimplementes aquí `gh pr create` — es responsabilidad de `/create-pr`, que además lanza automáticamente `/review-pr` al terminar (ver Paso 9).

---

## Paso 9: Revisión automática del PR

`/create-pr` ya invoca `/review-pr` automáticamente al crear el PR (su Step 6) — usa ese resultado en vez de re-derivar aquí una lista de criterios propia. Si por algún motivo no se lanzó, ejecútalo explícitamente: `/review-pr [numero]`.

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
