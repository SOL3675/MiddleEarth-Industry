package sol3675.middleearthindustry.util;

import cofh.api.energy.EnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;
import sol3675.middleearthindustry.references.ModInfo;

import java.util.Iterator;
import java.util.List;

public class ClientUtil
{

    public static Minecraft mc()
    {
        return Minecraft.getMinecraft();
    }

    public static FontRenderer font()
    {
        return mc().fontRenderer;
    }

    public static Tessellator tes()
    {
        return Tessellator.instance;
    }

    public static void bindTexture(String path)
    {
        mc().getTextureManager().bindTexture(new ResourceLocation(path));
    }

    public static void drawColouredRect(int x, int y, int w, int h, int colour)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        tes().startDrawingQuads();
        tes().setColorRGBA_I(colour, colour>>24&255);
        tes().addVertex(x, y+h, 0);
        tes().addVertex(x+w, y+h, 0);
        tes().addVertex(x+w, y, 0 );
        tes().addVertex(x, y, 0);
        tes().draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawGradientRect(int x0, int y0, int x1, int y1, int colour0, int colour1)
    {
        float f = (float)(colour0>>24&255)/255F;
        float f1 = (float)(colour0>>16&255)/255F;
        float f2 = (float)(colour0>>8&255)/255F;
        float f3 = (float)(colour0&255) / 255F;
        float f4 = (float)(colour1>>24&255)/255F;
        float f5 = (float)(colour1>>16&255)/255F;
        float f6 = (float)(colour1>>8&255)/255F;
        float f7 = (float)(colour1&255)/255F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double)x1, (double)y0, 0);
        tessellator.addVertex((double)x0, (double)y0, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)x0, (double)y1, 0);
        tessellator.addVertex((double)x1, (double)y1, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawHoveringText(List<String> list, int x, int y, FontRenderer font, int xSize, int ySize)
    {
        if(!list.isEmpty())
        {
            boolean uni = ClientUtil.font().getUnicodeFlag();
            ClientUtil.font().setUnicodeFlag(false);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext())
            {
                String s = iterator.next();
                int l = font.getStringWidth(s);
                if(l > k)
                {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            boolean shift = false;
            if(xSize > 0 && j2 + k > xSize)
            {
                j2 -= 28 + k;
                shift = true;
            }
            if(ySize>0 && k2 + i1 + 6 > ySize)
            {
                k2 = ySize - i1 - 6;
                shift = true;
            }
            if(!shift && mc().currentScreen != null)
            {
                if(j2 + k > mc().currentScreen.width)
                {
                    j2 -= 28 + k;
                }
                if(k2 + i1 + 6 > mc().currentScreen.height)
                {
                    k2 = mc().currentScreen.height - i1 - 6;
                }
            }

            if(list.size() > 1)
            {
                i1 += 2 + (list.size() - 1) * 10;
            }
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = ((k1 & 16711422) >> 1 | k1 & -16777216);
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for(int i2 = 0; i2 < list.size(); ++i2)
            {
                String s1 = (String)list.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);
                if(i2 == 0)
                {
                    k2 += 2;
                }
                k2 += 10;
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            ClientUtil.font().setUnicodeFlag(uni);
        }
    }

    public static void drawTexturedRect(float x, float y, float w, float h, double... uv)
    {
        tes().startDrawingQuads();
        tes().addVertexWithUV(x, y+h, 0, uv[0], uv[3]);
        tes().addVertexWithUV(x+w, y+h, 0, uv[1], uv[3]);
        tes().addVertexWithUV(x+w, y, 0, uv[1], uv[2]);
        tes().addVertexWithUV(x, y, 0, uv[0], uv[2]);
        tes().draw();
    }

    public static void drawTexturedRect(int x, int y, int w, int h, float picSize, int... uv)
    {
        double[] d_uv = new double[]{uv[0]/picSize,uv[1]/picSize, uv[2]/picSize,uv[3]/picSize};
        drawTexturedRect(x,y,w,h, d_uv);
    }

    public static void drawEnergyBar(TileEntityMeiMachine tile, int xPos, int yPos)
    {
        bindTexture(ModInfo.TEXTUREPREFIX + "textures/gui/elements/powerbar.png");
        drawTexturedRect(xPos, yPos, 13, 61, 256f, 0, 13, 0, 61);
        int stored = (int)(53 * (tile.energyStorage.getEnergyStored() / (float)tile.energyStorage.getMaxEnergyStored()));
        drawGradientRect(xPos + 2, yPos + 2 + (53 - stored), xPos + 10, yPos + 55, 0xffb51500, 0xff600b00);
    }
}
