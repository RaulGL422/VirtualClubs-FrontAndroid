# /create-pr — Create Pull Request

Creates a PR from the current branch to `development` (default) or a specified base.

## Usage
- `/create-pr` — creates PR targeting `development`
- `/create-pr main` — creates PR targeting `main`

## Step 1: Verify preconditions

Run `git branch --show-current`.

If the branch is `main` or `development`, stop:
> "You cannot create a PR from `main` or `development`."

Default base branch is `development`. If an argument was passed (e.g. `main`), use it as the base.

Verify there are commits above the base branch:
```bash
git log [base-branch]...HEAD --oneline
```

If there are no commits, stop:
> "No changes to create a PR from. Commit your changes first with `/commit`."

## Step 2: Review the changes

Run in parallel (using the base branch determined above):
- `git log [base-branch]...HEAD --oneline` — all commits in the PR
- `git diff [base-branch]...HEAD --stat` — summary of changed files
- `git diff [base-branch]...HEAD` — full diff for context

## Step 3: Verify gh authentication

```bash
gh auth status
```

If not authenticated, stop and inform:
> "The GitHub CLI (`gh`) is not authenticated. Run `gh auth login` in your terminal and try again."

## Step 4: Generate title and description

Based on commits and diff, generate:

**Title** (max 70 characters): Clear and descriptive, in English.

**Description** with sections:
- **What does this PR do?** — 2-3 bullet summary
- **Main changes** — list of files and what changed
- **How to test** — steps to verify on device/emulator
- **Checklist** — checkboxes: tests pass, no secrets in code, CLAUDE.md updated if needed, security reviewed, build verified

## Step 5: Confirm and create the PR

Show the user the generated title and description and ask:
> "Create the PR with this title and description? (yes/edit/cancel)"

Wait for confirmation before continuing.

If confirmed, push the branch and create the PR:
```bash
git push --set-upstream origin [current-branch]
gh pr create --title "[title]" --body "[description]" --base [base-branch]
```

## Step 6: Launch automatic review

After creating the PR, automatically run `/review-pr [number]` to get an immediate review of the newly created PR.

Show the PR URL at the end.
