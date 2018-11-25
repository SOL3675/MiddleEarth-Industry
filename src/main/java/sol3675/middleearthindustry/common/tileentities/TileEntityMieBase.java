package sol3675.middleearthindustry.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityMieBase extends TileEntity{
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.readCustomNBT(nbt, false);
    }

    public abstract void readCustomNBT(NBTTagCompound nbt, boolean descPacket);

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeCustomNBT(nbt, false);
    }

    public abstract void writeCustomNBT(NBTTagCompound nbt, boolean descPacket);

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeCustomNBT(nbt, true);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readCustomNBT(pkt.func_148857_g(), true);
    }

    public void receiveMessageFromClient(NBTTagCompound message)
    {

    }
}
