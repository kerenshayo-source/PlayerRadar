package com.example.playerradar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import java.util.List;

public class RadarScreen extends Screen {
    private static final int ROW_HEIGHT=22;
    public RadarScreen() { super(Component.literal("Player Radar")); }
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float delta) {
        super.render(g,mouseX,mouseY,delta);
        if(minecraft==null || minecraft.player==null)return;
        int mapSize=Math.max(80,Math.min(280,Math.min(width/2,height-76)));
        int mapX=20,mapY=35,listX=mapX+mapSize+24;
        g.drawString(font,"PLAYER RADAR",20,14,0xFF4DD0F0,true);
        PlayerRadar.drawRadar(g,minecraft,mapX,mapY,mapSize,256);
        g.drawString(font,"PLAYERS RECEIVED FROM SERVER",listX,35,0xFFEAF4FA,true);
        List<Player> players=PlayerRadar.players(minecraft);
        int maxRows=Math.max(0,(height-90)/ROW_HEIGHT);
        if(players.isEmpty())g.drawString(font,"No players currently visible",listX,62,0xFFB7C6D5,true);
        for(int i=0;i<Math.min(maxRows,players.size());i++) {
            Player p=players.get(i);
            int ry=59+i*ROW_HEIGHT;
            boolean chosen=p.getUUID().equals(PlayerRadar.selectedId);
            boolean hovered=mouseX>=listX&&mouseX<width-12&&mouseY>=ry&&mouseY<ry+19;
            g.fill(listX,ry,width-12,ry+19,chosen?0xFF1A6A79:hovered?0xFF31465C:0xB51F3044);
            String label=p.getName().getString()+" ("+(int)Math.sqrt(p.distanceToSqr(minecraft.player))+"m)";
            g.drawString(font,label,listX+7,ry+5,chosen?0xFFFFFF8C:0xFFFFFFFF,true);
        }
        g.drawString(font,"Click player to navigate | Esc closes",listX,height-28,0xFFABBBCE,true);
        g.drawString(font,"Green: you  Red: player  Yellow: target",20,height-28,0xFFABBBCE,true);
    }
    @Override public boolean mouseClicked(double mouseX,double mouseY,int button) {
        if(button==0&&minecraft!=null) {
            int mapSize=Math.max(80,Math.min(280,Math.min(width/2,height-76)));
            int listX=20+mapSize+24,row=(int)(mouseY-59)/ROW_HEIGHT;
            List<Player> players=PlayerRadar.players(minecraft);
            int maxRows=Math.max(0,(height-90)/ROW_HEIGHT);
            if(mouseX>=listX&&mouseX<width-12&&mouseY>=59&&row>=0&&row<Math.min(maxRows,players.size())&&mouseY<59+row*ROW_HEIGHT+19) {
                PlayerRadar.selectedId=players.get(row).getUUID();onClose();return true;
            }
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }
    @Override public boolean isPauseScreen(){return false;}
}
