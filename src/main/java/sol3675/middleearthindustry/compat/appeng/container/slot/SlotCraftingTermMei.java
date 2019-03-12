package sol3675.middleearthindustry.compat.appeng.container.slot;

import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.slot.AppEngCraftingSlot;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import appeng.helpers.InventoryAction;
import appeng.util.Platform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import sol3675.middleearthindustry.references.Constant;

public class SlotCraftingTermMei extends AppEngCraftingSlot
{

    private final Constant.TableFaction faction;

    public SlotCraftingTermMei(final Constant.TableFaction faction, final EntityPlayer player, final BaseActionSource mySrc, final IEnergySource energySrc, final IStorageMonitorable storage, final IInventory cMatrix, final IInventory secondMatrix, final IInventory output, final int x, final int y, final IContainerCraftingPacket ccp)
    {
        super(player, cMatrix, output, 0, x, y);
        this.faction = faction;
    }


}
