package sol3675.middleearthindustry.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import sol3675.middleearthindustry.common.gui.ContainerSideConfigBase;
import sol3675.middleearthindustry.common.tileentities.TileEntityMeiMachine;

public abstract class GuiSideConfigBase extends GuiContainer
{
    public TileEntityMeiMachine tile;
    public GuiSideConfigBase(TileEntityMeiMachine tile)
    {
        super(new ContainerSideConfigBase(tile));
        this.tile =tile;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }
}
