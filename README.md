# Extra Generators — NeoForge 1.21.1

This repository is the in-progress NeoForge port of the original Extra Generators mod.

## Target
- Minecraft: 1.21.1
- Mod loader: NeoForge
- Java: 21
- Mod ID: `extragenerators`

## Build
GitHub Actions builds the project on pushes to `main`, pull requests, and manual workflow dispatch. The resulting JAR is uploaded as the workflow artifact `extra-generators-neoforge-1.21.1`.

For a local build with Java 21 and Gradle 8.14.3 installed:
```bash
gradle --no-daemon build
```

## Porting status
This initial commit contains only the NeoForge build scaffold and mod entrypoint. The original Fabric implementation still needs to be ported: generator blocks and block entities, energy storage/integration, menus and screens, networking, recipes, tags, models, textures, and translations.

No functional generator content is claimed until those systems have been implemented and tested.
