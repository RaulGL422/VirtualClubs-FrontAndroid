# /release-debug — Publicar Release a Debug

Agrupa los work items con estado `Pendiente debug` en Plane, genera la entrada de CHANGELOG, propone el bump de versión y crea la rama de release.

## Uso
- `/release-debug` — modo interactivo, Claude propone el bump
- `/release-debug --patch` — fuerza bump de patch (0.0.X)
- `/release-debug --minor` — fuerza bump de minor (0.X.0)

---

## Paso 1: Verificar estado del repositorio

Ejecuta `git branch --show-current` y `git status` en paralelo.

Si hay cambios sin commitear, **detente**:
> "Hay cambios sin commitear. Haz commit primero con `/commit`."

---

## Paso 2: Obtener features pendientes de Plane

Usa `mcp__plane__list_work_items`:
- `project_id`: `ee9b3862-a518-4a58-a39a-40ec993204e3`
- `params`: `{"state": "a4a1e67d-1f00-456c-abca-dfb498d0c0dd"}` (Pendiente debug)

Extrae por cada item: `id`, `sequence_id` (VC-N), `name`, `priority`.

### Si no hay items

> "No encontré items en estado 'Pendiente debug'. ¿Continuar igualmente con los commits recientes? (sí/no)"

---

## Paso 3: Revisar commits recientes

```bash
git tag --sort=-version:refname | head -1
# Si existe tag: git log vX.Y.Z..HEAD --oneline
# Si no: git log --oneline -30
```

---

## Paso 4: Leer versión actual

Lee `app/build.gradle.kts` y extrae:
- `versionName` (semver, ej: `"0.2.0"`)
- `versionCode` (entero, ej: `4`)

Lee `CHANGELOG.md` y extrae el último `## [X.Y.Z]`. Usa la mayor de las dos como versión base.

---

## Paso 5: Proponer bump de versión

Si se pasó `--patch` o `--minor`, úsalo directamente. Si no:

| Señal en commits | Bump |
|------------------|------|
| Al menos un `feat(*)` | **MINOR** — versionName: X.(Y+1).0, versionCode: +1 |
| Solo `fix(*)`/`chore(*)`/`refactor(*)`/`test(*)` | **PATCH** — versionName: X.Y.(Z+1), versionCode: +1 |

```
Versión actual:     [versionName] (code [versionCode])
Versión propuesta:  [nueva] (code [versionCode+1])  ← PATCH/MINOR
  Razón: [explicación]

¿Confirmar? (sí / --patch / --minor / cancelar)
```

---

## Paso 6: Generar entrada de CHANGELOG

```markdown
## [X.Y.Z] — AAAA-MM-DD

### Added
- [nuevas pantallas, features, endpoints consumidos] — VC-N

### Changed
- [cambios de comportamiento] — VC-N

### Fixed
- [bugs corregidos] — VC-N

### Internal
- [tests, refactoring, docs sin impacto de usuario] — VC-N
```

Omite secciones vacías. Incluye `— VC-N` al final de cada ítem con tarjeta.

---

## Paso 7: Mostrar resumen y pedir confirmación

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Release Debug  v[X.Y.Z] (code [N])
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Tarjetas incluidas ([N]):
  VC-N — [Nombre]

[entrada de CHANGELOG generada]

Archivos a modificar:
  CHANGELOG.md          nueva entrada v[X.Y.Z]
  app/build.gradle.kts  versionName → [X.Y.Z] | versionCode → [N+1]
  CLAUDE.md             Version: [X.Y.Z]

Rama:  chore/release-v[X.Y.Z]  (desde development)
Tag:   v[X.Y.Z]

¿Proceder? (sí/cancelar)
```

---

## Paso 8: Aplicar cambios en archivos

- **`CHANGELOG.md`:** insertar nueva entrada después de `## [Sin publicar]`
- **`app/build.gradle.kts`:** actualizar `versionName` y `versionCode`
- **`CLAUDE.md`:** actualizar `**Version:**`

---

## Paso 9: Crear rama, commit, push y PR

```bash
git fetch origin development && git checkout development && git pull origin development
git checkout -b chore/release-v[X.Y.Z]
git add CHANGELOG.md app/build.gradle.kts CLAUDE.md
```

```
chore(release): bump version to v[X.Y.Z]

Includes: [VC-N list]
```

```bash
git push --set-upstream origin chore/release-v[X.Y.Z]
git tag v[X.Y.Z]
gh pr create \
  --title "chore(release): version v[X.Y.Z]" \
  --body "[CHANGELOG section + included cards list]" \
  --base development
```

---

## Paso 10: Actualizar Plane

Para cada item con estado `Pendiente debug`, actualiza en paralelo:
`mcp__plane__update_work_item` (`project_id: "ee9b3862-a518-4a58-a39a-40ec993204e3"`, `work_item_id: [id]`, `state_id: "534c0f34-6aa6-44b3-935a-ae1b158f1946"`) → **Pendiente de testeo**

---

## Paso 11: Confirmar resultado

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  ✅ Release v[X.Y.Z] preparado
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

  PR:     [URL]
  Plane:  [N] items → Pendiente de testeo
  Tag:    v[X.Y.Z] creado localmente

Próximos pasos:
  1. Fusiona el PR a development
  2. Construye el APK de pruebas: ./gradlew assembleDevDebug
  3. Distribuye a testers (Firebase App Distribution / manual)
  4. Ejecuta pruebas manuales en dispositivo
  5. Si todo OK → items pasan a Pendiente de publicar (manual)
  6. Para publicar: ./gradlew assembleProdRelease + subir a Play Store
```
