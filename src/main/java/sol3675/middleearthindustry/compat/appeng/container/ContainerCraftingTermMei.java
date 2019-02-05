package sol3675.middleearthindustry.compat.appeng.container;

import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.ContainerMEMonitorable;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCraftingTermMei extends ContainerMEMonitorable
{
    public ContainerCraftingTermMei(final InventoryPlayer inventoryPlayer, final ITerminalHost monitorable)
    {
        super(inventoryPlayer, monitorable, false);
    }
}
