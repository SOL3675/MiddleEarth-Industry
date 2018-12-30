package sol3675.middleearthindustry.common.tileentities;

import cofh.api.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAutoCraftingTable extends TileEntityMeiMachine{
    public EnergyStorage energyStorage = new EnergyStorage(MeiCfg.AutocraftRequireRF ? 1000000 : 0);
    public ItemStack[] inventory = new ItemStack[9+1];
    public List<IRecipe> recipeList;
    public CrafterPatternInventory pattern;
    public UpgradesInventory upgradesInventory;

    public TileEntityAutoCraftingTable()
    {
        super();
        recipeList = null;
        pattern = new CrafterPatternInventory(this);
        upgradesInventory = new UpgradesInventory();
    }

    public TileEntityAutoCraftingTable(List<IRecipe> recipeList)
    {
        super();
        this.recipeList = recipeList;
        pattern = new CrafterPatternInventory(this, recipeList);
        upgradesInventory = new UpgradesInventory();
    }

    @Override
    public void updateEntity()
    {

    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.writeCustomNBT(nbt, descPacket);
        if(!descPacket)
        {
            nbt.setTag("inventory", Util.writeInventory(inventory));
            NBTTagList upgradeList = new NBTTagList();
            upgradesInventory.writeToNBT(upgradeList);
            nbt.setTag("upgrade", upgradeList);
            NBTTagList patternList = new NBTTagList();
            pattern.writeToNBT(patternList);
            nbt.setTag("pattern", patternList);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.readCustomNBT(nbt, descPacket);
        if(!descPacket)
        {
            inventory = Util.readInventory(nbt.getTagList("inventory", 10), 9);
            NBTTagList upgradeList = nbt.getTagList("upgrade", 10);
            upgradesInventory = new UpgradesInventory();
            NBTTagList patternList = nbt.getTagList("pattern", 10);
            if(recipeList != null)
            {
                pattern = new CrafterPatternInventory(this, recipeList);
            }
            else
            {
                pattern = new CrafterPatternInventory(this);
            }
            pattern.readFromNBT(patternList);
        }
    }

    public boolean consumeItem(Object query, int querySize, ItemStack[] inventory, ArrayList<ItemStack> containerItems)
    {
        for(int i = 0; i < inventory.length; ++i)
        {
            int taken = Math.min(querySize, inventory[i].stackSize);
            boolean doTake = true;
            if(inventory[i].getItem().hasContainerItem(inventory[i]))
            {
                ItemStack container = inventory[i].getItem().getContainerItem(inventory[i]);
                if(container != null && inventory[i].getItem().doesContainerItemLeaveCraftingGrid(inventory[i]))
                {
                    containerItems.add(container.copy());
                    if(inventory[i].stackSize - taken <= 0)
                    {
                        inventory[i] = null;
                        doTake = false;
                    }
                }
                else if(inventory[i].stackSize - taken <= 0)
                {
                    inventory[i] = container;
                    doTake = false;
                }
            }
            if(doTake)
            {
                inventory[i].stackSize -= taken;
                if(inventory[i].stackSize <= 0)
                {
                    inventory[i] = null;
                }
            }
            querySize -= taken;
            if(querySize <= 0)
            {
                break;
            }
        }
        return query == null || querySize <= 0;
    }

}
