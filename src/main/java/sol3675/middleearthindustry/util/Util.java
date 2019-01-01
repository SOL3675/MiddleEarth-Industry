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
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class Util
{
    public static boolean isExistingOreName(String name)
    {
        if(!OreDictionary.doesOreNameExist(name))
        {
            return false;
        }
        else
        {
            return !OreDictionary.getOres(name).isEmpty();
        }
    }

    public static boolean compareToOreName(ItemStack stack, String oreName)
    {
        if(isExistingOreName(oreName))
        {
            return false;
        }
        ItemStack comp = copyStackWithAmount(stack, 1);
        ArrayList<ItemStack> s = OreDictionary.getOres(oreName);
        for(ItemStack st : s)
        {
            if(ItemStack.areItemStacksEqual(comp, st))
            {
                return true;
            }
        }
        return false;
    }

    public static ItemStack copyStackWithAmount(ItemStack stack, int amount)
    {
        if(stack==null)
        {
            return null;
        }
        ItemStack s2 = stack.copy();
        s2.stackSize=amount;
        return s2;
    }

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


    public static boolean stackMatchesObject(ItemStack stack, Object o)
    {
        return stackMatchesObject(stack, o, false);
    }

    public static boolean stackMatchesObject(ItemStack stack, Object o, boolean checkNBT)
    {
        if(o instanceof ItemStack)
        {
            return OreDictionary.itemMatches((ItemStack)o, stack, false) && (!checkNBT || ((ItemStack)o).getItemDamage() == OreDictionary.WILDCARD_VALUE || ItemStack.areItemStackTagsEqual((ItemStack)o, stack));
        }
        else if(o instanceof ArrayList)
        {
            for(Object io : (ArrayList)o)
            {
                if(io instanceof ItemStack && OreDictionary.itemMatches((ItemStack)io, stack, false) && (!checkNBT || ((ItemStack)io).getItemDamage() == OreDictionary.WILDCARD_VALUE || ItemStack.areItemStackTagsEqual((ItemStack)io, stack)))
                {
                    return true;
                }
            }
        }
        else if(o instanceof ItemStack[])
        {
            for(ItemStack io : (ItemStack[])o)
            {
                if(OreDictionary.itemMatches(io, stack, false) && (!checkNBT || io.getItemDamage() == OreDictionary.WILDCARD_VALUE || ItemStack.areItemStackTagsEqual(io, stack)))
                {
                    return true;
                }
            }
        }
        else if(o instanceof String)
        {
            return compareToOreName(stack, (String)o);
        }
        return false;
    }


}
