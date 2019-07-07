package sol3675.middleearthindustry.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lotr.common.LOTRMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.references.ModInfo;
import sol3675.middleearthindustry.util.Util;

import java.util.List;
import java.util.Random;

public class BlockStoneGen extends BlockMeiBase
{
    public static final int BASIC = 0;
    public static final int MORDOR = 1;
    public static final int GONDOR = 2;
    public static final int ROHAN = 3;
    public static final int SARLLUIN = 4;
    public static final int SARNGARAN = 5;
    public static final int CHALK = 6;

    public BlockStoneGen()
    {
        super("stoneGen", Material.rock, 4, ItemBlockStoneGen.class, "basic", "mordor", "gondor", "rohan", "sarlluin", "sarngaran", "chalk");
        setHardness(2.5F);
        setResistance(10.0F);
        for(int meta = 0; meta < subNames.length; ++meta)
        {
            this.setMetaLightOpacity(meta, 255);
        }

    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if(!world.isRemote)
        {
            TileEntity inventory = world.getTileEntity(x, y+1, z);
            if(inventory instanceof ISidedInventory && (((ISidedInventory)inventory).getAccessibleSlotsFromSide(0).length > 0 || (inventory instanceof IInventory && ((IInventory)inventory).getSizeInventory() > 0)))
            {
                switch (world.getBlockMetadata(x, y, z))
                {
                    case BASIC:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(Blocks.stone, 1), 1);
                        break;
                    case MORDOR:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(LOTRMod.rock, 1, 0), 1);
                        break;
                    case GONDOR:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(LOTRMod.rock, 1, 1), 1);
                        break;
                    case ROHAN:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(LOTRMod.rock, 1, 2), 1);
                        break;
                    case SARLLUIN:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(LOTRMod.rock, 1, 3), 1);
                        break;
                    case SARNGARAN:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(LOTRMod.rock, 1, 4), 1);
                        break;
                    case CHALK:
                        Util.insertStackIntoInventory((IInventory)inventory, new ItemStack(LOTRMod.rock, 1, 5), 1);
                        break;
                }
            }
            world.scheduleBlockUpdate(x, y, z, world.getBlock(x, y, z), 20);
        }
     }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        world.scheduleBlockUpdate(x, y, z, world.getBlock(x, y, z), 20);
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(int i = 0; i < subNames.length; ++i)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        for(int j = 0; j < subNames.length; ++j)
        {
            for(int i = 0; i < this.iconDimensions; ++i)
            {
                icons[j][i] =iconRegister.registerIcon(ModInfo.TEXTUREPREFIX + subNames[j] + Constant.BLOCK_SIDE_4D[i]);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        return super.getIcon(world, x, y, z, side);
    }
}
