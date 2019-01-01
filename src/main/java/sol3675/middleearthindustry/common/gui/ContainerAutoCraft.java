package sol3675.middleearthindustry.common.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.util.Util;

public abstract class ContainerAutoCraft extends Container
{
    TileEntityAutoCraftingTable tile;
    int slotCount;

    public ContainerAutoCraft(InventoryPlayer inventoryPlayer, TileEntityAutoCraftingTable tile)
    {
        this.tile = tile;
        this.tile.pattern.recalculateOutput();
        for(int i = 0; i < 9; ++i)
        {
            int x = 29 + (i % 3) * 18;
            int y = 17 + (i / 3) * 18;
            this.addSlotToContainer(new MeiSlot.Ghost(this, tile.pattern, i, x, y));
        }
        this.addSlotToContainer(new MeiSlot.Output(this, tile,9,100, 36));
        for(int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(tile, i, 7 + i * 18, 84));
        }
        slotCount = 10;

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 115+i*18));
            }
        }
        for(int i=0; i<9; i++){
            addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 173));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return tile.isUseableByPlayer(player);
    }

    @Override
    public ItemStack slotClick(int id, int button, int modifier, EntityPlayer player)
    {
        Slot slot = id < 0 ? null : (Slot)this.inventorySlots.get(id);
        if(!(slot instanceof MeiSlot.Ghost))
        {
            return super.slotClick(id, button, modifier, player);
        }
        ItemStack stack = null;
        ItemStack stackSlot = slot.getStack();
        if(stackSlot != null)
        {
            stack = stackSlot.copy();
        }
        if(button == 2)
        {
            slot.putStack(null);
        }
        else if(button == 0 || button == 1)
        {
            InventoryPlayer playerInventory = player.inventory;
            ItemStack stackHeld = playerInventory.getItemStack();
            if(stackSlot == null)
            {
                if(stackHeld != null && slot.isItemValid(stackHeld))
                {
                    slot.putStack(Util.copyStackWithAmount(stackHeld, 1));
                }
            }
            else if(stackHeld == null)
            {
                slot.putStack(null);
            }
            else if(slot.isItemValid(stackHeld))
            {
                slot.putStack(Util.copyStackWithAmount(stackHeld, 1));
            }
        }
        else if(button == 5)
        {
            InventoryPlayer playerInventory = player.inventory;
            ItemStack stackHeld = playerInventory.getItemStack();
            if(!slot.getHasStack())
            {
                slot.putStack(Util.copyStackWithAmount(stackHeld, 1));
            }
        }
        return stack;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot)inventorySlots.get(slot);

        if(slotObject != null && slotObject.getHasStack() && !(slotObject instanceof MeiSlot.Ghost))
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if(slot<19)
            {
                if(!this.mergeItemStack(stackInSlot, 19, (19 + 36), true))
                {
                    return null;
                }
            }
            else
            {
                if(!this.mergeItemStack(stackInSlot, 10, 19, false))
                {
                    return null;
                }
            }
            if(stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            }
            else
            {
                slotObject.onSlotChanged();
            }
            if(stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
    }
}
