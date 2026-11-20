package com.example.playerradar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class PlayerRadar implements ClientModInitializer {
    public static UUID selectedId;
    private static KeyMapping mapKey;
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("playerradar", "general"));
    private static final int BLUE=0xFF4DD0F0;

    @Override public void onInitializeClient() {
        mapKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.playerradar.open", InputConstants.Type.KEYBOARD, InputConstants.KEY_M, CATEGORY));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (mapKey.consumeClick()) client.gui.setScreen(new RadarScreen());
        });
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("playerradar", "radar_hud"), (gui, tickCounter) -> renderHud(gui));
    }
    public static List<Player> players(Minecraft mc) {
        List<Player> out=new ArrayList<>();
        if(mc.level==null || mc.player==null) return out;
        for(Player p:mc.level.players()) if(!p.getUUID().equals(mc.player.getUUID())) out.add(p);
        out.sort(Comparator.comparingDouble(p->p.distanceToSqr(mc.player)));
        return out;
    }
    public static Player target(Minecraft mc) {
        if(selectedId==null || mc.level==null) return null;
        for(Player p:mc.level.players()) if(selectedId.equals(p.getUUID())) return p;
        return null;
    }
    public static void renderHud(GuiGraphicsExtractor gui) {
        Minecraft mc=Minecraft.getInstance();
        if(mc.player==null || mc.level==null || mc.gui.hud.isHidden()) return;
        int width=gui.guiWidth(), x=width-124, y=8;
        drawRadar(gui,mc,x,y,116,64);
        gui.text(mc.font,"M : OPEN MAP",x+12,y+120,0xFFE1EBF5,true);
        Player t=target(mc);
        if(t!=null) {
            double dx=t.getX()-mc.player.getX(),dz=t.getZ()-mc.player.getZ();
            double dist=Math.hypot(dx,dz);
            double bearing=Math.toDegrees(Math.atan2(-dx,dz));
            double relative=Math.toRadians(bearing-mc.player.getYRot());
            String arrow=Math.cos(relative)>.7?"^":Math.cos(relative)<-.7?"v":Math.sin(relative)>0?"<":">";
            String label=arrow+" "+t.getName().getString()+"  "+(int)dist+"m";
            gui.fill(width/2-100,8,width/2+100,30,0xD8111825);
            gui.centeredText(mc.font,label,width/2,15,BLUE);
        } else if(selectedId!=null) {
            gui.centeredText(mc.font,"TARGET OUT OF RANGE",width/2,15,0xFFFFAA77);
        }
    }
    public static void drawRadar(GuiGraphicsExtractor g,Minecraft mc,int x,int y,int size,double radius) {
        g.fill(x,y,x+size,y+size,0xDD101B2B);
        g.fill(x,y,x+size,y+1,BLUE);g.fill(x,y+size-1,x+size,y+size,BLUE);
        g.fill(x,y,x+1,y+size,BLUE);g.fill(x+size-1,y,x+size,y+size,BLUE);
        int cx=x+size/2,cy=y+size/2;
        g.fill(cx,y+5,cx+1,y+size-5,0x334CB5D1);
        g.fill(x+5,cy,x+size-5,cy+1,0x334CB5D1);
        if(mc.player==null)return;
        for(Player player:players(mc)) {
            double dx=player.getX()-mc.player.getX(), dz=player.getZ()-mc.player.getZ();
            if(Math.hypot(dx,dz)>radius)continue;
            int px=cx+(int)Math.round(dx/radius*(size/2-8));
            int py=cy+(int)Math.round(dz/radius*(size/2-8));
            g.fill(px-3,py-3,px+4,py+4,player.getUUID().equals(selectedId)?0xFFFFFF40:0xFFFF637E);
        }
        g.fill(cx-3,cy-3,cx+4,cy+4,0xFF63E5A0);
        g.text(mc.font,"N",cx-3,y+5,0xFFEAF6FF,true);
        g.text(mc.font,((int)radius)+"m",x+5,y+size-12,0xFFB9C9D9,true);
    }
}
