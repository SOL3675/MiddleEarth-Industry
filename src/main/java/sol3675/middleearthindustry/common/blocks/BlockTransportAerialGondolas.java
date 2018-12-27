package sol3675.middleearthindustry.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sol3675.middleearthindustry.common.tileentities.TileEntityTransportAerialGondolas;

public class BlockTransportAerialGondolas extends BlockContainerMeiBase
{

    public static final int META_cable = 0;
    public static final int META_cableDiagonal = 1;
    public static final int META_io = 2;
    public static final int META_corner = 3;
    public static final int META_junction = 4;

    public BlockTransportAerialGondolas()
    {
        super("aerialGondola", Material.circuits, 4, ItemBlockTransportAerialGondolas.class, "cable", "cableDiagonal", "io", "corner", "junction");
        setHardness(1.0F);
        setResistance(10.0F);
        this.setMetaLightOpacity(META_cable, 255);
        this.setMetaLightOpacity(META_cableDiagonal, 255);
        this.setMetaLightOpacity(META_io, 255);
        this.setMetaLightOpacity(META_corner, 255);
        this.setMetaLightOpacity(META_junction, 255);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        //switch(meta)
        return new TileEntityTransportAerialGondolas();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isBlockNormalCube()
    {
        return false;
    }

    @Override
    public boolean isNormalCube()
    {
        return false;
    }



}
