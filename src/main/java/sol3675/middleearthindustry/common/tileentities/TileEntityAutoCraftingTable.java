package sol3675.middleearthindustry.common.tileentities;

import cofh.api.energy.EnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.util.Util;

import java.util.ArrayList;
import java.util.Iterator;

public class TileEntityAutoCraftingTable extends TileEntityMeiMachine{
    public ItemStack[] inventory = new ItemStack[9+1];
    public Constant.TableFaction tableFaction;
    public CrafterPatternInventory pattern;
    public UpgradesInventory upgradesInventory;
    public int facing = 1;
    private int duration = 0;
    private int progress = 40;
    private int consumeEnergy;

    public TileEntityAutoCraftingTable()
    {
        super();
        super.energyStorage = new EnergyStorage(MeiCfg.AutocraftRequireRF ? 1000000 : 0);
        tableFaction = null;
        pattern = new CrafterPatternInventory(this);
        upgradesInventory = new UpgradesInventory();
    }

    public TileEntityAutoCraftingTable(Constant.TableFaction tableFaction)
    {
        super();
        this.tableFaction = tableFaction;
        pattern = new CrafterPatternInventory(this, tableFaction);
        upgradesInventory = new UpgradesInventory();
    }

    @Override
    public void updateEntity()
    {
        //Update Rate
        if(upgradesInventory.getStackInSlot(0) != null)
        {
            progress = 40 - 4 * upgradesInventory.getStackInSlot(0).stackSize;
        }
        else
        {
            progress = 40;
        }
        ++duration;
        if(duration < progress)
        {
            return;
        }
        duration = 0;

        //Crafting
        boolean update = false;
        ItemStack[] outputBuffer = new ItemStack[0];
        CrafterPatternInventory patternInventory = pattern;
        if(patternInventory.inventory[9] != null && canOutput(patternInventory.inventory[9]))
        {
            ItemStack output = patternInventory.inventory[9].copy();
            ArrayList<ItemStack> queryList = new ArrayList();
            for(ItemStack stack : outputBuffer)
            {
                if(stack != null)
                {
                    queryList.add(stack.copy());
                }
            }
            for(ItemStack stack : this.inventory)
            {
                if(stack != null)
                {
                    queryList.add(stack.copy());
                }
            }
            if(upgradesInventory.getStackInSlot(1) != null)
            {
                consumeEnergy = 256 - 24 * upgradesInventory.getStackInSlot(1).stackSize;
            }
            else
            {
                consumeEnergy = 256;
            }
            if(this.hasIngredients(patternInventory, queryList) && (!MeiCfg.AutocraftRequireRF || this.energyStorage.extractEnergy(consumeEnergy, true) == consumeEnergy))
            {
                if(MeiCfg.AutocraftRequireRF)
                {
                    this.energyStorage.extractEnergy(consumeEnergy, false);
                }
                ArrayList<ItemStack> outputList = new ArrayList<ItemStack>();
                outputList.add(output);

                Object[] oreInputs = null;
                ArrayList<Integer> usedOreSlots = new ArrayList();
                if(patternInventory.recipe instanceof ShapedOreRecipe || patternInventory.recipe instanceof ShapelessOreRecipe)
                {
                    oreInputs = patternInventory.recipe instanceof ShapedOreRecipe ? ((ShapedOreRecipe)patternInventory.recipe).getInput() : ((ShapelessOreRecipe)patternInventory.recipe).getInput().toArray();
                }
                for(int i = 0; i < 9; ++i)
                {
                    if(patternInventory.inventory[i] != null)
                    {
                        Object query = patternInventory.inventory[i].copy();
                        int querySize = patternInventory.inventory[i].stackSize;
                        if(query instanceof ItemStack && oreInputs != null)
                        {
                            for (int iOre = 0; iOre < oreInputs.length; ++iOre)
                            {
                                if(!usedOreSlots.contains(Integer.valueOf(iOre)))
                                {
                                    if(Util.stackMatchesObject((ItemStack)query, oreInputs[iOre], true))
                                    {
                                        query = oreInputs[iOre];
                                        querySize = 1;
                                        break;
                                    }
                                }
                            }
                        }
                        boolean taken = false;
                        for(int j = 0; j < outputBuffer.length; ++j)
                        {
                            if(taken = consumeItem(query, querySize, outputBuffer, outputList))
                            {
                                break;
                            }
                        }
                        if(!taken)
                        {
                            this.consumeItem(query, querySize, inventory, outputList);
                        }
                    }
                    outputBuffer = outputList.toArray(new ItemStack[outputList.size()]);
                    update = true;
                }
            }
        }

        //Auto Input Output Test

        TileEntity inventory = this.worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if(outputBuffer != null && outputBuffer.length > 0)
        {
            for(int iOutput = 0; iOutput < outputBuffer.length; ++iOutput)
            {
                ItemStack output = outputBuffer[iOutput];
                if(output != null && output.stackSize > 0)
                {
                    if(!isRecipeIngredient(output))
                    {
                        if((inventory instanceof ISidedInventory && ((ISidedInventory)inventory).getAccessibleSlotsFromSide(facing).length>0) || (inventory instanceof IInventory && ((IInventory)inventory).getSizeInventory() > 0))
                        {
                            output = Util.insertStackIntoInventory((IInventory)inventory, output, facing);
                            if(output  == null || output.stackSize <= 0)
                            {
                                continue;
                            }
                        }
                    }
                    int free = -1;
                    if(iOutput == 0)
                    {
                        if(this.inventory[9] == null && free < 0)
                        {
                            free = 9;
                        }
                        else if(this.inventory[9] != null && OreDictionary.itemMatches(output, this.inventory[9], true) && this.inventory[9].stackSize + output.stackSize <= this.inventory[9].getMaxStackSize())
                        {
                            this.inventory[9].stackSize += output.stackSize;
                            free = -1;
                            break;
                        }
                    }
                    else
                    {
                        for(int i = 0; i < this.inventory.length; ++i)
                        {
                            if(this.inventory[i] == null && free < 0)
                            {
                                free = i;
                            }
                            else if(this.inventory[i] != null && OreDictionary.itemMatches(output, this.inventory[i], true) && this.inventory[i].stackSize + output.stackSize <= this.inventory[i].getMaxStackSize())
                            {
                                this.inventory[i].stackSize += output.stackSize;
                                free = -1;
                                break;
                            }
                        }
                    }
                    if(free >= 0)
                    {
                        this.inventory[free] = output.copy();
                    }
                }
            }
        }

        if((inventory instanceof ISidedInventory && ((ISidedInventory)inventory).getAccessibleSlotsFromSide(facing).length > 0) || (inventory instanceof IInventory && ((IInventory)inventory).getSizeInventory() > 0))
        {
            if(!isRecipeIngredient(this.inventory[9]))
            {
                this.inventory[9] = Util.insertStackIntoInventory((IInventory)inventory, this.inventory[9], facing);
            }
        }

        if(update)
        {
            this.markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
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
            if(tableFaction != null)
            {
                nbt.setString("faction", tableFaction.toString());
            }
            else
            {
                nbt.setString("faction", "general");
            }
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
            upgradesInventory.readFromNBT(upgradeList);
            NBTTagList patternList = nbt.getTagList("pattern", 10);
            if(tableFaction != null)
            {
                pattern = new CrafterPatternInventory(this, tableFaction);
            }
            else
            {
                pattern = new CrafterPatternInventory(this);
            }
            pattern.readFromNBT(patternList);
            String faction = nbt.getString("faction");
            if(faction == "general")
            {
                tableFaction = null;
            }
            for(Constant.TableFaction f : Constant.TableFaction.values())
            {
                if(faction == f.toString())
                {
                    tableFaction = f;
                }
            }
        }
    }

