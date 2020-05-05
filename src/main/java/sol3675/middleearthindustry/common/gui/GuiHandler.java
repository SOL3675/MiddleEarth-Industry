package sol3675.middleearthindustry.common.gui;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.client.gui.GuiAutoCraftingTable;
import sol3675.middleearthindustry.client.gui.GuiSideConfigItemInput;
import sol3675.middleearthindustry.client.gui.GuiSideConfigItemOutput;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;
import sol3675.middleearthindustry.compat.appeng.gui.GuiAEHandler;
import sol3675.middleearthindustry.references.Constant;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        ForgeDirection side = ForgeDirection.getOrientation(ID);
        if(Loader.isModLoaded("appliedenergistics2"))
        {
            if((world != null) && (side != ForgeDirection.UNKNOWN))
            {
                return GuiAEHandler.getPartGuiElement(side, player, world, x, y, z, true);
            }
        }
        TileEntity tile = world.getTileEntity(x, y, z);

        switch (ID)
        {
            case Constant.GUI_AUTO_CRAFTING_TABLE:
                return new ContainerAutoCraft(player.inventory, (TileEntityAutoCraftingTable)tile);

            case Constant.SIDE_CONFIG_ITEM_INPUT:
                return new ContainerSideConfigBase((TileEntityMeiMachine) tile);

            case Constant.SIDE_CONFIG_ITEM_OUTPUT:
                return new ContainerSideConfigBase((TileEntityMeiMachine) tile);

            default:
                if(Loader.isModLoaded("appliedenergistics2"))
                {
                    ID -= ForgeDirection.values().length;
                    return GuiAEHandler.getServerGuiElementAE(ID, player, world, x, y, z);
                }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        ForgeDirection side = ForgeDirection.getOrientation(ID);
        if(Loader.isModLoaded("appliedenergistics2"))
        {
            if((world != null) && (side != ForgeDirection.UNKNOWN))
            {
                GuiAEHandler.getPartGuiElement(side, player, world, x, y, z, false);
            }
        }
        TileEntity tile = world.getTileEntity(x, y, z);

        switch (ID)
        {
            case Constant.GUI_AUTO_CRAFTING_TABLE:
                return new GuiAutoCraftingTable(player, (TileEntityAutoCraftingTable)tile, world, x, y, z);

            case Constant.SIDE_CONFIG_ITEM_INPUT:
                return new GuiSideConfigItemInput(player, (TileEntityMeiMachine)tile, world, x, y, z);

            case Constant.SIDE_CONFIG_ITEM_OUTPUT:
                return new GuiSideConfigItemOutput(player, (TileEntityMeiMachine)tile, world, x, y, z);

            default:
                if(Loader.isModLoaded("appliedenergistics2"))
                {
                    ID -= ForgeDirection.values().length;
                    return GuiAEHandler.getClientGuiElementAE(ID, player, world, x, y, z);
                }
        }

        return null;
    }


}
