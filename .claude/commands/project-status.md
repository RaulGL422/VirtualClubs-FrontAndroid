# /project-status — Project Status Overview

Generates a summary of the current state of the Android project.

## Step 1: Git and GitHub state

Run in parallel:
```bash
git branch --show-current
git log --oneline -8
git status --short
git stash list
gh pr list --state open --json number,title,headRefName,createdAt
```

## Step 2: Search for technical debt in the code

Use Grep to search for TODOs, FIXMEs, and bad practices in `app/src/main/java`:
- `TODO`, `FIXME`, `HACK`
- `println`, `Log.d` (possible sensitive data in debug logs)

Also check the current state of `HomePage.kt` (screen pending full club management content).

## Step 3: Verify compilation

```bash
./gradlew compileDevDebugKotlin -q 2>&1 | tail -5
```

## Step 4: Generate report

```
## Project Status — Virtual Clubs Android — [current date]

### Git
- Current branch: [branch]
- Recent commits: [list]
- Uncommitted changes: [N files] / Clean
- Saved stashes: [N] / None
- Open PRs: [list with number and title] / None

### Build
- Status: [OK / Errors found]

### Technical Debt
- TODOs/FIXMEs found: [list with file:line] / None
- Screens pending implementation:
  - [ ] Home screen (real club content)
  - [ ] [others found]
- Technical debt from CLAUDE.md:
  - [ ] UI/instrumentation tests not implemented
  - [ ] UserApi limited to email only
  - [ ] [others]

### Suggested next action
[One concrete action based on the current state]
```
