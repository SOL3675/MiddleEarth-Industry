package sol3675.middleearthindustry.common.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sol3675.middleearthindustry.client.gui.GuiAutoCraftingTable;
import sol3675.middleearthindustry.client.gui.GuiSideConfigItemInput;
import sol3675.middleearthindustry.client.gui.GuiSideConfigItemOutput;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;
import sol3675.middleearthindustry.references.Constant;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(ID == Constant.GUI_AUTO_CRAFTING_TABLE)
        {
            return new ContainerAutoCraft(player.inventory, (TileEntityAutoCraftingTable)tile);
        }
        else if(ID == Constant.SIDE_CONFIG_ITEM_INPUT)
        {
            return new ContainerSideConfigBase((TileEntityMeiMachine) tile);
        }
        else if(ID == Constant.SIDE_CONFIG_ITEM_OUTPUT)
        {
            return new ContainerSideConfigBase((TileEntityMeiMachine) tile);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(ID == Constant.GUI_AUTO_CRAFTING_TABLE)
        {
            return new GuiAutoCraftingTable(player, (TileEntityAutoCraftingTable)tile, world, x, y, z);
        }
        else if(ID == Constant.SIDE_CONFIG_ITEM_INPUT)
        {
            return new GuiSideConfigItemInput(player, (TileEntityMeiMachine)tile, world, x, y, z);
        }
        else if(ID == Constant.SIDE_CONFIG_ITEM_OUTPUT)
        {
            return new GuiSideConfigItemOutput(player, (TileEntityMeiMachine)tile, world, x, y, z);
        }
        return null;
    }
}
