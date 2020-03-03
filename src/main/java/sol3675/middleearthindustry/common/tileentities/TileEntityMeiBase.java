package sol3675.middleearthindustry.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityMeiBase extends TileEntity
{
    //TODO Rewritten
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.readCustomNBT(nbt);
    }

    public abstract void readCustomNBT(NBTTagCompound nbt);

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeCustomNBT(nbt);
    }

    public abstract void writeCustomNBT(NBTTagCompound nbt);

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeCustomNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readCustomNBT(pkt.func_148857_g());
    }

    public void receiveMessageFromClient(NBTTagCompound message)
    {

    }

    public boolean canPlayerAccess(EntityPlayer player)
    {
        return true;
    }

}
