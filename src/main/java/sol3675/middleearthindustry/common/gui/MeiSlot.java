package sol3675.middleearthindustry.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class MeiSlot extends Slot
{
    final Container container;

    public MeiSlot(Container container, IInventory inventory, int id, int x, int y)
    {
        super(inventory, id, x, y);
        this.container = container;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return true;
    }

    public static class Ghost extends MeiSlot
    {
        public Ghost(Container container, IInventory inventory, int id, int x, int y)
        {
            super(container, inventory, id, x, y);
        }

        @Override
        public void putStack(ItemStack itemStack)
        {
            super.putStack(itemStack);
        }

        @Override
        public boolean canTakeStack(EntityPlayer player)
        {
            return false;
        }

        @Override
        public int getSlotStackLimit()
        {
            return 1;
        }
    }

    public static class Output extends MeiSlot
    {
        public Output(Container container, IInventory inventory, int id, int x, int y)
        {
            super(container, inventory, id, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return false;
        }
    }
}


