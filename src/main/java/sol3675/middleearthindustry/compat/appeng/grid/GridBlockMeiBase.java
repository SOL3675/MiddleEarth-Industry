package sol3675.middleearthindustry.compat.appeng.grid;

import appeng.api.networking.*;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.parts.PartItemStack;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiBase;

import java.util.EnumSet;

public class GridBlockMeiBase implements IGridBlock
{
    protected IGrid grid;
    protected int usedChannels;
    protected PartMeiBase part;

    public GridBlockMeiBase(final PartMeiBase part)
    {
        this.part = part;
    }

    public final IGrid getGrid()
    {
        IGridNode node = (this).part.getGridNode();
        if(node != null)
        {
            return node.getGrid();
        }
        return null;
    }

    public ISecurityGrid getSecurityGrid()
    {
        IGrid grid = this.getGrid();
        if(grid ==null)
        {
            return null;
        }
        return (ISecurityGrid)grid.getCache(ISecurityGrid.class);
    }

    public IEnergyGrid getEnergyGrid()
    {
        IGrid grid = this.getGrid();
        if(grid == null)
        {
            return null;
        }
        return grid.getCache(IEnergyGrid.class);
    }

    public IStorageGrid getStorageGrid()
    {
        IGrid grid = this.getGrid();
        if(grid == null)
        {
            return null;
        }
        return (IStorageGrid)grid.getCache(IStorageGrid.class);
    }

    public IMEMonitor<IAEItemStack> getItemMonitor()
    {
        IStorageGrid storageGrid = this.getStorageGrid();
        if(storageGrid == null)
        {
            return null;
        }
        return storageGrid.getItemInventory();
    }

    @Override
    public EnumSet<ForgeDirection> getConnectableSides()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<GridFlags> getFlags()
    {
        return EnumSet.of(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    public AEColor getGridColor()
    {
        return AEColor.Transparent;
    }

    @Override
    public double getIdlePowerUsage()
    {
        return this.part.getIdlePowerUsage();
    }

    @Override
    public DimensionalCoord getLocation() {
        return this.part.getLocation();
    }

    @Override
    public IGridHost getMachine()
    {
        return this.part;
    }

    @Override
    public ItemStack getMachineRepresentation()
    {
        return this.part.getItemStack(PartItemStack.Network);
    }

    @Override
    public void gridChanged() {}

    @Override
    public boolean isWorldAccessible() {
        return false;
    }

    @Override
    public void onGridNotification(GridNotification gridNotification) {}

    @Override
    public void setNetworkStatus(IGrid iGrid, int i) {}


}
