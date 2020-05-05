package sol3675.middleearthindustry.compat.appeng.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.util.Util;

public class SlotTable extends Slot
{
    private int index;

    public SlotTable(final IInventory inventory, final int index, final int x, final int y)
    {
        super(inventory, index, x, y);
        this.index = index;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return Util.isFactionTable(itemStack);
    }
}
