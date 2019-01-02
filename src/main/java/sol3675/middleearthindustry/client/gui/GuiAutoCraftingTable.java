package sol3675.middleearthindustry.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import sol3675.middleearthindustry.common.gui.ContainerAutoCraft;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.references.Constant;

public class GuiAutoCraftingTable extends GuiContainer
{
    private static final int SIDE_CONFIG_ITEM = 0;
    private static final int SIDE_CONFIG_ENERGY = 1;
    private static final int RESET_PATTERN = 2;

    private static Constant.TableFaction tableFaction;

    public TileEntityAutoCraftingTable tile;
    public GuiAutoCraftingTable(InventoryPlayer inventoryPlayer, TileEntityAutoCraftingTable tile)
    {
        super(new ContainerAutoCraft(inventoryPlayer, tile));
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 197;
        if(tile.tableFaction != null)
        {
            tableFaction = tile.tableFaction;
        }
        else
        {
            tableFaction = null;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(SIDE_CONFIG_ITEM, guiLeft + 124, guiTop + 5, 16, 16, EnumChatFormatting.GRAY + "\\u2716"));
        this.buttonList.add(new GuiButton(SIDE_CONFIG_ENERGY, guiLeft + 140, guiTop + 5, 16, 16, EnumChatFormatting.GRAY + "\\u2716"));
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_)
    {
        super.actionPerformed(p_146284_1_);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int foo)
    {
        if(tableFaction != null)
        {
            this.fontRendererObj.drawString(StatCollector.translateToLocal("auto_" + Constant.getUnlocalizedTableName(tableFaction)), 8, 6, 0);
        }
        else
        {
            this.fontRendererObj.drawString(StatCollector.translateToLocal("auto_CraftTable"), 8, 6, 0);
        }
    }
}
