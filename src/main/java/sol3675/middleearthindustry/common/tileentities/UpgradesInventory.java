package sol3675.middleearthindustry.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import sol3675.middleearthindustry.common.MeiContents;

public class UpgradesInventory extends MeiInventoryBase
{
    public ItemStack[] inventory = new ItemStack[2];

    @Override
    public int getSizeInventory()
    {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack itemStack = getStackInSlot(slot);
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        inventory[slot] = itemStack;
    }

    @Override
    public String getInventoryName()
    {
        return "Upgrades";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 8;
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == 0 && (itemStack.getItem() == MeiContents.itemUpgrade) && (itemStack.getItemDamage() == 0))
        {
            return true;
        }
        if(slot == 1 && (itemStack.getItem() == MeiContents.itemUpgrade) && (itemStack.getItemDamage() == 1))
        {
            return true;
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagList list)
    {
        for(int i=0; i < this.inventory.length; ++i)
        {
            if(this.inventory[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(itemTag);
                list.appendTag(itemTag);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagList list)
    {
        for(int i=0; i < list.tagCount(); ++i)
        {
            NBTTagCompound itemTag = list.getCompoundTagAt(i);
            int slot = itemTag.getByte("Slot") & 255;
            if(slot >= 0 && slot<getSizeInventory())
            {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
    }
}
