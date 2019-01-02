package sol3675.middleearthindustry.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.util.Util;

import java.util.ArrayList;

public class CrafterPatternInventory extends MeiInventoryBase
{
    public ItemStack[] inventory = new ItemStack[10];
    public IRecipe recipe;
    public Constant.TableFaction tableFaction;
    //TODO
    final TileEntityAutoCraftingTable tile;

    public CrafterPatternInventory(TileEntityAutoCraftingTable tile)
    {
        this.tile = tile;
        this.tableFaction = null;
    }

    public CrafterPatternInventory(TileEntityAutoCraftingTable tile, Constant.TableFaction tableFaction)
    {
        this.tile = tile;
        this.tableFaction = tableFaction;
    }

    @Override
    public String getInventoryName()
    {
        return "MeiCraftPattern";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public int getSizeInventory()
    {
        return 10;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
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
        if(slot < 9 && itemStack != null)
        {
            if(itemStack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                itemStack = itemStack.splitStack(amount);
                if(itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if(itemStack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        if(slot < 9)
        {
            inventory[slot] = itemStack;
            if(itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            {
                itemStack.stackSize = getInventoryStackLimit();
            }
        }
        recalculateOutput();
    }

    public void recalculateOutput()
    {
        InventoryCrafting crafting = new Util.InventoryAutoCrafting(3, 3);
        for(int i = 0; i < 9; ++i)
        {
            crafting.setInventorySlotContents(i, inventory[i]);
        }
        if(tableFaction != null)
        {
            this.recipe = Util.findRecipe(crafting, tile.getWorldObj(), tableFaction);
        }
        else
        {
            this.recipe = Util.findRecipe(crafting, tile.getWorldObj());
        }
        this.inventory[9] = recipe != null ? recipe.getCraftingResult(crafting) : null;
    }

    public ArrayList<ItemStack> getTotalPossibleOutput()
    {
        ArrayList<ItemStack> outputList = new ArrayList<ItemStack>();
        outputList.add(inventory[9].copy());
        for(int i = 0; i < 9; ++i)
        {
            ItemStack container = inventory[i].getItem().getContainerItem(inventory[i]);
            if(container != null && inventory[i].getItem().doesContainerItemLeaveCraftingGrid(inventory[i]))
            {
                outputList.add(container.copy());
            }
        }
        return outputList;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack)
    {
        return true;
    }

    @Override
    public void markDirty()
    {
        this.tile.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagList list)
    {
        super.readFromNBT(list);
        recalculateOutput();
    }
}
