package sol3675.middleearthindustry.compat.te.items;

import cofh.thermalexpansion.block.machine.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sol3675.middleearthindustry.common.items.ItemMeiBase;

public class UpgradeKit extends ItemMeiBase
{
    public UpgradeKit()
    {
        super("upgradekit", 64, "hardened", "reinforced", "resonant");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof TileMachineBase)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                tile.readFromNBT(nbt);
                byte level = nbt.getByte("Level");

                if((int)level == stack.getItemDamage())
                {
                    nbt.setByte("Level", ++level);
                    tile.writeToNBT(nbt);
                    --stack.stackSize;
                    return true;
                }
            }
        }
        return false;
    }
}
