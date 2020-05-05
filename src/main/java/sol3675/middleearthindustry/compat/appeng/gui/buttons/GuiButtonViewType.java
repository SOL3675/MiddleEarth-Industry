package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import appeng.api.config.ViewItems;
import appeng.core.localization.ButtonToolTips;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.EnumChatFormatting;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonViewType extends StateButton
{
    private String tooltipViewType = "";

    public GuiButtonViewType(final int ID, final int xPos, final int yPos, final int width, final int height)
    {
        super(ID, xPos, yPos, width, height, AEStateIcons.VIEW_TYPE_ALL, 0, 0, AEStateIcons.REGULAR_BUTTON);
        this.setViewMode(ViewItems.ALL);
    }

    @Override
    public void getToolTip(final List<String> tooltip)
    {
        this.addAboutToTooltip(tooltip, ButtonToolTips.View.getLocal(), EnumChatFormatting.GRAY + this.tooltipViewType);
    }

    public void setViewMode(final ViewItems mode)
    {
        switch (mode)
        {
            case ALL:
                this.tooltipViewType = ButtonToolTips.StoredCraftable.getLocal();
                this.stateIcon = AEStateIcons.VIEW_TYPE_ALL;
                break;

            case CRAFTABLE:
                this.tooltipViewType = ButtonToolTips.Craftable.getLocal();
                this.stateIcon = AEStateIcons.VIEW_TYPE_CRAFT;
                break;

            case STORED:
                this.tooltipViewType = ButtonToolTips.StoredItems.getLocal();
                this.stateIcon = AEStateIcons.VIEW_TYPE_STORED;
                break;
        }
    }
}
