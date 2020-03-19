package sol3675.middleearthindustry.compat.appeng.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import sol3675.middleearthindustry.compat.appeng.gui.GuiAEHandler;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiBase;
import sol3675.middleearthindustry.network.NetworkHandler;

public class PacketServerChangeGui extends PacketServer
{
    private static final byte MODE_REGULAR = 0, MODE_PART = 1; //MODE_WIRELESS = 2;

    private int guiID;
    private PartMeiBase part;
    private World world;
    private int x;
    private int y;
    private int z;

    private static PacketServerChangeGui newPacket(final EntityPlayer player, final byte mode)
    {
        PacketServerChangeGui packet = new PacketServerChangeGui();
        packet.player = player;
        packet.mode = mode;
        return packet;
    }

    private static void sendGuiChange(final int guiID, final EntityPlayer player, final World world, final int x, final int y, final int z)
    {
        PacketServerChangeGui packet = newPacket(player, MODE_REGULAR);
        packet.guiID = guiID;
        packet.world = world;
        packet.x = x;
        packet.y = y;
        packet.z = z;
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendGuiChangeToPart(final PartMeiBase part, final EntityPlayer player, final World world, final int x, final int y, final int z)
    {
        PacketServerChangeGui packet = newPacket(player, MODE_PART);
        packet.part = part;
        packet.world = world;
        packet.x = x;
        packet.y = y;
        packet.z = z;
        NetworkHandler.sendPacketToServer(packet);
    }

    @Override
    public void execute()
    {
        switch (this.mode)
        {
            case MODE_REGULAR:
                GuiAEHandler.launchAEGui(this.guiID, this.player, this.world, this.x, this.y, this.z);
                break;

            case MODE_PART:
                GuiAEHandler.launchAEGui(this.part, this.player, this.world, this.x, this.y, this.z);
                break;
        }
    }

    @Override
    protected void readData(final ByteBuf stream)
    {
        if(this.mode == MODE_REGULAR)
        {
            this.guiID = stream.readInt();
        }
        else if(this.mode == MODE_PART)
        {
            this.part = PacketBase.readPart(stream);
        }
        this.world = PacketBase.readWorld(stream);
        this.x = stream.readInt();
        this.y = stream.readInt();
        this.z = stream.readInt();
    }

    @Override
    protected void writeData(final ByteBuf stream)
    {
        if(this.mode == MODE_REGULAR)
        {
            stream.writeInt(this.guiID);
        }
        else if(this.mode == MODE_PART)
        {
            PacketBase.writePart(this.part, stream);
        }
        PacketBase.writeWorld(this.world, stream);
        stream.writeInt(this.x);
        stream.writeInt(this.y);
        stream.writeInt(this.z);
    }
}
