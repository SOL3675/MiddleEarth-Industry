package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.api.config.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.client.gui.widgets.ISortSource;
import appeng.client.me.ItemRepo;
import appeng.client.render.AppEngRenderItem;
import appeng.core.AEConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sol3675.middleearthindustry.compat.appeng.container.ContainerCraftingTermMei;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonSearchMode;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonSortingDirection;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonSortingMode;
import sol3675.middleearthindustry.compat.appeng.gui.buttons.GuiButtonViewType;
import sol3675.middleearthindustry.compat.appeng.gui.widget.WidgetAEItem;
import sol3675.middleearthindustry.compat.appeng.network.PacketServerCraftingTermMei;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiTerm;
import sol3675.middleearthindustry.compat.appeng.textures.AEStateIcons;
import sol3675.middleearthindustry.compat.appeng.textures.GuiTextureManager;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.util.ClientUtil;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCraftingTermMei extends GuiScrollBase implements ISortSource
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
        this.repo = new ItemRepo(this.scrollbar, this);
        this.terminalStyle = (TerminalStyle) AEConfig.instance.getConfigManager().getSetting(Settings.TERMINAL_STYLE);
    }

    private boolean clickHandler_RegionDeposit(final int mouseX, final int mouseY)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            Slot slotClicked = this.getSlotAtMousePosition(mouseX, mouseY);
            if(slotClicked != null)
            {
                PacketServerCraftingTermMei.sendDepositRegion(this.player, slotClicked.slotNumber);
                return true;
            }
        }
        return false;
    }

    private boolean clickHandler_SearchBox(final int mouseX, final int mouseY, final int mouseButton)
    {
        if((mouseButton == Constant.MOUSE_BUTTON_RIGHT) &&
                ClientUtil.isInGuiRegion(6, 98, 10, 65, mouseX, mouseY, this.guiLeft, this.guiTop))
        {
            this.searchFiled.setText("");
            this.repo.setSearchString("");
            this.viewNeedsUpdate = true;
            this.searchFiled.mouseClicked(mouseX - this.guiLeft, mouseY - this.guiTop, mouseButton);
            return true;
        }
        return false;
    }

    private boolean clickHandler_Widgets(final int mouseX, final int mouseY, final int mouseButton)
    {
        if(ClientUtil.isInGuiRegion(17, 7, this.numberOfWidgetRows * 18, 161, mouseX, mouseY, this.guiLeft, this.guiTop))
        {
            boolean doExtract = (this.player.inventory.getItemStack() == null);
            doExtract |= (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && (mouseButton == Constant.MOUSE_BUTTON_RIGHT));
            if(doExtract)
            {
                this.sendItemWidgetClicked(mouseX, mouseY, mouseButton);
            }
            else
            {
                PacketServerCraftingTermMei.sendDeposit(this.player, mouseButton);
            }
            return true;
        }
        return false;
    }

    private void doMEWheelAction(final int deltaZ, final int mouseX, final int mouseY)
    {
        if(ClientUtil.isInGuiRegion(17, 7, this.numberOfWidgetRows * 18, 161, mouseX, mouseY, this.guiLeft, this.guiTop))
        {
            if(deltaZ > 0)
            {
                if(this.player.inventory.getItemStack() != null)
                {
                    PacketServerCraftingTermMei.sendDeposit(this.player, Constant.MOUSE_WHEEL);
                }
            }
            else
            {
                this.sendItemWidgetClicked(mouseX, mouseY, Constant.MOUSE_WHEEL);
            }
        }
    }

    private WidgetAEItem drawItemWidgets(final int mouseX, final int mouseY)
    {
        boolean hasNoOverlay = true;
        WidgetAEItem widgetUnderMouse = null;
        for(int index = 0; index < this.widgetCount; index++)
        {
            WidgetAEItem currentWidget = this.itemWidgets.get(index);
            currentWidget.drawWidget();
            if(hasNoOverlay && currentWidget.isMouseOverWidget(mouseX, mouseY))
            {
                currentWidget.drawMouseHoverUnderlay();
                hasNoOverlay = false;
                widgetUnderMouse = currentWidget;
            }
        }
        return widgetUnderMouse;
    }

    private void sendItemWidgetClicked(final int mouseX, final int mouseY, final int mouseButton)
    {
        for(int index = 0; index < this.widgetCount; index++)
        {
            WidgetAEItem currentWidget = this.itemWidgets.get(index);
            if(currentWidget.isMouseOverWidget(mouseX, mouseY))
            {
                IAEItemStack widgetStack = currentWidget.getItemStack();
                if(widgetStack != null)
                {
                    if((widgetStack.getStackSize() == 0) || (mouseButton == Constant.MOUSE_BUTTON_MIDDLE))
                    {
                        if((widgetStack.isCraftable()) && (mouseButton != Constant.MOUSE_BUTTON_RIGHT))
                        {
                            PacketServerCraftingTermMei.sendAutoCraft(this.player, widgetStack);
                            return;
                        }
                    }
                    PacketServerCraftingTermMei.sendExtract(this.player, widgetStack, mouseButton, GuiScreen.isShiftKeyDown());
                }
                return;
            }
        }
    }

    private void setupTerminalStyle()
    {
        int extraRows = 0;
        if(this.terminalStyle == TerminalStyle.TALL)
        {
            extraRows = Math.max(0, ((this.height - 243) / 18) - 3);
        }
        this.ySize = 243 + (extraRows * 18);
        this.guiTop = (this.height - this.ySize) / 2;
        this.numberOfWidgetRows = 3 + extraRows;
        this.widgetCount = this.numberOfWidgetRows * 9;
        this.itemWidgets.clear();

        for(int row = 0; row < this.numberOfWidgetRows; row++)
        {
            for(int column = 0; column < 9; column++)
            {
                int index = (row * 9) + column;
                int posX = 7 + (column * 18);
                int posY = 17 + (row * 18);

                this.itemWidgets.add(new WidgetAEItem(this, posX, posY, this.aeItemRenderer));
            }
        }
        this.updateScrollbarRange();
        this.setScrollbarHeight(52 + (extraRows * 18));
        int prevYOffset = this.lowerTerminalYOffset;
        this.lowerTerminalYOffset = (extraRows * 18);
        if(prevYOffset != this.lowerTerminalYOffset)
        {
            ((ContainerCraftingTermMei)this.inventorySlots).changeSlotsYOffset(this.lowerTerminalYOffset - prevYOffset);
        }
        this.cachesItemTooltip.clear();
    }

    private void updateMEWidget()
    {
        int repoIndex = 0;
        for(int index = 0; index < this.widgetCount; index++)
        {
            IAEItemStack itemStack = this.repo.getReferenceItem(repoIndex++);
            if(itemStack != null)
            {
                this.itemWidgets.get(index).setItemStack(itemStack);
            }
            else
            {
                this.itemWidgets.get(index).setItemStack(null);
            }
        }
    }

    private void updateScrollbarRange()
    {
        int totalNumberOfRows = (int)Math.ceil(this.repo.size() / (double)9);
        int max = Math.max(0, totalNumberOfRows - this.numberOfWidgetRows);
        this.scrollbar.setRange(0, max, 2);
    }

    private void updateSorting()
    {
        this.buttonSortingDirection.setSortingDirection(this.sortingDirection);
        this.buttonSortingMode.setSortMode(this.sortingOrder);
        this.buttonViewType.setViewMode(this.viewMode);
        this.viewNeedsUpdate = true;
    }

    private void updateView()
    {
        this.viewNeedsUpdate = false;
        this.repo.updateView();
        this.updateScrollbarRange();
        this.updateMEWidget();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float alpha, final int mouseX, final int mouseY)
    {
        if(this.viewNeedsUpdate)
        {
            this.updateView();
        }
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        this.mc.renderEngine.bindTexture(GuiTextureManager.CRAFTING_TERMINAL_MEI.getTexture());
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 195, 35);
        for(int i = 0; i < (this.numberOfWidgetRows - 3); i++)
        {
            int yPos = this.guiTop + 35 + (i * 18);
            this.drawTexturedModalRect(this.guiLeft, yPos, 0, 35, 195, 18);
        }
        this.drawTexturedModalRect(this.guiLeft, this.guiTop + 35 + this.lowerTerminalYOffset, 0, 18 + 17, 195, 190 + 18);
        this.drawTexturedModalRect(this.guiLeft + 195, this.guiTop, 195, 0, 35, 104);
        Minecraft.getMinecraft().renderEngine.bindTexture(AEStateIcons.AE_STATES_TEXTURE);

        int u = AEStateIcons.VIEW_CELL_BACKGROUND.getU(), v = AEStateIcons.VIEW_CELL_BACKGROUND.getV();
        int h = AEStateIcons.VIEW_CELL_BACKGROUND.getHeight(), w = AEStateIcons.VIEW_CELL_BACKGROUND.getWidth();
        int x = this.guiLeft + 206, y = this.guiTop + 8;

        for(int row = 0; row < 5; row++)
        {
            this.drawTexturedModalRect(x, y + (row * 18), u, v, w, h);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        //TODO
    }

    @Override
    protected ScrollbarParams getScrollbarParameters()
    {
        return new ScrollbarParams(175, 18, 52 + ((3 - this.numberOfWidgetRows) * 18));
    }

    @Override
    protected void keyTyped(final char key, final int keyID)
    {
        //TODO
        super.keyTyped(key, keyID);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
    {
        //TODO
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void onButtonClicked(final GuiButton button, final int mouseButton)
    {
        //TODO
        super.onButtonClicked(button, mouseButton);
    }

    @Override
    protected void onMouseWheel(final int deltaZ, final int mouseX, final int mouseY)
    {
        //TODO
    }

    @Override
    protected void onScrollbarMoved()
    {
        //TODO
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float mouseButton) {
        super.drawScreen(mouseX, mouseY, mouseButton);
        //TODO
    }

    @Override
    public Enum getSortBy()
    {
        return this.sortingOrder;
    }

    @Override
    public Enum getSortDir()
    {
        return this.sortingDirection;
    }

    @Override
    public Enum getSortDisplay()
    {
        return this.viewMode;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        //TODO
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public void onReceiveChange(final IAEItemStack change)
    {
        //TODO
    }

    public void onReceiveFulllist(final IItemList<IAEItemStack> itemList)
    {
        this.repo.clear();
        for(IAEItemStack stack : itemList)
        {
            this.onReceiveChange(stack);
        }
    }

    public void onReceiveSorting(final SortOrder order, final SortDir direction, final ViewItems viewMode)
    {
        this.sortingDirection = direction;
        this.sortingOrder = order;
        this.viewMode = viewMode;
        this.updateSorting();
    }

    public void onViewCellsChanged(final ItemStack[] viewCells)
    {
        this.repo.setViewCell(viewCells);
        this.viewNeedsUpdate = true;
    }
}
