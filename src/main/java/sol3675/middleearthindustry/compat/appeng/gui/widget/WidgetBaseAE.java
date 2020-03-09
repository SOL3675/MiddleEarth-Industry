package sol3675.middleearthindustry.compat.appeng.gui.widget;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import sol3675.middleearthindustry.compat.appeng.gui.GuiBaseAE;

import java.util.List;

public abstract class WidgetBaseAE extends Gui
{
    public static final int WIDGET_SIZE = 18;
    protected int xPos, yPos;
    protected GuiBaseAE hostGui;

    public WidgetBaseAE(final GuiBaseAE hostGui, final int xPos, final int yPos)
    {
        this.hostGui = hostGui;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void drawMouseHoverUnderlay()
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        this.drawGradientRect(this.xPos + 1, this.yPos + 1, this.xPos + 17, this.yPos + 17, 0x80FFFF, 0x80FFFF);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public abstract void drawWidget();

    public abstract void getTooltip(List<String> tooltip);

    public boolean isMouseOverWidget(final int mouseX, final int mouseY)
    {
        return ((mouseX - this.hostGui.guiLeft()) >= this.xPos) && ((mouseX - this.hostGui.guiLeft()) <= (this.xPos + WidgetBaseAE.WIDGET_SIZE - 1)) && ((mouseY - this.hostGui.guiTop()) >= this.yPos) && ((mouseY - this.hostGui.guiTop()) <= (this.yPos + WidgetBaseAE.WIDGET_SIZE - 1));
    }

    public abstract void onMouseClicked();

    public void setPosition(final int xPos, final int yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }
}
