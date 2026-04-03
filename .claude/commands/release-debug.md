# /release-debug — Preparar Release para Debug/Testing

Agrupa tareas con estado "📦 Pendiente debug", genera entrada de CHANGELOG, actualiza la versión del app y crea un PR de release.

## Uso
- `/release-debug` — release patch (0.1.1 → 0.1.2)
- `/release-debug --minor` — release minor (0.1.1 → 0.2.0)

---

## Paso 1: Verificar estado

Ejecuta `git status`. Si hay cambios sin commitear, detente:
> "⛔ Tienes cambios sin commitear. Haz `/commit` antes de continuar."

Verifica que estás en la rama `development`.

---

## Paso 2: Obtener tareas "Pendiente debug" de Notion

Busca en Notion las tareas con estado `📦 Pendiente debug`:
- `data_source_url`: `"collection://276a7f5d-0a0f-802c-8d6f-000b821853c1"`
- Filtra por `Estado` = `📦 Pendiente debug`

Si no hay tareas en ese estado, detente:
> "No hay tareas en estado '📦 Pendiente debug' para incluir en este release."

Lista las tareas encontradas y muéstralas al usuario para confirmación.

---

## Paso 3: Determinar nueva versión

Lee la versión actual en `app/build.gradle.kts`:
```kotlin
versionName = "X.Y.Zv Alpha"
versionCode = N
```

Calcula la nueva versión:
- Sin flags → incrementa patch (Z+1)
- `--minor` → incrementa minor (Y+1), reset patch a 0

Muestra al usuario:
```
Versión actual: X.Y.Zv Alpha (versionCode: N)
Nueva versión:  X.Y.Z+1v Alpha (versionCode: N+1)

Tareas incluidas en este release:
  - VC-[N] [título]
  - VC-[N] [título]
  ...

¿Continuar? (sí/no)
```

---

## Paso 4: Crear rama de release

```bash
git fetch origin development
git checkout development
git pull origin development
git checkout -b release/v[nueva-version]
git push --set-upstream origin release/v[nueva-version]
```

---

## Paso 5: Actualizar versión en build.gradle.kts

Edita `app/build.gradle.kts`:
- Actualiza `versionName`
- Incrementa `versionCode` en 1

---

## Paso 6: Generar o actualizar CHANGELOG

Crea o actualiza `CHANGELOG.md` en la raíz del proyecto añadiendo una sección al inicio:

```markdown
## [X.Y.Z] — [fecha actual]

### Implementado
- VC-[N]: [título de la tarea] ([tipo])
- VC-[N]: [título de la tarea] ([tipo])

### Corregido
- VC-[N]: [título de la tarea si es bug]
```

---

## Paso 7: Commit y PR

```bash
git add app/build.gradle.kts CHANGELOG.md
git commit -m "chore(release): bump version to [nueva-version]

Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>"
git push origin release/v[nueva-version]
```

Crea PR hacia `development` (o `main` si se especifica) con:
```bash
gh pr create --title "Release v[nueva-version]" --body "[descripción con lista de tareas]" --base development
```

---

## Paso 8: Actualizar estado en Notion

Para cada tarea incluida en el release, actualiza el estado a `⌛🔎 Pendiente de testeo`:
- Usa `notion-update-page` con `command: "update_properties"`
- `properties`: `{"Estado": "⌛🔎 Pendiente de testeo"}`

---

## Paso 9: Confirmar resultado

```
✅ Release v[nueva-version] preparado:
  Rama:    release/v[nueva-version]
  PR:      #[numero] — [URL]
  Tareas:  [N] tareas → ⌛🔎 Pendiente de testeo
  CHANGELOG actualizado
```
