# /new-feature — Create Feature Branch

Creates a correctly named working branch from `development` (default) or a specified base branch.

## Usage
- `/new-feature add profile screen` — creates branch from description
- `/new-feature fix refresh token on network change`
- `/new-feature add profile screen --from main` — use a different base branch

---

## Step 1: Check working state

Run `git status`. If there are uncommitted changes, warn the user:
> "You have uncommitted changes on the current branch. Do you want me to stash them before switching? (yes/no)"

---

## Step 2: Determine branch name

Based on the description, determine type and name:

**Branch type:**
- `feature/` — new screen or feature
- `fix/` — bug fix
- `refactor/` — refactoring
- `test/` — tests
- `chore/` — maintenance, dependencies
- `docs/` — documentation only

**Name:** kebab-case, ASCII only (no accents, no special characters), max 40 characters.

**Full format:** `[type]/[short-name]`

Examples: `feature/profile-screen`, `fix/refresh-token-network`, `chore/update-dependencies`

Propose the name to the user before creating the branch.

---

## Step 3: Create the branch

Default base is `development`. If `--from <branch>` was passed, use that.

```bash
git fetch origin [base-branch]
git checkout [base-branch]
git pull origin [base-branch]
git checkout -b [branch-name]
```

## Step 4: Initial push to remote

```bash
git push --set-upstream origin [branch-name]
```

## Step 5: Confirm

Show the created branch name and remind the user to use `/commit` for changes and `/create-pr` when done.
