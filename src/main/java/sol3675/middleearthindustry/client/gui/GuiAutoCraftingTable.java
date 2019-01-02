package sol3675.middleearthindustry.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import sol3675.middleearthindustry.common.gui.ContainerAutoCraft;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.network.MessageTileSync;
import sol3675.middleearthindustry.proxy.CommonProxy;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.references.ModInfo;
import sol3675.middleearthindustry.util.ClientUtil;

import java.util.ArrayList;

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
    protected void actionPerformed(GuiButton button)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("buttonID", button.id);
        CommonProxy.packetHandler.sendToServer(new MessageTileSync(tile, tag));
    }

    @Override
    public void drawScreen(int mx, int my, float partial)
    {
        super.drawScreen(mx, my, partial);
        ArrayList<String> tooltip = new ArrayList<String>();
        if(MeiCfg.AutocraftRequireRF && mx >= guiLeft + 8  && mx <= guiLeft + 20 && mx >= guiTop + 16 && mx <= guiTop + 76)
        {
            tooltip.add(tile.energyStorage.getEnergyStored() + "/" + tile.energyStorage.getMaxEnergyStored() + "RF");
        }

        if(tile.inventory[9] == null && tile.pattern.inventory[9] != null)
        {
            if(mx >= guiLeft + 100  && mx <= guiLeft + 115 && mx >= guiTop + 36 && mx <= guiTop + 51)
            {
                tooltip.add(tile.pattern.inventory[9].getDisplayName());
                tile.pattern.inventory[9].getItem().addInformation(tile.pattern.inventory[9], ClientUtil.mc().thePlayer, tooltip, false);
                for(int j = 0; j < tooltip.size(); ++j)
                {
                    tooltip.set(j, (j == 0 ? tile.pattern.inventory[9].getRarity().rarityColor : EnumChatFormatting.GRAY) + tooltip.get(j));
                }
            }
        }

        if(!tooltip.isEmpty())
        {
            ClientUtil.drawHoveringText(tooltip, mx, my, fontRendererObj, xSize, -1);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.bindTexture(ModInfo.TEXTUREPREFIX + "textures/gui/autocraftingtable.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if(MeiCfg.AutocraftRequireRF)
        {
            ClientUtil.drawEnergyBar(tile.energyStorage, guiLeft + 8, guiTop + 16);
        }

        if(tile.inventory[9] == null && tile.pattern.inventory[9] != null)
        {
            ItemStack stack = tile.pattern.inventory[9];
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.disableStandardItemLighting();
            this.zLevel = 200.0F;
            itemRender.zLevel = 200.0F;
            FontRenderer font = null;
            if(stack != null)
            {
                font = stack.getItem().getFontRenderer(stack);
            }
            if(font == null)
            {
                font = fontRendererObj;
            }
            itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), stack, guiLeft + 100, guiTop + 36);
            itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), stack, guiLeft + 100, guiTop + 36, EnumChatFormatting.GRAY.toString()+stack.stackSize);
            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            ClientUtil.drawColouredRect(guiLeft + 100, guiTop + 36, 16, 16, 0x77444444);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
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
