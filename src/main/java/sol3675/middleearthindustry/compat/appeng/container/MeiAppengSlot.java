package sol3675.middleearthindustry.compat.appeng.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class MeiAppengSlot extends Slot
{
    final Container container;

    public MeiAppengSlot(Container container, IInventory inventory, int id, int x, int y)
    {
        super(inventory, id, x, y);
        this.container = container;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return true;
    }
}
