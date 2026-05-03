# /explain — Explain a File or Concept

Explains any file, class, function, or technical concept in the project, aimed at a junior developer who is learning.

## Usage
- `/explain AuthViewModel.kt` — explains the auth ViewModel
- `/explain SafeResponse` — explains the response handling pattern
- `/explain Hilt` — explains the dependency injection system
- `/explain NavGraph.kt` — explains the navigation setup

---

## Step 1: Locate the resource

If it is a file, find it in the project and read it completely.
If it is a concept, use your knowledge of Android/Kotlin/Jetpack Compose.

## Step 2: Explain using this structure

### What is it?
A clear and simple definition in 2-3 lines.

### Why does it exist in this project?
The specific problem it solves within VirtualClubs Android.

### How does it work?
Step-by-step description of the flow, with references to specific lines of code when useful.

### How does it connect with the rest?
Which classes use it, what it depends on, which architectural pattern it implements (Clean Architecture, MVVM, etc.).

### Limitations or things to keep in mind
Edge cases, possible future improvements, related technical debt.

### Practical example
A concrete code snippet from the project illustrating the usage, with inline comments.

---

## Tone rules
- Explain as if the reader does not know the concept
- Use simple analogies when they help
- Relate to standard Android patterns when relevant
- Do not assume prior knowledge of Clean Architecture or MVVM
