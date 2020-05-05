package sol3675.middleearthindustry.compat.appeng.gui.buttons;

import appeng.api.config.SortDir;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonSortingDirection extends StateButton
{
    public GuiButtonSortingDirection(final int ID, final int xPos, final int yPos, final int width, final int height)
    {
        super(ID, xPos, yPos, width, height, AEStateIcons.SORT_DIR_ASC, 0, 0, AEStateIcons.REGULAR_BUTTON);
    }

    @Override
    public void getToolTip(final List<String> tooltip)
    {
        this.addAboutToTooltip(tooltip, StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.SortOrder"), StatCollector.translateToLocal("gui.tooltips.appliedenergistics2.ToggleSortDirection"));
    }

    public void setSortingDirection(final SortDir direction)
    {
        switch (direction)
        {
            case ASCENDING:
                this.stateIcon = AEStateIcons.SORT_DIR_ASC;
                break;

            case DESCENDING:
                this.stateIcon = AEStateIcons.SORT_DIR_DEC;
                break;
        }
    }
}
