package sol3675.middleearthindustry.compat.waila;


import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.references.Constant;

import java.util.List;

public class MeiWailaProvider implements IWailaDataProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerBodyProvider(new MeiWailaProvider(), TileEntityAutoCraftingTable.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        //Block block = iWailaDataAccessor.getBlock();
        TileEntity tile = iWailaDataAccessor.getTileEntity();
        if(tile instanceof TileEntityAutoCraftingTable)
        {
            String tableName = Constant.getUnlocalizedTableName(((TileEntityAutoCraftingTable) tile).tableFaction);
            if(tableName == null)
            {
                tableName = "general";
            }
            list.add(String.format("Faction: %s", StatCollector.translateToLocal(tableName)));
            /*
            if(MeiCfg.AutocraftRequireRF)
            {
                int rf = iWailaDataAccessor.getNBTData().getInteger("Energy");
                list.add(String.format("%d / 1000000 RF", new Object[]{rf}));
            }
             */
        }
        else
        {
            list.add(StatCollector.translateToLocal("hud.msg.empty"));
        }
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, int i, int i1, int i2) {
        return nbtTagCompound;
    }
}
