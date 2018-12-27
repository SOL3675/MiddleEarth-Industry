package sol3675.middleearthindustry.compat.ie.blocks;

import blusunrize.immersiveengineering.common.util.Lib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import sol3675.middleearthindustry.common.blocks.BlockContainerMeiBase;
import sol3675.middleearthindustry.compat.ie.tileentities.TileEntityCompressedThermoelectricGen;
import sol3675.middleearthindustry.references.ModInfo;

public class BlockIECompat extends BlockContainerMeiBase
{
    public static final int META_compressedThermoelectricGen = 0;
    private String[] NAME = {"compressedThermoelectricGen"};

    public BlockIECompat()
    {
        super("blockIECompat", Material.iron, 4, ItemBlockIECompat.class, "compressedThermoelectricGen");
        setHardness(3.0F);
        setResistance(15.0F);
        this.setMetaLightOpacity(META_compressedThermoelectricGen, 255);
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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (meta)
        {
            case META_compressedThermoelectricGen:
            return new TileEntityCompressedThermoelectricGen();
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
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return super.getIcon(side, meta);
    }
}
