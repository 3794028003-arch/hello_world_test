---
name: git-github-workflow
description: Guide Git and GitHub repository work from cloning or initialization through a feature branch, reviewable commits, pushing, pull requests, review, and merge. Use when a user asks for Git setup, branch-based development, commits, GitHub push/authentication, pull requests, merge guidance, or help diagnosing common Git workflow errors.
---

# Git 与 GitHub 工作流程

Guide the user through a safe, branch-based workflow. Prefer explaining the next command briefly, then verifying its result before moving to a consequential step.

## 1. Establish context

Inspect state before changing it:

```powershell
git status
git branch --show-current
git remote -v
```

Ask only for missing, material choices: repository URL, base branch, or whether the change is ready to merge. Never infer that a remote branch or `main` should be overwritten.

For a new local repository, use:

```powershell
git init
git branch -M main
git remote add origin <repository-url>
```

For an existing GitHub repository, prefer cloning:

```powershell
git clone <repository-url>
cd <repository-folder>
```

## 2. Configure author identity when needed

If `git commit` reports `Author identity unknown`, set a name and an email chosen by the user. Use the email associated with GitHub (or GitHub’s no-reply email if privacy is desired).

```powershell
git config --global user.name "<name>"
git config --global user.email "<email>"
git config --global --get-regexp "^user\\.(name|email)$"
```

Use repository-local configuration instead of `--global` if the user needs a different identity for this repository:

```powershell
git config user.name "<name>"
git config user.email "<email>"
```

Do not invent or expose an email address.

## 3. Start an isolated change

Update the base branch only after checking that working changes will not be disturbed:

```powershell
git switch main
git pull --ff-only origin main
git switch -c <type/short-description>
```

Use a meaningful branch name, such as `feature/login-form`, `fix/null-check`, or `docs/setup-guide`. If the repository uses another base branch, substitute it consistently.

## 4. Review, stage, and commit

After making the requested edits, inspect exactly what will be committed:

```powershell
git status
git diff
git add <specific-files>
git diff --staged
git commit -m "<imperative summary>"
```

Prefer specific file paths over `git add .`. Use `git add .` only when the user has reviewed all intended changes. Keep commits focused and messages concise, for example `add hello world example` or `fix login validation`.

Before committing, check for accidental secrets, generated files, or unrelated edits. Do not commit credentials, tokens, `.env` files, or private keys.

## 5. Push and authenticate

Publish the branch and establish upstream tracking:

```powershell
git push -u origin <branch-name>
```

If Git opens a sign-in prompt, choose browser sign-in, authenticate to GitHub, authorize Git Credential Manager if requested, return to the terminal, then rerun the push if it did not resume. Do not request, display, or paste a password or personal access token into chat.

For subsequent updates:

```powershell
git push
```

## 6. Create and manage a pull request

Create a PR from `<branch-name>` into the agreed base branch. Write a title that describes the outcome and a concise body containing:

- What changed
- How it was tested
- Any review or deployment notes

Resolve review feedback with new commits, push them, and confirm the PR updates. Before merging, verify CI/review requirements and that the PR base and head branches are correct.

After merging, synchronize local state and remove only the merged feature branch:

```powershell
git switch main
git pull --ff-only origin main
git branch -d <branch-name>
git push origin --delete <branch-name>
```

Only delete the remote branch when the PR is merged and the user’s repository policy allows it.

## Troubleshooting

| Symptom | Safe response |
| --- | --- |
| `Author identity unknown` | Configure `user.name` and `user.email`, then repeat the commit. |
| Authentication prompt or push denied | Complete browser sign-in; check `git remote -v` and repository permission. |
| Non-fast-forward rejection | Fetch/pull the target branch, integrate or rebase deliberately, resolve conflicts, test, then push. Do not force-push by default. |
| Merge conflict | Stop, show the conflicting files with `git status`, resolve and test each file, then stage and continue. |
| Changes on the wrong branch | Do not discard them. Inspect with `git status`/`git diff`; use a new branch, commit, or stash only after explaining the consequence. |

Avoid destructive commands (`reset --hard`, `clean -fd`, force-push) unless the user explicitly asks, the exact target is verified, and the recovery impact is explained.
