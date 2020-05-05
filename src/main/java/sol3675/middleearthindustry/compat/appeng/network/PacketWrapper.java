package sol3675.middleearthindustry.compat.appeng.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import sol3675.middleearthindustry.network.NetworkHandler;

public abstract class PacketWrapper implements IMessage
{
    private PacketBase embeddedPacket;

    public PacketWrapper()
    {
    }

    public PacketWrapper(final PacketBase packet)
    {
        this.embeddedPacket = packet;
    }

    public void execute()
    {
        if(this.embeddedPacket != null)
        {
            this.embeddedPacket.execute();
        }
    }

    @Override
    public void fromBytes(final ByteBuf stream)
    {
        short id = stream.readShort();
        if(id == -1)
        {
            return;
        }

        Class epClass = NetworkHandler.getPacketClassFromID(id);
        if(epClass == null)
        {
            return;
        }

        try
        {
            this.embeddedPacket = (PacketBase)epClass.newInstance();
            this.embeddedPacket.fromBytes(stream);
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public void toBytes(final ByteBuf stream)
    {
        if(this.embeddedPacket != null)
        {
            short id = NetworkHandler.getPacketID(this.embeddedPacket);
            stream.writeShort(id);
        }
        else
        {
            stream.writeShort(-1);
        }
    }
}
