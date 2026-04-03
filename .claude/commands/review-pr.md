# /review-pr — Revisión de Pull Request

Revisa el PR actual (o el especificado como argumento) de forma exhaustiva, enfocado en Android/Jetpack Compose.

## Uso
- `/review-pr` — revisa el PR de la rama actual
- `/review-pr 42` — revisa el PR número 42

## Paso 1: Identificar el PR

Si se pasó un número como argumento, usa ese PR.

Si no, ejecuta:
```
git branch --show-current
gh pr list --head [rama-actual] --json number,title,url,baseRefName
```

Si no existe PR para la rama actual, avisa al usuario y detente.

**Detectar tarjeta Notion:** Extrae el número N si el nombre de la rama contiene `vc-[N]`. Se usará al final para actualizar el estado.

## Paso 2: Obtener información del PR

Ejecuta:
```
gh pr view [numero] --json title,body,baseRefName,headRefName,additions,deletions,changedFiles,commits
gh pr diff [numero]
```

## Paso 3: Revisar el código (análisis exhaustivo)

Analiza el diff completo evaluando **todas** las categorías siguientes:

---

### 🔴 ERRORES CRÍTICOS (bloquean el merge)

- Crash potencial: NullPointerException en Composables, StateFlow no inicializado antes de colectar
- Lógica de negocio incorrecta o rota
- Navegación rota (rutas mal definidas, argumentos sin decodificar)
- `TODO`, `FIXME`, `HACK`, `Log.d`, `println` olvidados en el diff
- Descripción del PR vacía o sin secciones requeridas
- Compilación rota (verificar que no hay errores de sintaxis evidentes)
- Coroutines no canceladas o lanzadas en scope incorrecto

---

### 🔐 SEGURIDAD ANDROID

- [ ] **Almacenamiento de tokens:** ¿Se usan `SecureUserPreferences` (AES/GCM) y NO `SharedPreferences` plano para tokens?
- [ ] **Datos en logs:** ¿Hay tokens, contraseñas o datos de usuario en `Log.d/e/i`?
- [ ] **Deep links:** ¿Los deep links validan los parámetros antes de usarlos?
- [ ] **Permisos en Manifest:** ¿Solo se añaden los permisos estrictamente necesarios?
- [ ] **Credenciales hardcodeadas:** ¿Hay secrets, API keys o contraseñas en el código?
- [ ] **BuildConfig correctamente usado:** ¿Las URLs y keys usan `BuildConfig.BASE_URL` y `BuildConfig.GOOGLE_CLIENT_ID`?
- [ ] **Datos sensibles en Bundle/arguments de navegación:** ¿Se evita pasar tokens por argumentos de navegación?

---

### 📚 DOCUMENTACIÓN

- [ ] ¿Los composables reutilizables nuevos tienen `@Preview`?
- [ ] ¿Las funciones públicas o complejas tienen KDoc?
- [ ] ¿Se actualizó `CLAUDE.md` si se añadieron rutas nuevas, endpoints nuevos o patrones nuevos?
- [ ] ¿Los ErrorType nuevos tienen su caso en `ErrorHandler.kt`?

---

### 🏗️ ARQUITECTURA Y MALAS PRÁCTICAS

- ¿Se respeta la separación de capas? (Composable → ViewModel → UseCase → Repository, no saltar capas)
- ¿El ViewModel **no** tiene referencias a Context, Activity ni Composables?
- ¿El estado del ViewModel se expone como `StateFlow` inmutable (no `MutableStateFlow` público)?
- ¿Los efectos secundarios (navegación, toast) se manejan con `SharedFlow`/`Channel`, no desde el Composable directamente?
- ¿Se usa `hiltViewModel()` correctamente en los composables?
- ¿Los use cases tienen `operator fun invoke(...)` y contienen la lógica de negocio?
- ¿Los repositorios solo hacen mapeo de datos, no lógica de negocio?
- ¿Hay código duplicado que podría extraerse a componentes o utilities?
- ¿Los nombres de variables, funciones y clases son descriptivos?
- ¿Imports sin usar o código comentado innecesario?
- ¿Los composables nuevos reutilizables están en `presentation/components/`, no inlineados?

