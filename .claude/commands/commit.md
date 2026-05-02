# /commit — Semantic Commit + Push

Runs the full commit flow following Conventional Commits.

## Step 1: Verify current branch

Run `git branch --show-current` and confirm the current branch is **not** `main` or `development`.

If it is, stop immediately:
> "⛔ You are on `[branch]`. Direct commits to `main` or `development` are not allowed. Create a feature branch with `/new-feature` or switch branches manually."

## Step 2: Check repository state

Run in parallel:
- `git status`
- `git diff --staged`

## Step 3: Evaluate staged changes

**If nothing is staged (`git diff --staged` is empty):**

Show modified files from `git status` and ask:
> "Nothing is staged. Found the following modified files:
> [file list]
>
> Stage all with `git add -A`? (yes/no) Or tell me which files to stage."

Wait for confirmation before continuing.

**If files are already staged:** continue to Step 4.

## Step 4: Analyze the changes

Read the full diff from `git diff --staged` and determine:

1. **Commit type:**
   - `feat` — new screen or feature
   - `fix` — bug fix
   - `refactor` — refactoring without behavior change
   - `chore` — maintenance, configuration, dependencies
   - `docs` — documentation only
   - `test` — tests only
   - `style` — formatting, import reordering (no logic change)
   - `perf` — performance improvement

2. **Optional scope** (which module): `auth`, `navigation`, `theme`, `home`, `settings`, `user`, `network`, `di`, `deps`, `components`

3. **Description** in English, concise, imperative mood (e.g., `add profile screen`)

## Step 5: Propose commit message

```
[type]([scope]): short description in English
```

Show the user a summary:

```
Files in this commit:
  [git diff --staged --stat output]

Proposed message:
  [type](scope): short description
```

**Wait for explicit user confirmation before continuing.** Options:
- `yes` / `ok` / `confirm` → proceed to Step 6
- `edit [new message]` → use the new message and confirm again
- `cancel` → stop without committing

## Step 6: Commit and push

Run the commit only after receiving explicit confirmation.

Then run `git push origin [current-branch]`.

If the push fails because there is no upstream yet:
`git push --set-upstream origin [current-branch]`

## Step 7: Confirm result

Show the push result and the hash of the created commit.
