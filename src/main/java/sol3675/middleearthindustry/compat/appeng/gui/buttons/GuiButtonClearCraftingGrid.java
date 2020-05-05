package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import appeng.core.localization.ButtonToolTips;
import net.minecraft.util.EnumChatFormatting;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

public class GuiButtonClearCraftingGrid extends StateButton
{
    private static final AEStateIcons CLEAR_ICON = AEStateIcons.CLEAR_GRID;
    private final boolean showTooltip;

    public GuiButtonClearCraftingGrid(final int ID, final int xPos, final int yPos, final int width, final int height, final boolean showTooltip)
    {
        super(ID, xPos, yPos, width, height, GuiButtonClearCraftingGrid.CLEAR_ICON, 0, 0, AEStateIcons.REGULAR_BUTTON);
        this.showTooltip = showTooltip;
    }

    @Override
    public void getToolTip(List<String> tooltip)
    {
        if(this.showTooltip)
        {
            this.addAboutToTooltip(tooltip, ButtonToolTips.Stash.getLocal(), EnumChatFormatting.GRAY.toString() + ButtonToolTips.StashDesc.getLocal());
        }
    }
}
