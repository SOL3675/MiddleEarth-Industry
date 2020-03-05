package sol3675.middleearthindustry.compat.appeng.container;

import appeng.api.networking.security.IActionHost;
import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.helpers.IContainerCraftingPacket;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiTerm;

public class ContainerCraftingTermMei extends ContainerMEMonitorable implements IAEAppEngInventory, IContainerCraftingPacket
{

    private final PartMeiTerm meiTerm;
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);
    private final SlotCraftingMatrix[] craftingSlots = new SlotCraftingMatrix[9];


    public ContainerCraftingTermMei(final InventoryPlayer inventoryPlayer, final ITerminalHost monitorable)
    {
        super(inventoryPlayer, monitorable, false);
        this.meiTerm = (PartMeiTerm) monitorable;


    }

    @Override
    public IInventory getInventoryByName(String s) {
        return null;
    }

    @Override
    public boolean useRealItems() {
        return false;
    }

    @Override
    public void saveChanges() {

    }

    @Override
    public void onChangeInventory(IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return false;
    }

}
