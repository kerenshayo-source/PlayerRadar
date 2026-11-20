# Player Radar — Minecraft Java 26.3 (Fabric)

Source revision addressing the 17 compiler errors in GitHub Actions build #2.

Changes: GuiGraphics -> GuiGraphicsExtractor; Screen.render -> extractRenderState; mouseClicked uses MouseButtonEvent; KeyMappingHelper and SDL key constants; HudElementRegistry; client.gui.setScreen; HUD hidden state; text() in lieu of drawString().

This source package has **not been compiled or tested in the game**. Follow the GitHub Actions result; if compilation fails, send the full build log.

**Important:** Only players already known to the client are visible. No server-wide tracking, terrain navigation or wallhacks.

Your GitHub repository currently stores the source under `PlayerRadar-26.3/` and the workflow at repository-root `.github/workflows/build.yml`. When updating, preserve those paths.
