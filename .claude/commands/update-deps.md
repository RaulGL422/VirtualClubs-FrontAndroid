# /update-deps — Revisar y Actualizar Dependencias

Revisa `gradle/libs.versions.toml` en busca de dependencias desactualizadas y propone actualizaciones seguras.

## Paso 1: Leer las dependencias actuales

Lee `gradle/libs.versions.toml` completo y extrae todas las versiones declaradas en `[versions]`.

Lee también `app/build.gradle.kts` para entender qué dependencias están activas.

## Paso 2: Analizar versiones

Compara las versiones actuales contra el conocimiento de las últimas versiones estables de cada dependencia. Las claves a revisar:

| Dependencia | Versión actual | Notas |
|-------------|---------------|-------|
| AGP (`agp`) | 9.1.0 | |
| Kotlin (`kotlin`) | 2.2.10 | |
| Compose BOM (`composeBom`) | 2025.07.00 | |
| Hilt (`hiltAndroid`) | 2.57 | |
| Navigation Compose | 2.9.3 | |
| DataStore | 1.1.7 | |
| Security Crypto | 1.1.0 | |
| Retrofit | 3.0.0 | |
| OkHttp Logging | 5.1.0 | |
| Lifecycle KTX | 2.9.2 | |
| Credentials | 1.5.0 | |
| Play Services Auth | 21.4.0 | |
| Core KTX | 1.16.0 | |

## Paso 3: Clasificar las actualizaciones

Para cada dependencia con actualización disponible, clasifica:

**SEGURA (patch / minor sin breaking changes conocidos):**
- Cambio de versión patch (x.y.Z → x.y.Z+1)
- Minor en librerías con buen historial de compatibilidad

**REQUIERE REVISIÓN (puede tener breaking changes):**
- Compose BOM — cambios de API en Material3 entre versiones
- Hilt — puede requerir ajustes en módulos o anotaciones
- Navigation Compose — cambios de API entre versiones menores
- Retrofit — cambios en Converters o interceptores
- AGP — puede requerir ajustes en build.gradle.kts

**MAYOR / NO RECOMENDADA ahora:**
- Kotlin major versions
- AGP major versions
- Cambios que requieren migración de API

## Paso 4: Presentar reporte

```
## Actualizaciones de Dependencias — [fecha]

### Actualizaciones Seguras (aplico automáticamente si confirmas)
| Dependencia | Versión actual | Versión nueva | Tipo |
|-------------|---------------|---------------|------|
| ... | | | |

### Requieren Revisión (te explico qué cambia antes de actualizar)
| Dependencia | Versión actual | Versión nueva | Riesgo |
|-------------|---------------|---------------|--------|
| ... | | | |

### No Recomendadas Ahora
[dependencias con major bump o razones para no actualizar]

---
¿Quieres que aplique las actualizaciones seguras? (sí/no)
¿Quieres que te explique los cambios de alguna dependencia específica? (nombre)
```

## Paso 5: Aplicar actualizaciones confirmadas

Si el usuario confirma, edita `gradle/libs.versions.toml` actualizando solo las versiones aprobadas.

Luego ejecuta para verificar que compila:
```bash
./gradlew compileDevDebugKotlin -q
```

Si hay error de compilación después de actualizar, revierte el cambio específico y reporta qué dependencia causó el problema.

## Paso 6: Recordatorio

Al terminar, sugiere hacer `/commit` con tipo `chore(deps): actualiza dependencias`.
