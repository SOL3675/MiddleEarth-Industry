package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.api.config.*;
import appeng.client.gui.widgets.ISortSource;
import appeng.client.me.ItemRepo;
import appeng.client.render.AppEngRenderItem;
import appeng.core.AEConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import sol3675.middleearthindustry.compat.appeng.container.ContainerCraftingTermMei;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonSearchMode;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonSortingDirection;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonSortingMode;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonViewType;
import sol3675.middleearthindustry.compat.appeng.gui.widget.WidgetAEItem;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiTerm;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCraftingTermMei extends GuiBaseAE implements ISortSource
{
    private AppEngRenderItem aeItemRenderer = new AppEngRenderItem();
    private String guiTitle;
    private int widgetCount = 3 * 9;
    private List<WidgetAEItem> itemWidgets = new ArrayList<WidgetAEItem>();
    private EntityPlayer player;
    private GuiTextField searchFiled;
    private final ItemRepo repo;
    private SortOrder sortingOrder = SortOrder.NAME;
    private SortDir sortingDirection = SortDir.ASCENDING;
    private ViewItems viewMode = ViewItems.ALL;
    private TerminalStyle terminalStyle = TerminalStyle.SMALL;
    private int lowerTerminalYOffset = 0;
    private int previousMouseX = 0;
    private int previousMouseY = 0;
    private WidgetAEItem previousWidgetUnderMouse = null;
    private long lastTooltipUpdateTime = 0;
    private int numberOfWidgetRows = 3;
    private GuiButtonSortingMode buttonSortingMode;
    private GuiButtonSortingDirection buttonSortingDirection;
    private GuiButtonViewType buttonViewType;
    private GuiButtonSearchMode buttonSearchMode;
    private boolean viewNeedsUpdate = true;

    private ArrayList<String> cachesItemTooltip = new ArrayList<String>();

    public GuiCraftingTermMei(final PartMeiTerm part, final EntityPlayer player)
    {
        super(new ContainerCraftingTermMei(part, player));
        this.player = player;
        this.xSize = 230;
        this.ySize = 243;
        this.guiTitle = ""; //TODO
        this.repo = new ItemRepo(this.scrollBar, this);
        this.terminalStyle = (TerminalStyle) AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE);
    }
}
