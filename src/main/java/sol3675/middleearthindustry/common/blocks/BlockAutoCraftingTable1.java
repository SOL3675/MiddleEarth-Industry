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

public class BlockAutoCraftingTable1 extends BlockContainerMeiBase
{
    public static final int META_GENERAL = 0;
    public static final int META_MORGUL = 1;
    public static final int META_ELVEN = 2;
    public static final int META_DWARVEN = 3;
    public static final int META_URUK = 4;
    public static final int META_WOODELVEN = 5;
    public static final int META_GONDORIAN = 6;
    public static final int META_ROHIRRIC = 7;
    public static final int META_DUNLENDING = 8;
    public static final int META_ANGMAR = 9;
    public static final int META_NEARHARAD = 10;
    public static final int META_HIGHELVEN = 11;
    public static final int META_BLUEDWARVEN = 12;
    public static final int META_RANGER = 13;
    public static final int META_DOLGULDUR = 14;
    public static final int META_GUNDABAD = 15;

    public BlockAutoCraftingTable1()
    {
        super("autoCraftingTable1", Material.rock, 4, ItemBlockAutoCraftingTable1.class, "generalTable", "morgulTable", "elvenTable", "dwarvenTable", "urukTable", "woodElvenTable", "gondorianTable", "rohirricTable", "dunlendingTable", "angmarTable", "nearHaradTable", "highElvenTable", "blueDwarvenTable", "rangerTable", "dolGuldurTable", "gundabadTable");
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
            case META_GENERAL:
                return new TileEntityAutoCraftingTable();
            case META_MORGUL:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Morgul);
            case META_ELVEN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Elven);
            case META_DWARVEN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Dwarven);
            case META_URUK:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Uruk);
            case META_WOODELVEN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.WoodElven);
            case META_GONDORIAN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Gondorian);
            case META_ROHIRRIC:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Rohirric);
            case META_DUNLENDING:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Dunlending);
            case META_ANGMAR:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Angmar);
            case META_NEARHARAD:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.NearHarad);
            case META_HIGHELVEN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.HighElven);
            case META_BLUEDWARVEN:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.BlueDwarven);
            case META_RANGER:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Ranger);
            case META_DOLGULDUR:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.DolGuldur);
            case META_GUNDABAD:
                return new TileEntityAutoCraftingTable(Constant.TableFaction.Gundabad);

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
            if(tile.tableFaction == null  && !world.isRemote)
            {
                player.openGui(MiddleEarthIndustry.instance, Constant.GUI_AUTO_CRAFTING_TABLE, world, x, y, z);
                return true;
            }
            else if(tile.tableFaction != null && LOTRLevelData.getData(player).getAlignment(Constant.getFaction(tile.tableFaction)) > 0  && !world.isRemote)
            {
                player.openGui(MiddleEarthIndustry.instance, Constant.GUI_AUTO_CRAFTING_TABLE, world, x, y, z);
                return true;
            }
            else if(tile.tableFaction != null)
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
