package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import appeng.api.config.TerminalStyle;
import appeng.core.localization.ButtonToolTips;
import net.minecraft.util.EnumChatFormatting;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

public class GuiButtonTerminalStyle extends StateButton
{
    private String tooltipStyle = "";

    public GuiButtonTerminalStyle(final int ID, final int xPos, final int yPos, final int width, final int height, final TerminalStyle currentStyle)
    {
        super(ID, xPos, yPos, width, height, null, 0, 0, AEStateIcons.REGULAR_BUTTON);
        this.setTerminalStyle(currentStyle);
    }

    @Override
    public void getToolTip(List<String> tooltip)
    {
        this.addAboutToTooltip( tooltip, ButtonToolTips.TerminalStyle.getLocal(), EnumChatFormatting.GRAY + this.tooltipStyle );
    }

    public void setTerminalStyle(final TerminalStyle style)
    {
        switch (style)
        {
            case SMALL:
                this.stateIcon = AEStateIcons.TERM_STYLE_SMALL;
                this.tooltipStyle = ButtonToolTips.TerminalStyle_Small.getLocal();
                break;

            case TALL:
                this.stateIcon = AEStateIcons.TERM_STYLE_TALL;
                this.tooltipStyle = ButtonToolTips.TerminalStyle_Tall.getLocal();
                break;

            default:
                break;
        }
    }
}
