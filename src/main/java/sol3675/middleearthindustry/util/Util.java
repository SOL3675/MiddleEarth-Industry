package sol3675.middleearthindustry.util;

import lotr.common.recipe.LOTRRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.List;

public class Util
{
    public static IRecipe findRecipe(InventoryCrafting crafting, World world)
    {
        for(int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); ++i)
        {
            IRecipe recipe = (IRecipe)CraftingManager.getInstance().getRecipeList().get(i);
            if(recipe.matches(crafting, world))
            {
                return recipe;
            }
        }
        return null;
    }

    public static IRecipe findRecipe(InventoryCrafting crafting, World world, List<IRecipe> list)
    {
        for(int i = 0; i < list.size(); ++i)
        {
            IRecipe recipe = (IRecipe)list.get(i);
            if(recipe.matches(crafting, world))
            {
                return recipe;
            }
        }
        return null;
    }

    public static class InventoryAutoCrafting extends InventoryCrafting
    {
        private static final Container nullContainer = new Container()
        {
            @Override
            public void onCraftMatrixChanged(IInventory inventory)
            {
            }
            @Override
            public boolean canInteractWith(EntityPlayer p_75145_1_)
            {
                return false;
            }
        };
        public InventoryAutoCrafting(int width, int height)
        {
            super(nullContainer, width, height);
        }
    }

    public static ItemStack[] readInventory(NBTTagList nbt, int size)
    {
        ItemStack[] inventory = new ItemStack[size];
        int max = nbt.tagCount();
        for(int i = 0; i < max; ++i)
        {
            NBTTagCompound itemTag = nbt.getCompoundTagAt(i);
            int slot = itemTag.getByte("Slot") & 255;
            if(slot >= 0 && slot < size)
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }
        return inventory;
    }

    public static NBTTagList writeInventory(ItemStack[] inventory)
    {
        NBTTagList inventoryList = new NBTTagList();
        for(int i = 0; i < inventory.length; ++i)
        {
            if(inventory[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte)i);
                inventory[i].writeToNBT(itemTag);
                inventoryList.appendTag(itemTag);
            }
        }
        return inventoryList;
    }
}
