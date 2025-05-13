-m # On contributing to the repo

## Why this exists
 To ensure clean, consistent, collaborative work. We're all working together, and having outlined standards will make it easier for markers to breakdown.
 
## Git Workflow & Commit Standards
- Always work in a feature branch, don't commit to master
- Commit messages follow  `type: description`
    - e.g. feat: added signin-page
    - docs: added documentation for signin-page
    - fix: fixed a null reference
- Some standard types are: `feat`, `fix`, `refactor`, `chore`, `docs`

See [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) for more information

## Pull request process
- Open PR against `master` branch when your feature is ready, describing the feature and what you've done
- Request a review from atleast one other team member
- Don't merge your own PRs without feedback, unless it's urgent and communicated
- If a pull-request is ***ONLY*** non-breaking documentation/chore work (i.e., doesn't touch `/src/`), then a fast-tracked merge is acceptable

## Why /docs/ exists
- Docs explain how the app works and how to contribute without guesswork
- New features must be accompanied by doc updates
- This is to enforce maintainability as the project grows in complexity, preventing patch-work approaches