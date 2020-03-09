package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import appeng.api.config.SearchBoxMode;
import appeng.core.localization.ButtonToolTips;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.EnumChatFormatting;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonSearchMode extends StateButton
{
    private String tooltipMode = "";

    public GuiButtonSearchMode(final int ID, final int xPos, final int yPos, final int buttonWidth, final int buttonHeight, final SearchBoxMode currentMode)
    {
        super(ID, xPos, yPos, buttonWidth, buttonHeight, null, 0, 0, AEStateIcons.REGULAR_BUTTON);

    }

    @Override
    public void getToolTip(final List<String> tooltip)
    {
        this.addAboutToTooltip(tooltip, ButtonToolTips.SearchMode.getLocal(), EnumChatFormatting.GRAY + this.tooltipMode);
    }

    public void setSearchMode(final SearchBoxMode mode)
    {
        switch (mode)
        {
            case AUTOSEARCH:
                this.tooltipMode = ButtonToolTips.SearchMode_Auto.getLocal();
                this.stateIcon = AEStateIcons.SEARCH_MODE_MANUAL;
                break;

            case MANUAL_SEARCH:
                this.tooltipMode = ButtonToolTips.SearchMode_Standard.getLocal();
                this.stateIcon = AEStateIcons.SEARCH_MODE_MANUAL;
                break;

            case NEI_AUTOSEARCH:
                this.tooltipMode = ButtonToolTips.SearchMode_NEIAuto.getLocal();
                this.stateIcon = AEStateIcons.SEARCH_MODE_AUTO;
                break;

            case NEI_MANUAL_SEARCH:
                this.tooltipMode = ButtonToolTips.SearchMode_NEIStandard.getLocal();
                this.stateIcon = AEStateIcons.SEARCH_MODE_NEI_MANUAL;
                break;
        }
    }
}
