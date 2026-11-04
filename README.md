# Extra Generators — NeoForge 1.21.1

NeoForge port of Extra Generators for Minecraft 1.21.1.

## Target platform
- Minecraft 1.21.1
- NeoForge 21.1.x
- Java 21
- Mod ID: `extragenerators`

## Build
The GitHub Actions workflow builds on pushes to `main`, pull requests, and manual dispatch. It uploads the resulting JAR as a workflow artifact.

Local build requirements: Java 21 and Gradle 8.14.3.
```bash
gradle --no-daemon build
```

## Port status
The port is in progress. The implementation work covers the 18 existing generator types, block entities, FE storage/transfer, menus and screens, recipes, translations, blockstates and model JSON. Do not treat the project as release-ready until the Gradle build and in-game checks pass.

## Textures
PNG assets are intentionally handled separately. Their expected resource locations and filenames are listed in [ASSET_IMPORT.md](ASSET_IMPORT.md). Keep the namespace `assets/extragenerators` and preserve the exact filenames referenced by the model and GUI JSON files.

## Scope
The current port preserves the existing generator set and mechanics. Diesel/gasoline variants and additional Create fuel integrations are not part of this pass.
