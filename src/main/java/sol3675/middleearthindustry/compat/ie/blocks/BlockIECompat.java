package sol3675.middleearthindustry.compat.ie.blocks;

import blusunrize.immersiveengineering.common.util.Lib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import sol3675.middleearthindustry.common.blocks.BlockContainerMeiBase;
import sol3675.middleearthindustry.compat.ie.tileentities.TileEntityCompressedThermoelectricGen;
import sol3675.middleearthindustry.compat.ie.tileentities.TileEntityDoubleCompressedThermoelectricGen;
import sol3675.middleearthindustry.references.ModInfo;

import java.util.List;

public class BlockIECompat extends BlockContainerMeiBase
{
    public static final int META_compressedThermoelectricGen = 0;
    public static final int META_doubleCompressedThermoelectricGen = 1;

    private String[] NAME =
            {
                    "compressedThermoelectricGen",
                    "doubleCompressedThermoelectricGen"
            };

    public BlockIECompat()
    {
        super("blockIECompat", Material.iron, 4, ItemBlockIECompat.class, "compressedThermoelectricGen", "doubleCompressedThermoelectricGen");
        setHardness(3.0F);
        setResistance(15.0F);
        this.setMetaLightOpacity(META_compressedThermoelectricGen, 255);
        this.setMetaLightOpacity(META_doubleCompressedThermoelectricGen, 255);
    }

    @Override
    public boolean isToolEffective(String type, int metadata)
    {
        if(Lib.TOOL_HAMMER.equals(type))
        {
            return true;
        }
        return super.isToolEffective(type, metadata);
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
            case META_compressedThermoelectricGen:
                return new TileEntityCompressedThermoelectricGen();
            case META_doubleCompressedThermoelectricGen:
                return new TileEntityDoubleCompressedThermoelectricGen();
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        icons[0][0] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[0] + "_bottom");
        icons[0][1] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[0] + "_top");
        icons[0][2] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[0] + "_side");
        icons[0][3] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[0] + "_side");

        icons[1][0] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[1] + "_bottom");
        icons[1][1] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[1] + "_top");
        icons[1][2] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[1] + "_side");
        icons[1][3] = ir.registerIcon(ModInfo.TEXTUREPREFIX + NAME[1] + "_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return super.getIcon(side, meta);
    }
}