---

### ⚡ RENDIMIENTO Y COMPOSE

- ¿Hay recomposiciones innecesarias? (lambdas no recordadas, objetos nuevos en cada recomposición)
- ¿Se usa `remember { }` correctamente para valores que no deben recalcularse?
- ¿Los `LazyColumn`/`LazyRow` usan `key` correctamente?
- ¿Las operaciones de IO o CPU pesadas están en `Dispatchers.IO` o `Dispatchers.Default`?
- ¿Los Flows se colectan con `collectAsStateWithLifecycle()` (no `collectAsState()`) para respetar el ciclo de vida?
- ¿Se usa `rememberCoroutineScope` solo cuando es necesario (no para reemplazar `viewModelScope`)?

---

### ✅ TESTS

- ¿El nuevo ViewModel o UseCase tiene tests unitarios en `src/test/`?
- ¿Se cubren los casos de error además del happy path?
- ¿Los tests nuevos de UI usan el framework de Compose Testing?
- **Nota:** No es posible ejecutar los tests con el diff. Si hay tests nuevos, deben ejecutarse con `./gradlew testDevDebugUnitTest` antes del merge.

---

## Paso 4: Generar reporte

Presenta el resultado con este formato:

```
## Revisión PR #[numero]: [título]

**Base:** [rama-base] ← [rama-head]
**Cambios:** +[adiciones] / -[eliminaciones] en [N] archivos

---

### 🔴 Errores Críticos
[lista o "Ninguno encontrado"]

### 🔐 Problemas de Seguridad
[lista o "Ninguno encontrado"]

### 📚 Documentación Faltante
[lista o "Completa"]

### 🏗️ Malas Prácticas
[lista o "Ninguna encontrada"]

### ⚡ Problemas de Rendimiento / Compose
[lista o "Ninguno encontrado"]

### ✅ Estado de Tests
[observaciones]

---

### Veredicto
[APROBADO / APROBADO CON SUGERENCIAS / CAMBIOS REQUERIDOS]

### Próximos pasos sugeridos:
1. [acción concreta]
2. [acción concreta]
```

Si hay errores críticos o problemas de seguridad, explica el riesgo y propón el fix concreto con código.

---

## Paso 5: Actualizar estado en Notion (si aplica)

Si se detectó una tarjeta Notion (VC-N en el nombre de rama), actualiza el estado según el veredicto:

| Veredicto | Estado Notion |
|-----------|---------------|
| APROBADO | `📦 Pendiente debug` |
| APROBADO CON SUGERENCIAS | `📦 Pendiente debug` |
| CAMBIOS REQUERIDOS | `🔄️Cambios Solicitados` |

Busca la página con `notion-search` en `data_source_url: "collection://276a7f5d-0a0f-802c-8d6f-000b821853c1"` usando el número N, luego usa `notion-update-page` con `command: "update_properties"` y el estado correspondiente.

---

## Paso 6: Actualizar documentación si corresponde

Después de generar el reporte, lee `CLAUDE.md` y determina si el PR introduce cambios que lo dejan desactualizado. Evalúa:

**Rutas de Navegación:** ¿El PR añade, elimina o modifica rutas en `Screen.kt`/`NavGraph.kt`? Actualiza la tabla.

**Endpoints API Consumidos:** ¿El PR consume nuevos endpoints o modifica llamadas existentes? Actualiza la tabla.

**Deuda Técnica Conocida:** ¿El PR resuelve o introduce deuda técnica? Actualiza la tabla.

**Patrones y Convenciones:** ¿El PR introduce un patrón nuevo? Documéntalo.

Si no hay nada que actualizar en `CLAUDE.md`, indica: `"CLAUDE.md está al día, no requiere cambios."`

Si hay cambios, aplícalos directamente y al final muestra un resumen:
```
### Documentación actualizada
- [sección]: [qué cambió]
```
