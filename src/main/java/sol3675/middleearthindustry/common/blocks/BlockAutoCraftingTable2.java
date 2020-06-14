package sol3675.middleearthindustry.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lotr.common.fac.LOTRAlignmentValues;
import lotr.common.LOTRLevelData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.MiddleEarthIndustry;
import sol3675.middleearthindustry.common.MeiContents;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.references.ModInfo;
import sol3675.middleearthindustry.util.Util;

import java.util.List;

public class BlockAutoCraftingTable2 extends BlockContainerMeiBase
{
    public static final int META_HALFTROLL = 0;
    public static final int META_DOLAMROTH = 1;
    public static final int META_MOREDAIN = 2;
    public static final int META_TAUREDAIN = 3;
    public static final int META_DALE = 4;
    public static final int META_DORWINION = 5;
    public static final int META_HOBBIT = 6;
    public static final int META_RHUN = 7;
    public static final int META_RIVENDELL = 8;
    public static final int META_UMBAR = 9;
    public static final int META_GULF = 10;
    public static final int META_BREE = 11;

    public BlockAutoCraftingTable2()
    {
        super("autoCraftingTable2", Material.rock, 4, ItemBlockAutoCraftingTable2.class, "halfTrollTable", "dolAmrothTable", "moredainTable", "tauredainTable", "daleTable", "dorwinionTable", "hobbitTable", "rhunTable", "rivendellTable", "umbarTable", "gulfTable", "breeTable");
        setHardness(2.5F);
        setResistance(10.0F);
        for(int meta = 0; meta < subNames.length; ++meta)
        {
            this.setMetaLightOpacity(meta, 255);
        }
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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (meta)
        {
            case META_HALFTROLL:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.HalfTroll);
            case META_DOLAMROTH:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.DolAmroth);
            case META_MOREDAIN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Moredain);
            case META_TAUREDAIN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Tauredain);
            case META_DALE:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Dale);
            case META_DORWINION:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Dorwinion);
            case META_HOBBIT:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Hobbit);
            case META_RHUN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Rhun);
            case META_RIVENDELL:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Rivendell);
            case META_UMBAR:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Umbar);
            case META_GULF:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Gulf);
            case META_BREE:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Bree);
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntityAutoCraftingTable tile = (TileEntityAutoCraftingTable)world.getTileEntity(x, y, z);
        if(player.getCurrentEquippedItem() != null)
        {
            if(player.getCurrentEquippedItem().getItem() == MeiContents.itemMultiblockBuilder)
            {
                if(player.isSneaking())
                {
                    side = ForgeDirection.OPPOSITES[side];
                }
                if(!world.isRemote)
                {
                    tile.setSideItemOutput(side);
                    tile.markDirty();
                    world.markBlockForUpdate(x, y, z);
                    world.addBlockEvent(x, y, z, tile.getBlockType(), 0, 0);
                }
                return true;
            }
        }

        if(!player.isSneaking())
        {
            if(LOTRLevelData.getData(player).getAlignment(Constant.getFaction(tile.tableFaction)) > 0 && !world.isRemote)
            {
                player.openGui(MiddleEarthIndustry.instance, Constant.GUI_AUTO_CRAFTING_TABLE, world, x, y, z);
                return true;
            }
            else if(LOTRLevelData.getData(player).getAlignment(Constant.getFaction(tile.tableFaction)) <= 0)
            {
                LOTRAlignmentValues.notifyAlignmentNotHighEnough(player, 1, Constant.getFaction(tile.tableFaction));
            }
            return true;
        }

        return false;
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
        //if side config save, should change this
        return super.getIcon(world, x, y, z, side);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntityAutoCraftingTable tile = (TileEntityAutoCraftingTable)world.getTileEntity(x, y, z);
        if(tile != null)
        {
            Util.dropContainerItems(tile, world, x, y, z);
            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
}
