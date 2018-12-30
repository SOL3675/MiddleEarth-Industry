package sol3675.middleearthindustry.common.tileentities;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class MeiInventoryBase implements IInventory
{
    public ItemStack[] inventory;

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    public void writeToNBT(NBTTagList list)
    {
        for(int i = 0; i < this.inventory.length; ++i)
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

    public void readFromNBT(NBTTagList list)
    {
        for(int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound itemTag = list.getCompoundTagAt(i);
            int slot = itemTag.getByte("Slot") & 255;
            if(slot >= 0 && slot < getSizeInventory())
            {
                this.inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
    }
}
