# /check-ds — Design System Compliance Check & Fix

Verifies that UI code uses Stadium DS tokens (`MaterialTheme.colorScheme`, `MaterialTheme.typography`, `MaterialTheme.shapes`) instead of raw or hardcoded values. Automatically fixes violations found.

## Usage
- `/check-ds` — checks only the files changed in the current branch vs `development`
- `/check-ds --all` — checks the entire `presentation/` layer

---

## Step 1: Load the design system

Read the four theme files to understand what tokens are available:

```
app/src/main/java/es/virtualclubs/presentation/theme/Color.kt
app/src/main/java/es/virtualclubs/presentation/theme/Type.kt
app/src/main/java/es/virtualclubs/presentation/theme/Shape.kt
app/src/main/java/es/virtualclubs/presentation/theme/Theme.kt
```

Build a mental map of available tokens:
- **Colors:** `MaterialTheme.colorScheme.primary`, `.secondary`, `.background`, `.surface`, `.onPrimary`, `.error`, etc.
- **Typography:** `MaterialTheme.typography.displayLarge`, `.headlineMedium`, `.bodyLarge`, `.labelSmall`, etc.
- **Shapes:** `MaterialTheme.shapes.small`, `.medium`, `.large`, `.extraLarge`
- **Raw palette tokens** (`VCBlue500`, `VCOrange400`, etc.) are defined in `Color.kt` and consumed in `Theme.kt` — they must NOT appear outside those two files.

---

## Step 2: Identify files to check

**If `--all` was passed:** find all `*.kt` files under `presentation/` except `theme/`.

**Otherwise (default):** run:
```bash
git diff development...HEAD --name-only
```
Filter the result to files under `presentation/` that are not in `theme/`.

If there are no files to check, inform the user and stop.

---

## Step 3: Scan for violations

Read each file and search for these patterns:

### 🎨 Color violations

| Pattern | Problem | Correct replacement |
|---------|---------|---------------------|
| `Color(0xFF...)` outside `Color.kt` / `Theme.kt` | Raw hex color | Use `MaterialTheme.colorScheme.*` |
| `VCBlue*`, `VCOrange*`, `VCNeutral*`, etc. outside `Color.kt` / `Theme.kt` | Direct palette token | Use `MaterialTheme.colorScheme.*` |
| `color = Color.Red`, `Color.White`, `Color.Black`, `Color.Transparent` (outside theme) | Android default color | Use semantic token or `Color.Transparent` only when unavoidable |
| `contentColor = Color(...)` | Raw color in content | Use `MaterialTheme.colorScheme.onSurface` or equivalent |

### 📝 Typography violations

| Pattern | Problem | Correct replacement |
|---------|---------|---------------------|
| `fontSize = X.sp` in a composable (not in `Type.kt`) | Hardcoded font size | Use `MaterialTheme.typography.*` style |
| `fontWeight = FontWeight.Bold/Medium/...` without a typography token | Hardcoded weight | Use `MaterialTheme.typography.*` style |
| `fontFamily = Poppins` or `fontFamily = Roboto` outside `Type.kt` | Direct font reference | Use `MaterialTheme.typography.*` style |
| `lineHeight = X.sp` | Hardcoded line height | Use `MaterialTheme.typography.*` style |
| `letterSpacing = X.sp` | Hardcoded letter spacing | Use `MaterialTheme.typography.*` style |

### 📐 Shape violations

| Pattern | Problem | Correct replacement |
|---------|---------|---------------------|
| `RoundedCornerShape(X.dp)` in composables (not in `Shape.kt`) | Hardcoded radius | Use `MaterialTheme.shapes.small/medium/large` |
| `CircleShape` used directly for non-icon elements | May be intentional, flag only | Consider `MaterialTheme.shapes.extraLarge` |

### 📏 Dimension violations (flag only, do not auto-fix)

| Pattern | Problem |
|---------|---------|
| Magic `.dp` values repeated 3+ times with same value | Candidate for extraction to a constant |
| Negative padding or offset without a comment | Potentially fragile layout |

---

## Step 4: Fix violations automatically

For each violation found:

1. **Read** the full file.
2. **Determine** the correct `MaterialTheme` token by matching the raw value to the Stadium DS palette or typography scale.
   - To match a hex color, compare it against `Color.kt` tokens, then find which `colorScheme` role uses it in `Theme.kt`.
   - To match a font size, compare it against `Type.kt` `AppTypography` styles.
3. **Replace** the raw value with the token.
4. **Add** the import `import androidx.compose.material3.MaterialTheme` if it is not already present.

If a raw value cannot be matched to any DS token (truly one-off value), **do not auto-fix** — flag it with:
> `⚠️ Unrecognized value — no matching DS token found. Manual review required.`

---

## Step 5: Verify fixes compile

After all fixes are applied, run:
```bash
./gradlew compileDevDebugKotlin
```

If it fails, read the error and revert only the problematic replacement, leaving the rest.

---

## Step 6: Generate report

```
## Design System Check — [branch or "full project"]
**Files scanned:** [N]
**Violations found:** [N] | **Auto-fixed:** [N] | **Manual review needed:** [N]

---

### ✅ Auto-fixed
| File | Line | Was | Now |
|------|------|-----|-----|
| [file:line] | [raw value] | [DS token] |

### ⚠️ Manual review required
| File | Line | Value | Reason |
|------|------|-------|--------|

### 📏 Dimension warnings (not auto-fixed)
[list or "None"]

### ✅ Files with no violations
[list]

---

### Verdict
[PASS / FIXED / NEEDS MANUAL REVIEW]
```

If fixes were applied, remind the user to run `/commit` to commit the changes.
