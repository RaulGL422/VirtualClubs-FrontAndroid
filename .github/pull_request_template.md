## What does this PR do?

<!--
Brief description: what problem does it solve or what feature does it add?
2-3 bullet points maximum.
-->

- 
- 

---

## Type of change

- [ ] Bug fix
- [ ] New feature / new screen
- [ ] Refactor (no behavior change)
- [ ] Tests
- [ ] Documentation
- [ ] Chore / config / dependencies

---

## Screenshots / recordings

<!--
For UI changes: before/after screenshots or a short screen recording.
Delete this section if not applicable.
-->

| Before | After |
|--------|-------|
| — | — |

---

## How to test

<!--
Steps to verify the change on a device or emulator.
Be specific: which flavor, which screen, what input, expected result.
-->

1. Select the `devDebug` build variant
2. 
3. 

---

## Checklist

- [ ] `./gradlew compileDevDebugKotlin` passes with no errors
- [ ] `./gradlew testDevDebugUnitTest` passes
- [ ] New ViewModels or use cases have unit tests
- [ ] New screens added to both `Screen.kt` and `NavGraph.kt`
- [ ] New error types added to `ErrorHandler.kt`
- [ ] New use cases registered as `@Singleton` in `UseCaseModule`
- [ ] No secrets, API keys, or tokens hardcoded or logged
- [ ] `CLAUDE.md` updated if new routes, endpoints, or patterns were introduced
- [ ] PR targets `development`, not `main`
- [ ] PR description is complete (no empty sections above)
