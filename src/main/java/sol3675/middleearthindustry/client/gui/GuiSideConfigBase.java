package sol3675.middleearthindustry.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import sol3675.middleearthindustry.common.gui.ContainerSideConfigBase;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;
import sol3675.middleearthindustry.references.ModInfo;
import sol3675.middleearthindustry.util.ClientUtil;

public abstract class GuiSideConfigBase extends GuiContainer
{
    static final int BACK = 0;
    static final int NONE = 1;
    static final int TOP = 2;
    static final int BOTTOM = 3;
    static final int NORTH = 4;
    static final int SOUTH = 5;
    static final int WEST = 6;
    static final int EAST = 7;

    static EntityPlayer player;
    static World world;
    static int x, y, z;

    public TileEntityMeiMachine tile;
    public GuiSideConfigBase(EntityPlayer player, TileEntityMeiMachine tile, World world, int x, int y, int z)
    {
        super(new ContainerSideConfigBase(tile));
        this.player = player;
        this.tile =tile;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSize = 176;
        this.ySize = 70;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(BACK, guiLeft + 2, guiTop + 2, 10, 20, EnumChatFormatting.GRAY + "<-"));
        this.buttonList.add(new GuiButton(NONE, guiLeft + 80, guiTop + 10, 16, 16, EnumChatFormatting.GRAY + "NONE"));
        this.buttonList.add(new GuiButton(TOP, guiLeft + 80, guiTop + 26, 16, 16, EnumChatFormatting.GRAY + "T"));
        this.buttonList.add(new GuiButton(BOTTOM, guiLeft + 80, guiTop + 58, 16, 16, EnumChatFormatting.GRAY + "B"));
        this.buttonList.add(new GuiButton(NORTH, guiLeft + 54, guiTop + 42, 16, 16, EnumChatFormatting.GRAY + "N"));
        this.buttonList.add(new GuiButton(SOUTH, guiLeft + 80, guiTop + 42, 16, 16, EnumChatFormatting.GRAY + "S"));
        this.buttonList.add(new GuiButton(WEST, guiLeft + 96, guiTop + 42, 16, 16, EnumChatFormatting.GRAY + "W"));
        this.buttonList.add(new GuiButton(EAST, guiLeft + 112, guiTop + 42, 16, 16, EnumChatFormatting.GRAY + "E"));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.bindTexture(ModInfo.TEXTUREPREFIX + "textures/gui/elements/simplesideconfig.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

}
