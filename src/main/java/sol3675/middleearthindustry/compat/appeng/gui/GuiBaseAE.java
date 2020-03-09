package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.parts.automation.UpgradeInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.ButtonBaseAE;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;
import sol3675.middleearthindustry.references.Constant;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiBaseAE extends GuiContainer
{
    private static final int upgradeU = AEStateIcons.UPGRADE_CARD_BACKGROUND.getU();
    private static final int upgradeV = AEStateIcons.UPGRADE_CARD_BACKGROUND.getV();
    private static final int upgradeWidth = AEStateIcons.UPGRADE_CARD_BACKGROUND.getWidth();
    private static final int upgradeHeight = AEStateIcons.UPGRADE_CARD_BACKGROUND.getHeight();

    protected final List<String> toolTip = new ArrayList<String>();

    public GuiBaseAE(final Container container)
    {
        super(container);
    }

    private final boolean addTooltipFromButtons(final int mouseX, final int mouseY)
    {
        for(Object obj : this.buttonList)
        {
            if(obj instanceof ButtonBaseAE)
            {
                ButtonBaseAE currentButton = (ButtonBaseAE)obj;
                if(currentButton.isMouseOverButton(mouseX, mouseY))
                {
                    currentButton.getToolTip(this.toolTip);
                    return true;
                }
            }
        }
        return false;
    }

    private final boolean isPointWithinSlot(final Slot slot, final int x, final int y)
    {
        return ((x - this.guiLeft) >= slot.xDisplayPosition) && ((x - this.guiLeft) <= (slot.xDisplayPosition + 16)) && ((y - this.guiTop) >= slot.yDisplayPosition) && ((y - this.guiTop) <= slot.yDisplayPosition + 16);
    }

    private final boolean nonLeftClickHandler_Buttons(final int mouseX, final int mouseY, final int mouseButton)
    {
        if(mouseButton != Constant.MOUSE_BUTTON_LEFT)
        {
            for(Object buttonObj : this.buttonList)
            {
                GuiButton button = (GuiButton)buttonObj;
                if(button.mousePressed(this.mc, mouseX, mouseY))
                {
                    button.func_146113_a(this.mc.getSoundHandler());
                    this.onButtonClicked(button, mouseButton);
                    return true;
                }
            }
        }
        return false;
    }

    protected final void drawAEToolAndUpgradeSlots(final float alpha, final int mouseX, final int mouseY)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(AEStateIcons.AE_STATES_TEXTURE);

        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
            if(slot.inventory instanceof UpgradeInventory)
            {
                this.drawTexturedModalRect(this.guiLeft + slot.xDisplayPosition, this.guiTop + slot.yDisplayPosition, GuiBaseAE.upgradeU, GuiBaseAE.upgradeV, GuiBaseAE.upgradeWidth, GuiBaseAE.upgradeHeight);
            }
        }
    }

    protected final Slot getSlotAtMousePosition(final int x, final int y)
    {
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); i++)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i);
            if(this.isPointWithinSlot(slot, x, y))
            {
                return slot;
            }
        }
        return null;
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
    {
        if(this.nonLeftClickHandler_Buttons(mouseX, mouseY, mouseButton))
        {
            return;
        }

        //if(this.inventorySlots instanceof )
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void onButtonClicked(final GuiButton button, final int mouseButton)
    {
    }

    @Override
    protected void actionPerformed(final GuiButton button)
    {
        this.onButtonClicked(button, Constant.MOUSE_BUTTON_LEFT);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float mouseButton)
    {
        super.drawScreen(mouseX, mouseY, mouseButton);

        if(this.toolTip.isEmpty())
        {
            this.addTooltipFromButtons(mouseX, mouseY);
        }
        if(!this.toolTip.isEmpty())
        {
            this.drawHoveringText(this.toolTip, mouseX, mouseY, this.fontRendererObj);
            this.toolTip.clear();
        }
    }

    public final FontRenderer getFontRenderer()
    {
        return this.fontRendererObj;
    }

    public final int guiLeft()
    {
        return this.guiLeft;
    }

    public final int guiTop()
    {
        return this.guiTop;
    }
}
