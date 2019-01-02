package sol3675.middleearthindustry.common.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityMeiMachine extends TileEntityMeiBase implements IEnergyReceiver, ISidedInventory
{
    public int[] sideConfigEnergy = {0, 0, 0, 0, 0, 0};
    public int[] sideConfigItem = {0, 0, 0, 0, 0, 0};
    public boolean autoInput = false;
    public boolean autoOutput = false;
    public EnergyStorage energyStorage = new EnergyStorage(0);

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        nbt.setIntArray("sideConfigEnergy", sideConfigEnergy);
        nbt.setIntArray("sideConfigItem", sideConfigItem);
        nbt.setBoolean("autoInput", autoInput);
        nbt.setBoolean("autoOutput", autoOutput);
        energyStorage.writeToNBT(nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        sideConfigEnergy = nbt.getIntArray("sideConfigEnergy");
        if(sideConfigEnergy == null || sideConfigEnergy.length < 6)
        {
            sideConfigEnergy = new int[6];
        }
        sideConfigItem = nbt.getIntArray("sideConfigItem");
        if(sideConfigItem == null || sideConfigItem.length < 6)
        {
            sideConfigItem = new int[6];
        }
        autoInput = nbt.getBoolean("autoInput");
        autoOutput = nbt.getBoolean("autoOutput");
        energyStorage.readFromNBT(nbt);
    }

    public void toggleSideEnergy(int side)
    {
        ++sideConfigEnergy[side];
        if(sideConfigEnergy[side] > 1)
        {
            sideConfigEnergy[side] = 0;
        }
    }

    public void toggleSideItem(int side)
    {
        ++sideConfigItem[side];
        if(sideConfigItem[side] > 3)
        {
            sideConfigItem[side] = 0;
        }
    }

    public void setAutoInput(boolean isAutoInput)
    {
        autoInput = isAutoInput;
    }

    public void setAutoOutput(boolean isAutoOutput)
    {
        autoOutput = isAutoOutput;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection direction)
    {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection direction)
    {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection direction)
    {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(ForgeDirection direction, int amount, boolean simulate)
    {
        if(worldObj.isRemote)
        {

        }
        int rf = energyStorage.receiveEnergy(amount, simulate);
        return rf;
    }
}
