package sol3675.middleearthindustry.compat.appeng.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.util.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class ContainerBaseAE extends Container
{
    private final HashMap<Integer, Slot> slotMap = new HashMap<Integer, Slot>();
    private final EntityPlayerMP playerMP;
    protected final EntityPlayer player;

    private final Slot[] hotbarSlots = new Slot[9];
    private final Slot[] playerSlots = new Slot[9 * 3];

    public ContainerBaseAE(final EntityPlayer player)
    {
        this.player = player;
        if(player instanceof EntityPlayerMP)
        {
            this.playerMP = (EntityPlayerMP)player;
        }
        else
        {
            this.playerMP = null;
        }
    }

    @Override
    protected Slot addSlotToContainer(@Nonnull final Slot slot)
    {
        super.addSlotToContainer(slot);
        return slot;
    }

    protected boolean detectAndSendChangesMP(@Nonnull final EntityPlayerMP playerMP)
    {
        return false;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        if(this.playerMP != null)
        {
            if(this.detectAndSendChangesMP(this.playerMP))
            {
                this.playerMP.isChangingQuantityOnly = false;
                super.detectAndSendChanges();
            }
        }
    }

    @Nullable
    public Slot getSlotOrNull(final int slotNumber)
    {
        return this.slotMap.getOrDefault(slotNumber, null);
    }

    @Override
    public void onContainerClosed(@Nonnull final EntityPlayer player)
    {
        super.onContainerClosed(player);
        this.slotMap.clear();
    }

    protected final boolean mergeSlotWithHotbarInventory(final ItemStack slotStack)
    {
        return this.mergeItemStack(slotStack, this.hotbarSlots[0].slotNumber, this.hotbarSlots[9 - 1].slotNumber + 1, false);
    }

    protected final boolean mergeSlotWithPlayerInventory(final ItemStack slotStack)
    {
        return this.mergeItemStack(slotStack, this.playerSlots[0].slotNumber, this.playerSlots[9 * 3 - 1].slotNumber+1, false);
    }

    protected final boolean slotClickedWasInHotbarInventory(final int slotNumber)
    {
        return (slotNumber >= this.hotbarSlots[0].slotNumber) && (slotNumber <= this.hotbarSlots[9-1].slotNumber);
    }

    protected final boolean slotClickedWasInPlayerInventory(final int slotNumber)
    {
        return (slotNumber >= this.playerSlots[0].slotNumber) && (slotNumber <= this.playerSlots[9 * 3 - 1].slotNumber);
    }

    protected final boolean swapSlotInventoryHotbar(final int slotNumber, final ItemStack slotStack)
    {
        if(this.slotClickedWasInHotbarInventory(slotNumber))
        {
            return this.mergeSlotWithPlayerInventory(slotStack);
        }
        else if(this.slotClickedWasInPlayerInventory(slotNumber))
        {
            return this.mergeSlotWithHotbarInventory(slotStack);
        }
        return false;
    }

    @Nullable
    protected final ItemStack takeItemFromPlayer(final ItemStack searchStack, final int amount)
    {
        if((searchStack == null) || (amount <= 0))
        {
            return null;
        }

        Slot matchingSlot = null;
        for(Slot slot : this.playerSlots)
        {
            if(!slot.getHasStack())
            {
                continue;
            }
            if(Util.areStacksEqualIgnoreAmount(searchStack, slot.getStack()))
            {
                matchingSlot = slot;
                break;
            }
        }

        if(matchingSlot == null)
        {
            for(Slot slot : this.hotbarSlots)
            {
                if(!slot.getHasStack())
                {
                    continue;
                }
                if(Util.areStacksEqualIgnoreAmount(searchStack, slot.getStack()))
                {
                    matchingSlot = slot;
                    break;
                }
            }
        }

        if(matchingSlot == null)
        {
            return null;
        }

        ItemStack matchStack = matchingSlot.getStack();
        if(matchStack.stackSize < amount)
        {
            return null;
        }

        ItemStack result = matchStack.splitStack(amount);

        if(matchStack.stackSize == 0)
        {
            matchingSlot.putStack(null);
        }
        else
        {
            matchingSlot.putStack(matchStack);
        }
        return result;
    }

    public final void bindPlayerInventory(final IInventory playerInventory, final int inventoryOffsetY, final int hotbarPositionY)
    {
        for(int column = 0; column < 9; column++)
        {
            this.hotbarSlots[column] = new Slot(playerInventory, column, 8 + (column * 18), hotbarPositionY);
            this.addSlotToContainer(this.hotbarSlots[column]);
        }
        for(int row = 0; row < 3; row++)
        {
            for(int column = 0; column < 9; column++)
            {
                int index = column + (row * 9);
                this.playerSlots[index] = new Slot(playerInventory, 9 + index, 8 + (column * 18), (row * 18) + inventoryOffsetY);
                this.addSlotToContainer(this.playerSlots[index]);
            }
        }
    }

    public final ArrayList<Slot> getNonEmptySlotsFromHotbar()
    {
        ArrayList<Slot> list = new ArrayList<Slot>();
        for(Slot slot : this.hotbarSlots)
        {
            if(slot.getHasStack())
            {
                list.add(slot);
            }
        }
        return list;
    }

    public final ArrayList<Slot> getNonEmptySlotsFromPlayerInventory()
    {
        ArrayList<Slot> list = new ArrayList<Slot>();
        for(Slot slot : this.playerSlots)
        {
            if(slot.getHasStack())
            {
                list.add(slot);
            }
        }
        return list;
    }

    public final Slot locateMergeSlot(final ItemStack itemStack)
    {
        Slot emptySlot = null;
        for(Slot slot : this.playerSlots)
        {
            if(slot.getHasStack())
            {
                ItemStack slotStack = slot.getStack();
                if((slotStack.stackSize < slotStack.getMaxStackSize()) && slotStack.isItemEqual(itemStack))
                {
                    return slot;
                }
            }
            else if(emptySlot == null)
            {
                emptySlot = slot;
            }
        }
        return emptySlot;
    }
}