    @Override
    public void receiveMessageFromClient(NBTTagCompound message)
    {
        if(message.hasKey("buttonID"))
        {
            int id = message.getInteger("buttonID");
            if(id == 2)
            {
                CrafterPatternInventory patternInventory = pattern;
                for(int i = 0; i < patternInventory.inventory.length; ++i)
                {
                    patternInventory.inventory[i] = null;
                }
            }
        }
    }

    public boolean consumeItem(Object query, int querySize, ItemStack[] inventory, ArrayList<ItemStack> containerItems)
    {
        for(int i = 0; i < inventory.length; ++i)
        {
            if(inventory[i] != null && Util.stackMatchesObject(inventory[i], query, true))
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
        }
        return query == null || querySize <= 0;
    }

    public boolean hasIngredients(CrafterPatternInventory pattern, ArrayList<ItemStack> queryList)
    {
        Object[] oreInputs = null;
        ArrayList<Integer> usedOreSlots = new ArrayList();
        if(pattern.recipe instanceof ShapedOreRecipe || pattern.recipe instanceof ShapelessOreRecipe)
        {
            oreInputs = pattern.recipe instanceof ShapedOreRecipe ? ((ShapedOreRecipe)pattern.recipe).getInput() : ((ShapelessOreRecipe)pattern.recipe).getInput().toArray();
        }
        boolean match = true;
        for(int i = 0; i < 9; ++i)
        {
            if(pattern.inventory[i] != null)
            {
                Object query = pattern.inventory[i].copy();
                int querySize = pattern.inventory[i].stackSize;
                if(oreInputs != null)
                {
                    for(int iOre = 0; iOre < oreInputs.length; ++iOre)
                    {
                        if(!usedOreSlots.contains(Integer.valueOf(iOre)))
                        {
                            if(Util.stackMatchesObject((ItemStack)query, oreInputs[iOre], true))
                            {
                                query = oreInputs[iOre];
                                querySize = 1;
                                break;
                            }
                        }
                    }
                }

                Iterator<ItemStack> it = queryList.iterator();
                while (it.hasNext())
                {
                    ItemStack next = it.next();
                    if(Util.stackMatchesObject(next, query, true))
                    {
                        int taken = Math.min(querySize, next.stackSize);
                        next.stackSize -= taken;
                        if(next.stackSize <= 0)
                        {
                            it.remove();
                        }
                        querySize -= taken;
                        if(querySize <= 0)
                        {
                            break;
                        }
                    }
                }
                if(querySize > 0)
                {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }

    public boolean canOutput(ItemStack output)
    {
        if(this.inventory[9] == null)
        {
            return true;
        }
        else if(OreDictionary.itemMatches(output, this.inventory[9], true) && ItemStack.areItemStackTagsEqual(output, this.inventory[9]) && this.inventory[9].stackSize + output.stackSize <= this.inventory[9].getMaxStackSize())
        {
            return true;
        }
        return false;
    }

    public boolean isRecipeIngredient(ItemStack stack)
    {
        CrafterPatternInventory patternInventory = pattern;
        for(int i = 0; i < 9; ++i)
        {
            if(patternInventory.inventory[i] != null && OreDictionary.itemMatches(patternInventory.inventory[i], stack, false))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if(slot<inventory.length)
        {
            return inventory[slot];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);
        if(stack != null)
        {
            if(stack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                stack = stack.splitStack(amount);
                if(stack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        this.markDirty();
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        if(stack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;
        if(stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return "MeiAutoCraftingTable";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        //TODO
        //Check Alignment
        return true;
    }

    @Override
    public void openInventory(){}

    @Override
    public void closeInventory(){}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if(stack == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        if(sideConfigItem[side] == 0 || sideConfigItem[side] == 1)
        {
            return new int[]{0,1,2,3,4,5,6,7,8};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        if(sideConfigItem[side] == 0 || sideConfigItem[side] == 1)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        if(sideConfigItem[side] == 0 || sideConfigItem[side] == 2)
        {
            return true;
        }
        return false;
    }


}
