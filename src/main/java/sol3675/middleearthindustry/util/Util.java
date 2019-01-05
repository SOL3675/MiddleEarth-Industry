package sol3675.middleearthindustry.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import sol3675.middleearthindustry.references.Constant;

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

    public static IRecipe findRecipe(InventoryCrafting crafting, World world, Constant.TableFaction tableFaction)
    {
        List<IRecipe> list = Constant.getRecipe(tableFaction);
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

    public static ItemStack insertStackIntoInventory(IInventory inventory, ItemStack stack, int side)
    {
        if(stack == null || inventory == null)
        {
            return null;
        }
        int stackSize = stack.stackSize;
        if(inventory instanceof ISidedInventory)
        {
            ISidedInventory sidedInventory = (ISidedInventory)inventory;
            int slots[] = sidedInventory.getAccessibleSlotsFromSide(side);
            if(slots == null)
            {
                return stack;
            }
            for(int i = 0; i < slots.length && stack != null; ++i)
            {
                ItemStack existingStack = inventory.getStackInSlot(slots[i]);
                if(existingStack == null)
                {
                    continue;
                }
                ItemStack toInsert = copyStackWithAmount(stack, Math.min(existingStack.getMaxStackSize(), inventory.getInventoryStackLimit()) - existingStack.stackSize);
                if(sidedInventory.canInsertItem(slots[i], toInsert, side))
                {
                    if(OreDictionary.itemMatches(existingStack, stack, true) && ItemStack.areItemStackTagsEqual(stack, existingStack))
                    {
                        stack = addToOccupiedSlot(sidedInventory, slots[i], stack, existingStack);
                    }
                }
            }
            for (int i = 0; i < slots.length && stack != null; ++i)
            {
                if(inventory.getStackInSlot(slots[i]) == null && sidedInventory.canInsertItem(slots[i], copyStackWithAmount(stack, inventory.getInventoryStackLimit()), side))
                {
                    stack = addToEmptyInventorySlot(sidedInventory, slots[i], stack);
                }
            }
        }
        else
        {
            int inventorySize = inventory.getSizeInventory();
            for(int i = 0; i < inventorySize && stack != null; ++i)
            {
                ItemStack existingStack = inventory.getStackInSlot(i);
                if(OreDictionary.itemMatches(existingStack, stack, true) && ItemStack.areItemStackTagsEqual(stack, existingStack))
                {
                    stack = addToOccupiedSlot(inventory, i, stack, existingStack);
                }
            }
            for (int i = 0; i < inventorySize && stack != null; ++i)
            {
                if(inventory.getStackInSlot(i) == null)
                {
                    stack = addToEmptyInventorySlot(inventory, i, stack);
                }
            }
        }
        if(stack == null || stack.stackSize != stackSize)
        {
            inventory.markDirty();
        }
        return stack;
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

    public static ItemStack addToOccupiedSlot(IInventory inventory, int slot, ItemStack stack, ItemStack existingStack)
    {
        int stackLimit = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
        if (stack.stackSize + existingStack.stackSize > stackLimit) {
            int stackDiff = stackLimit - existingStack.stackSize;
            existingStack.stackSize = stackLimit;
            stack = copyStackWithAmount(stack, stack.stackSize-stackDiff);
            inventory.setInventorySlotContents(slot, existingStack);
            return stack;
        }
        existingStack.stackSize += Math.min(stack.stackSize, stackLimit);
        inventory.setInventorySlotContents(slot, existingStack);
        return null;
    }

    public static ItemStack addToEmptyInventorySlot(IInventory inventory, int slot, ItemStack stack)
    {
        if (!inventory.isItemValidForSlot(slot, stack)) {
            return stack;
        }
        int stackLimit = inventory.getInventoryStackLimit();
        inventory.setInventorySlotContents(slot, copyStackWithAmount(stack, Math.min(stack.stackSize, stackLimit)));
        return stackLimit >= stack.stackSize ? null : stack.splitStack(stack.stackSize - stackLimit);
    }

    public static void dropContainerItems(IInventory inventory, World world, int x, int y, int z)
    {
        for(int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if(itemStack != null)
            {
                float f0 = world.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                while (itemStack.stackSize > 0)
                {
                    int i1 = world.rand.nextInt(21) + 10;
                    if(i1 > itemStack.stackSize)
                    {
                        i1 = itemStack.stackSize;
                    }
                    itemStack.stackSize -= i1;
                    EntityItem entityItem = new EntityItem(world, x + f0, y + f1, z + f2, new ItemStack(itemStack.getItem(), i1, itemStack.getItemDamage()));
                    if(itemStack.hasTagCompound())
                    {
                        entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                    }
                    entityItem.motionX = (world.rand.nextGaussian() * 0.05000000074505806D);
                    entityItem.motionY = (world.rand.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D);
                    entityItem.motionZ = (world.rand.nextGaussian() * 0.05000000074505806D);
                    world.spawnEntityInWorld(entityItem);
                }
            }
        }
    }
}
