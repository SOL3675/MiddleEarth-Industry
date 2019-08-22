package sol3675.middleearthindustry.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileRenderMEI extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        renderDynamic(tile, x, y, z, f);
    }

    public abstract void renderDynamic(TileEntity tile, double x, double y, double z, float f);
    public abstract void renderStatic(TileEntity tile, double x, double y, double z, float f);
}
