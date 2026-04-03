# /project-status — Estado General del Proyecto

Genera un resumen del estado actual del proyecto Android.

## Paso 1: Estado de Git y GitHub

Ejecuta en paralelo:
```bash
git branch --show-current
git log --oneline -8
git status --short
git stash list
gh pr list --state open --json number,title,headRefName,createdAt
```

## Paso 2: Buscar deuda técnica en el código

Busca TODOs, FIXMEs y malas prácticas con Grep en el directorio `app/src/main/java`:
- `TODO`, `FIXME`, `HACK`
- `println`, `Log.d` (posibles datos sensibles en debug)
- `// TODO` comentarios

También verifica el estado actual de la `HomePage.kt` (pantalla placeholder pendiente de contenido real).

## Paso 3: Verificar compilación rápida

```bash
./gradlew compileDevDebugKotlin -q 2>&1 | tail -5
```

## Paso 4: Generar reporte

```
## Estado del Proyecto — Virtual Clubs Android — [fecha actual]

### Git
- Rama actual: [rama]
- Últimos commits: [lista]
- Cambios sin commitear: [N archivos] / Limpio
- Stashes guardados: [N] / Ninguno
- PRs abiertos: [lista con número y título] / Ninguno

### Compilación
- Estado: [OK / Errores encontrados]

### Deuda Técnica
- TODOs/FIXMEs encontrados: [lista con archivo:línea] / Ninguno
- Pantallas pendientes de implementación:
  - [ ] Home screen (contenido real de clubes)
  - [ ] [otras encontradas]
- Items de deuda del CLAUDE.md:
  - [ ] ProGuard/R8 no configurado
  - [ ] Certificate pinning ausente
  - [ ] Tests sin cobertura
  - [ ] [otros]

### Próxima acción sugerida
[Una sola acción concreta basada en el estado actual]
```
