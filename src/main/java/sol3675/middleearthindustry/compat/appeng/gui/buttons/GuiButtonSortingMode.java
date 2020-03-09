package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import appeng.api.config.SortOrder;
import appeng.core.localization.ButtonToolTips;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonSortingMode extends StateButton
{
    private String tooltipSortBy = "";

    public GuiButtonSortingMode(final int ID, final int xPos, final int yPos, final int width, final int height)
    {
        super(ID, xPos, yPos, width, height, AEStateIcons.SORT_MODE_ALPHABETIC, 0, 0, AEStateIcons.REGULAR_BUTTON);
    }

    @Override
    public void getToolTip(final List<String> tooltip)
    {
        this.addAboutToTooltip(tooltip, ButtonToolTips.SortBy.getLocal(), EnumChatFormatting.GRAY + this.tooltipSortBy);
    }

    public void setSortMode(final SortOrder order)
    {
        switch (order)
        {
            case AMOUNT:
                this.stateIcon = AEStateIcons.SORT_MODE_AMOUNT;
                this.tooltipSortBy = StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.NumberOfItems");
                break;

            case INVTWEAKS:
                this.stateIcon = AEStateIcons.SORT_MODE_INVTWEAK;
                this.tooltipSortBy = StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.InventoryTweaks");
                break;

            case MOD:
                this.stateIcon = AEStateIcons.SORT_MODE_MOD;
                this.tooltipSortBy = StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.Mod");
                break;

            case NAME:
                this.stateIcon = AEStateIcons.SORT_MODE_ALPHABETIC;
                this.tooltipSortBy = StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.ItemName");
                break;
        }
    }
}
