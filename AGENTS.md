# AGENTS.md
Guide for agentic coding tools operating in `touche-android`.

## Scope and Stack
- Android app written in Kotlin with Jetpack Compose.
- Gradle Kotlin DSL build scripts.
- Single module: `:app`.
- Package root: `dev.bpavuk.touche`.
- JDK toolchain: 17.
- Android SDK: compile/target 36, min 24.
- Main runtime pieces: USB accessory IO, input mapping, Compose UI.

## Environment
- Prefer `./gradlew` over system Gradle.
- Android Studio + JDK 17 + Android SDK 36 is the standard setup.
- Nix dev shell is available (`flake.nix`, `devshell.nix`) and includes `android-sdk`, `gradle`, `jdk17`.

## Build Commands
- Full app build: `./gradlew :app:build`
- Assemble debug APK: `./gradlew :app:assembleDebug`
- Assemble release APK: `./gradlew :app:assembleRelease`
- Build all bundles: `./gradlew :app:bundle`
- Clean outputs: `./gradlew :app:clean`
- Project-level clean: `./gradlew clean`
- CI release workflow currently uses: `./gradlew assembleRelease --no-daemon`

## Lint and Verification
- Run lint (default variant): `./gradlew :app:lint`
- Run lint for debug: `./gradlew :app:lintDebug`
- Run lint for release: `./gradlew :app:lintRelease`
- Apply safe lint fixes: `./gradlew :app:lintFix`
- Run all checks: `./gradlew :app:check`
- No Detekt config found.
- No ktlint config found.

## Test Commands
- Run all unit tests: `./gradlew :app:test`
- Run debug unit tests: `./gradlew :app:testDebugUnitTest`
- Run release unit tests: `./gradlew :app:testReleaseUnitTest`
- Run connected instrumentation tests: `./gradlew :app:connectedDebugAndroidTest`
- Note: there are currently no committed test files, but tasks are configured.

## Single-Test Recipes (Important)
- Single unit test class:
  - `./gradlew :app:testDebugUnitTest --tests "dev.bpavuk.touche.SomeTest"`
- Single unit test method:
  - `./gradlew :app:testDebugUnitTest --tests "dev.bpavuk.touche.SomeTest.someMethod"`
- Single instrumentation test class:
  - `./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=dev.bpavuk.touche.SomeUiTest`
- Single instrumentation test method:
  - `./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=dev.bpavuk.touche.SomeUiTest#someMethod`
- If test filtering fails, use fully qualified class names and verify package path.

## Fast Validation Flow for Agents
- Small UI/code tweak:
  1. `./gradlew :app:assembleDebug`
  2. `./gradlew :app:lintDebug`
  3. Run targeted test command when tests exist for touched code.
- Data/input/runtime logic change:
  1. `./gradlew :app:testDebugUnitTest`
  2. `./gradlew :app:assembleDebug`
  3. `./gradlew :app:lintDebug`
- Broad refactor or dependency change:
  1. `./gradlew :app:build`
  2. `./gradlew :app:lint`
  3. Run connected tests if behavior depends on actual device input.

## Code Style Baseline
- Follow Kotlin official style (`kotlin.code.style=official`).
- Keep changes consistent with local patterns already in use.
- Prefer simple, readable code over clever abstractions.

## Imports
- Prefer explicit imports in new/edited files.
- Avoid wildcard imports in new code.
- Remove unused imports before finishing.
- Keep import groups stable and readable (androidx, kotlin/kotlinx, project).

## Formatting
- 4-space indentation, no tabs.
- Keep long parameter lists multiline.
- Use trailing commas in multiline declarations/calls.
- Keep braces and line breaks consistent with surrounding code.
- Do not reformat unrelated files.

## Types and Nullability
- Prefer `val` over `var`.
- Add explicit types when it improves API clarity.
- Keep nullability explicit and intentional.
- Use sealed/data types for protocol/domain payloads (`ToucheInput` pattern).
- Keep interfaces at boundaries (`ToucheRepository`, `InputViewModel`).

## Naming
- Classes/interfaces/objects: `UpperCamelCase`.
- Functions/properties/local vars: `lowerCamelCase`.
- Constants: `UPPER_SNAKE_CASE`.
- Compose screens/components should use descriptive nouns (`SettingsScreen`, `StylusSurface`).
- Test names should encode behavior and expected result.

## Compose Guidelines
- Prefer stateless composables and pass state/events explicitly.
- Include `modifier: Modifier = Modifier` for reusable composables.
- Keep previews close to the component they validate.
- Use private preview functions unless external access is needed.
- Reuse `MultiDevicePreview` where multi-device snapshots help.

## Coroutines and Threading
- Launch UI-initiated work from `viewModelScope`.
- Keep blocking IO on `Dispatchers.IO`.
- Keep suspend functions focused and side effects clear.
- Avoid hidden background work in composables.

## Error Handling and Logging
- Fail fast on invalid runtime preconditions (USB intent/accessory state).
- Throw specific exceptions with actionable messages.
- Catch exceptions only where recovery behavior is defined.
- Use stable log tags and log lifecycle transitions around USB and activity setup.
- Do not swallow exceptions silently.

## Android and Resources
- Prefer string resources for new user-facing text.
- Keep drawables/strings referenced through `R`.
- Follow existing manifest and activity patterns unless change is required.
- Keep SDK/version config centralized in Gradle scripts.

## Project Organization
- `data/`: USB transport and repository layer.
- `input/`: pointer/screen mapping and view-model logic.
- `ui/`: Compose screens/components/theme/surfaces.
- "app root" means package root (`dev.bpavuk.touche`, near `MainActivity`), not a literal `app_root` package.
- Keep files focused; avoid mixing unrelated responsibilities.

## Comments and Docs
- Add comments only for non-obvious rationale, invariants, or platform quirks.
- Avoid comments that restate obvious code.
- Keep TODOs specific and actionable.

## Dependency and Build Script Changes
- Prefer updating versions in `gradle/libs.versions.toml`.
- Add dependencies only when needed for current change.
- Keep AGP/Kotlin upgrades explicit and scoped.
- Validate build after any dependency/plugin change.

## Testing Conventions for New Tests
- Unit tests go in `app/src/test/...`.
- Instrumented tests go in `app/src/androidTest/...`.
- Use behavior-focused test names (example: `sendScreenEvent_emitsInitThenScreen`).
- For bug fixes, add/extend tests when feasible.

## Pre-PR Agent Checklist
- Build passes for touched scope.
- Relevant lint tasks pass.
- Relevant tests pass, or explain why not run.
- No unrelated file churn.
- Imports cleaned; resources/strings handled correctly.
- Commit messages explain why the change exists.
