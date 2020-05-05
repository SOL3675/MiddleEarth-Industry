package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class ButtonBaseAE extends GuiButton
{
    public ButtonBaseAE(final int ID, final int xPos, final int yPos, final int width, final int height, final String text)
    {
        super(ID, xPos, yPos, width, height, text);
    }

    public ButtonBaseAE(final int ID, final int xPos, final int yPos, final String text)
    {
        super(ID, xPos, yPos, text);
    }

    public abstract void getToolTip(final List<String> tooltip);

    public boolean isMouseOverButton(final int mouseX, final int mouseY)
    {
        return (mouseX >= this.xPosition) && (mouseX <= (this.xPosition + width)) && (mouseY >= this.yPosition) && (mouseY <= (this.yPosition + height));
    }
}
