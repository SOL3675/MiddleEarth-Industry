package sol3675.middleearthindustry.compat.appeng.gui.widget;

import appeng.api.storage.data.IAEItemStack;
import appeng.client.render.AppEngRenderItem;
import appeng.util.item.AEItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import sol3675.middleearthindustry.compat.appeng.gui.GuiBaseAE;

import java.util.List;

public class WidgetAEItem extends WidgetBaseAE
{
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final TextureManager TEXTURE_MANAGER = Minecraft.getMinecraft().getTextureManager();
    private final AppEngRenderItem aeItemRenderer;
    private IAEItemStack aeItemStack;

    public WidgetAEItem(final GuiBaseAE hostGUI, final int xPos, final int yPos, final AppEngRenderItem aeItemRenderer)
    {
        super(hostGUI, xPos, yPos);
        this.aeItemRenderer = aeItemRenderer;
    }

    @Override
    public void drawWidget()
    {
        try
        {
            if(this.aeItemStack != null)
            {
                this.zLevel = 2.0F;
                this.aeItemRenderer.zLevel = 2.0F;
                this.aeItemRenderer.setAeStack(this.aeItemStack);
                this.aeItemRenderer.renderItemAndEffectIntoGUI(WidgetAEItem.MC.fontRenderer, WidgetAEItem.TEXTURE_MANAGER, this.aeItemStack.getItemStack(), this.xPos + 1, this.yPos + 1);
                this.zLevel = 0.0F;
                this.aeItemRenderer.zLevel = 0.0F;
            }
        }
        catch (Exception e)
        {
        }
    }

    public IAEItemStack getItemStack()
    {
        return this.aeItemStack;
    }

    @Override
    public void getTooltip(List<String> tooltip)
    {
        if(this.aeItemStack != null)
        {
            ItemStack itemStack = this.aeItemStack.getItemStack();

            try
            {
                List<String> stackTooltip = itemStack.getTooltip(WidgetAEItem.MC.thePlayer, WidgetAEItem.MC.gameSettings.advancedItemTooltips);

                for (int index = 0; index < stackTooltip.size(); index++)
                {
                    if(index == 0)
                    {
                        stackTooltip.set(index, itemStack.getRarity().rarityColor + stackTooltip.get(index));
                    }
                    else
                    {
                        stackTooltip.set(index, EnumChatFormatting.GRAY + stackTooltip.get(index));
                    }
                    tooltip.add(stackTooltip.get(index));
                }
            }
            catch (Exception e)
            {
                tooltip.add(EnumChatFormatting.ITALIC + "<Unable to get item tooltip>");
            }

            String modName = ((AEItemStack)this.aeItemStack).getModID();
            modName = modName.substring(0, 1).toUpperCase() + modName.substring(1);
        }
    }

    @Override
    public void onMouseClicked()
    {
    }

    public void setItemStack(final IAEItemStack itemStack)
    {
        this.aeItemStack = itemStack;
    }
}
