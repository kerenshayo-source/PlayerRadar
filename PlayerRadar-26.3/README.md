# Player Radar Navigator - Minecraft Java 26.3 (Fabric)

**Status:** source-code adaptation only. Not compiled or tested against the 26.3 API; changes may be needed after the first build. This ZIP is NOT an installable .jar.

## Build
Requires Java 25, Gradle 9.6+, and Internet access for Fabric/Minecraft dependencies.
From this directory: `gradle build`.
Alternatively push the project (including `.github/workflows/build.yml`) to a GitHub repository and run Actions -> Build Minecraft 26.3 mod -> Run workflow. Download the built artifact if the workflow succeeds. On failure, read the compiler errors and update the API calls before trying again.

## Install
Install Fabric Loader for Minecraft Java 26.3 and matching Fabric API. Put the successful build output `build/libs/player-radar-26.3-1.1.0.jar` in `.minecraft/mods`. Do not install `-sources.jar` or `-dev.jar`.

## Controls
M opens map. Click a player name to select a navigation target. Escape closes the map.
Only player entities actually received by your client are visible, not all online players; names in tab alone do not give exact locations. Arrow indicates direct bearing, not walkable pathfinding. Respect the server rules.
