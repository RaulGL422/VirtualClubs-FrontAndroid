# /sync-main — Sincronizar con Main

Sincroniza la rama actual con `main` via rebase. Útil para mantener la rama de feature al día.

## Paso 1: Verificar estado

Ejecuta `git status`. Si hay cambios sin commitear, avisa:
> "Tienes cambios sin commitear. Haz `/commit` o stash antes de sincronizar."

Ejecuta `git branch --show-current` para mostrar la rama actual.

## Paso 2: Hacer fetch y rebase

```bash
git fetch origin main
git rebase origin/main
```

Si hay conflictos, muéstralos al usuario:
> "Hay conflictos en los siguientes archivos:
> [lista de archivos]
>
> Resuélvelos manualmente y luego ejecuta `git rebase --continue`. O usa `git rebase --abort` para cancelar."

Espera al usuario si hay conflictos.

## Paso 3: Push tras rebase

Si el rebase fue exitoso, avisa al usuario:
> "El rebase se completó. Como reescribiste el historial, el push requiere `--force-with-lease`:
> ```
> git push --force-with-lease origin [rama-actual]
> ```
> ¿Ejecuto el push? (sí/no)"

Solo ejecuta el push con force-with-lease si el usuario confirma explícitamente.

## Paso 4: Confirmar

Muestra el estado final con `git log --oneline -5`.
