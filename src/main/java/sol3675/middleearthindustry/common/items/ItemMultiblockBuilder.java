package sol3675.middleearthindustry.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sol3675.middleearthindustry.api.IMultiblock;
import sol3675.middleearthindustry.api.MultiblockHandler;

public class ItemMultiblockBuilder extends ItemMeiBase{

    public ItemMultiblockBuilder()
    {
        super("multiblock_builder", 1, "builder");
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            String[] tagMultiblocks = null;
            if(itemstack.hasTagCompound() ? itemstack.getTagCompound().hasKey("multiblockInterdiction") : false)
            {
                NBTTagList list = itemstack.getTagCompound().getTagList("multiblockInterdiction", 8);
                tagMultiblocks = new String[list.tagCount()];
                for(int i = 0; i < tagMultiblocks.length; ++i)
                {
                    tagMultiblocks[i] = list.getStringTagAt(i);
                }
            }
            for(IMultiblock multiblock : MultiblockHandler.getMultiblocks())
            {
                if(multiblock.isTrigger(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)))
                {
                    boolean isMultiblock = true;
                    if(tagMultiblocks != null)
                    {
                        for(String string : tagMultiblocks)
                        {
                            if(multiblock.getMultiblockName().equalsIgnoreCase(string))
                            {
                                isMultiblock = false;
                                break;
                            }
                        }
                    }
                    if(isMultiblock && multiblock.createMultiblock(world, x, y, z, side, player))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }


}
