# Texture import checklist

Place the PNG files in the mod resources tree at the exact paths below. Do not rename them: model and screen JSON references use these resource IDs.

## Block textures

Directory: `src/main/resources/assets/extragenerators/textures/block/`

- `blast_generator.png`
- `brew_generator.png`
- `burnable_generator.png`
- `colorful_generator.png`
- `demise_generator.png`
- `dragon_generator.png`
- `enchanted_generator.png`
- `generator_base.png`
- `generator_details.png`
- `gluttony_generator.png`
- `heavenly_generator.png`
- `icy_generator.png`
- `infernal_generator.png`
- `redstone_generator.png`
- `scalding_generator.png`
- `sludgy_generator.png`
- `steam_generator.png`
- `teleport_generator.png`
- `thermoelectric_generator.png`
- `withered_generator.png`

## GUI textures

Directory: `src/main/resources/assets/extragenerators/textures/gui/`

- `colorful_generator.png`
- `fluid_generator.png`
- `fluid_item_generator.png`
- `infinite_generator.png`
- `item_generator.png`

## Mod icon

Optional in-game/mod-list icon: `src/main/resources/assets/extragenerators/icon.png`

## Validation before building

1. Confirm every referenced PNG exists and is a valid PNG.
2. Keep transparent pixels and alpha channels intact.
3. Check all six block faces, especially the bottom face, for missing UVs or transparent gaps.
4. Build with Java 21 and Gradle 8.14.3.
5. Launch Minecraft 1.21.1 with NeoForge and check every generator model and GUI in-game.
