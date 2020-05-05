package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import com.google.common.base.Splitter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class StateButton extends ButtonBaseAE
{
    private AEStateIcons backgroundIcon;
    private int iconXOffset = 0;
    private int iconYOffset = 0;
    protected AEStateIcons stateIcon;

    public StateButton(final int ID, final int xPos, final int yPos, final int buttonWidth, final int buttonHeight, final AEStateIcons icon, final int iconXOffset, final int iconYOffset, final AEStateIcons backgroundIcon)
    {
        super(ID, xPos, yPos, buttonWidth, buttonHeight, "");
        this.stateIcon = icon;
        this.iconXOffset = iconXOffset;
        this.iconYOffset = iconYOffset;
        this.backgroundIcon = backgroundIcon;
    }

    private void drawScaledTexturedModalRect(final int xPos, final int yPos, final int u, final int v, final int width, final int height, final int textureWidth, final int textureHeight)
    {
        float magic_number = 0.00390625F;
        float minU = u * magic_number;
        float maxU = ( u + textureWidth ) * magic_number;
        float minV = v * magic_number;
        float maxV = ( v + textureHeight ) * magic_number;
        double dHeight = height;
        double dWidth = width;

        Tessellator ts = Tessellator.instance;
        ts.startDrawingQuads();
        ts.addVertexWithUV(xPos, yPos + dHeight, this.zLevel, minU, maxV);
        ts.addVertexWithUV(xPos + dWidth, yPos + dHeight, this.zLevel, maxU, maxV);
        ts.addVertexWithUV(xPos + dWidth, yPos, this.zLevel, maxU, minV);
        ts.addVertexWithUV(xPos, yPos, this.zLevel, minU, minV);
        ts.draw();
    }

    protected void addAboutToTooltip(final List<String> tooltip, final String title, final String text)
    {
        tooltip.add(EnumChatFormatting.WHITE + title);

        for (String line : Splitter.on("\n").split(WordUtils.wrap(text, 30, "\n", false)))
        {
            tooltip.add(EnumChatFormatting.GRAY + line.trim());
        }
    }

    protected void drawIcon(final Minecraft minecraftInstance, final AEStateIcons icon, final int xPos, final int yPos, final int iconWidth, final int iconHeight)
    {
        minecraftInstance.getTextureManager().bindTexture(icon.getTexture());
        this.drawScaledTexturedModalRect(xPos + this.iconXOffset, yPos + this.iconYOffset, icon.getU(), icon.getV(), iconWidth, iconHeight, icon.getWidth(), icon.getHeight());
    }

    @Override
    public void drawButton(final Minecraft minecraftInstance, final int x, final int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(this.backgroundIcon != null)
        {
            this.drawIcon(minecraftInstance, this.backgroundIcon, this.xPosition, this.yPosition, this.width, this.height);
        }
        if(this.stateIcon != null)
        {
            this.drawIcon(minecraftInstance, this.stateIcon, this.xPosition + this.iconXOffset, this.yPosition + this.iconYOffset, this.width, this.height);
        }
    }
}
