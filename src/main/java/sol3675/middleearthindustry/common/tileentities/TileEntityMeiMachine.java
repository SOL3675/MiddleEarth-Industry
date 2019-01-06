package sol3675.middleearthindustry.common.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileEntityMeiMachine extends TileEntityMeiBase implements IEnergyHandler, ISidedInventory
{
    public int[] itemIO = {6, 6}; //0: TOP, 1: BOTTOM, 2: NORTH, 3: SOUTH, 4: WEST, 5: EAST, 6: NONE
    public EnergyStorage energyStorage = new EnergyStorage(1000000);

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        nbt.setIntArray("itemIO", itemIO);
        energyStorage.writeToNBT(nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        itemIO = nbt.getIntArray("itemIO");
        if(itemIO == null || itemIO.length < 2)
        {
            itemIO = new int[]{6, 6};
        }
        energyStorage.readFromNBT(nbt);
    }

    public void setSideItemInput(int side)
    {
        itemIO[0] = side;
    }

    public void setSideItemOutput(int side)
    {
        itemIO[1] = side;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection direction)
    {
        for(int i = 0; i < 6; ++i)
        {
            if(direction == ForgeDirection.getOrientation(i))
            {
                return i == 0 ? true : false;
            }
        }
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
            return 0;
        }
        int rf = energyStorage.receiveEnergy(amount, simulate);
        return rf;
    }

    @Override
    public int extractEnergy(ForgeDirection direction, int amount, boolean simulate)
    {
        return energyStorage.extractEnergy(amount, simulate);
    }

    @Override
    public void receiveMessageFromClient(NBTTagCompound message) {
        if(message.hasKey("inputSlot"))
        {
            int sideInput = message.getInteger("inputSlot");
            setSideItemInput(sideInput);
        }
        if(message.hasKey("outputSlot"))
        {
            int sideOutput = message.getInteger("outputSlot");
            setSideItemOutput(sideOutput);
        }
    }
}
