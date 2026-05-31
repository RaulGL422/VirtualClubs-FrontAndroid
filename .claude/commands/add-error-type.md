# /add-error-type — Añadir Tipo de Error

Añade un nuevo `ErrorType` con su mensaje de usuario en `ErrorHandler` y su recurso de string. Tocar solo uno de los tres archivos sin los otros rompe la cadena de errores.

## Uso
- `/add-error-type CLUB_NOT_FOUND "El club solicitado no existe"`
- `/add-error-type SPORT_NOT_SUPPORTED "Este deporte no está disponible todavía"`

Formato: `/add-error-type [NOMBRE_MAYUSCULAS] "[mensaje visible al usuario]"`

---

## Paso 1: Leer el estado actual

Lee en paralelo:
- `domain/model/ErrorType.kt` — todos los tipos actuales con sus códigos
- `presentation/managers/ErrorHandler.kt` — mapeo ErrorType → R.string
- `app/src/main/res/values/strings.xml` — strings de error existentes

---

## Paso 2: Validar

Comprueba que el nombre no existe ya en `ErrorType.kt`. Si existe, para:
> "⛔ `[NOMBRE]` ya existe en ErrorType. ¿Quisiste decir otro?"

Determina el siguiente código disponible (1-13 reservados para el backend, Android usa 14+):
- Lee el código más alto en el enum
- El nuevo código será ese + 1

---

## Paso 3: Proponer los cambios

```
Cambios que se aplicarán:

1. domain/model/ErrorType.kt
   [NOMBRE]([código_siguiente])

2. app/src/main/res/values/strings.xml
   <string name="error_[nombre_snake]">[mensaje visible]</string>

3. presentation/managers/ErrorHandler.kt
   ErrorType.[NOMBRE] -> R.string.error_[nombre_snake]

¿Aplicar? (sí/cancelar)
```

---

## Paso 4: Aplicar los cambios

### 4.1 — `ErrorType.kt`

Añade el nuevo valor al enum manteniendo el orden numérico:
```kotlin
[NOMBRE]([código])
```

### 4.2 — `strings.xml`

Añade junto a los demás strings de error:
```xml
<string name="error_[nombre_en_snake_case]">[mensaje visible al usuario]</string>
```

### 4.3 — `ErrorHandler.kt`

Añade el caso en el `when` exhaustivo:
```kotlin
ErrorType.[NOMBRE] -> R.string.error_[nombre_en_snake_case]
```

---

## Paso 5: Verificar compilación

```bash
./gradlew compileDevDebugKotlin -q
```

Si el `when` en `ErrorHandler.kt` es exhaustivo, el compilador Kotlin confirmará que el nuevo caso está cubierto. Si no compila por `when` incompleto, significa que hay otro archivo que también necesita el caso — lee el error y añádelo.

---

## Paso 6: Confirmar

```
✅ ErrorType.[NOMBRE] añadido (código [N]):
  ErrorType.kt:    valor añadido
  strings.xml:     error_[nombre] → "[mensaje]"
  ErrorHandler.kt: case añadido

Uso: throw VirtualClubException(ErrorType.[NOMBRE])
     o: globalUIManager.showError(ErrorType.[NOMBRE])
```
