package sol3675.middleearthindustry.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import sol3675.middleearthindustry.MiddleEarthIndustry;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;
import sol3675.middleearthindustry.network.MessageTileSync;
import sol3675.middleearthindustry.proxy.CommonProxy;
import sol3675.middleearthindustry.references.Constant;

public class GuiSideConfigItemOutput extends GuiSideConfigBase
{
    public GuiSideConfigItemOutput(EntityPlayer player, TileEntityMeiMachine tile, World world, int x, int y, int z)
    {
        super(player, tile, world, x, y, z);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {

        NBTTagCompound tag = new NBTTagCompound();
        switch (button.id)
        {
            case BACK:
                if(tile instanceof TileEntityAutoCraftingTable)
                {
                    player.openGui(MiddleEarthIndustry.instance, Constant.GUI_AUTO_CRAFTING_TABLE, world, x, y, z);
                }
                return;
            case NONE:
                tag.setInteger("outputSlot", 6);
                break;
            case TOP:
                tag.setInteger("outputSlot", 0);
                break;
            case BOTTOM:
                tag.setInteger("outputSlot", 1);
                break;
            case NORTH:
                tag.setInteger("outputSlot", 2);
                break;
            case SOUTH:
                tag.setInteger("outputSlot", 3);
                break;
            case WEST:
                tag.setInteger("outputSlot", 4);
                break;
            case EAST:
                tag.setInteger("outputSlot", 5);
                break;
        }
        CommonProxy.packetHandler.sendToServer(new MessageTileSync(tile, tag));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int foo)
    {
        this.fontRendererObj.drawString(StatCollector.translateToLocal("setAutoOutputSide"), 20, 3, 0);
    }
}
