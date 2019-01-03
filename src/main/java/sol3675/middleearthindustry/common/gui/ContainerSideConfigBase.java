package sol3675.middleearthindustry.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;

public abstract class ContainerSideConfigBase extends Container
{
    TileEntityMeiMachine tile;

    public ContainerSideConfigBase(TileEntityMeiMachine tile)
    {
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
