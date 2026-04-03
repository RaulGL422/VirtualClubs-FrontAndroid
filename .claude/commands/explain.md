# /explain — Explicar Archivo o Concepto

Explica cualquier archivo, clase, función o concepto técnico del proyecto, orientado a un desarrollador junior que está aprendiendo.

## Uso
- `/explain AuthViewModel.kt` — explica el ViewModel de autenticación
- `/explain SafeResponse` — explica el patrón de manejo de respuestas
- `/explain Hilt` — explica el sistema de inyección de dependencias
- `/explain NavGraph.kt` — explica la navegación del proyecto

---

## Paso 1: Localizar el recurso

Si es un archivo, búscalo en el proyecto y léelo completo.
Si es un concepto, usa tu conocimiento de Android/Kotlin/Jetpack Compose.

## Paso 2: Explicar con esta estructura

### ¿Qué es?
Una definición clara y simple en 2-3 líneas.

### ¿Por qué existe en este proyecto?
El problema concreto que resuelve dentro de VirtualClubs Android.

### ¿Cómo funciona?
Descripción paso a paso del flujo, con referencias a líneas de código específicas cuando sea útil.

### ¿Cómo se conecta con el resto?
Qué clases lo usan, de qué depende, qué patrón de arquitectura implementa (Clean Architecture, MVVM, etc.).

### Limitaciones o cosas a tener en cuenta
Casos extremos, posibles mejoras futuras, deuda técnica relacionada.

### Ejemplo práctico
Un fragmento de código concreto del proyecto que ilustre el uso, con comentarios inline.

---

## Reglas de tono
- Explica como si el lector no conoce el concepto
- Usa analogías simples cuando ayuden
- Relaciona con patrones Android estándar cuando sea relevante
- No asumas conocimiento previo de Clean Architecture o MVVM
