package sol3675.middleearthindustry.compat.appeng.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.network.NetworkHandler;
import sol3675.middleearthindustry.util.Util;

public class PacketClientSync extends PacketClient
{
    private static final byte MODE_PLAYER_HELD = 1, MODE_SOUND = 2;
    private ItemStack syncStack;
    private boolean syncFlag;
    private String syncString;

    private static PacketClientSync newPacket(final EntityPlayer player, final byte mode)
    {
        PacketClientSync packet = new PacketClientSync();
        packet.player = player;
        packet.mode = mode;
        return packet;
    }

    public static void sendPlayerHeldItem(final EntityPlayer player, final ItemStack heldItem)
    {
        PacketClientSync packet = PacketClientSync.newPacket(player, PacketClientSync.MODE_PLAYER_HELD);
        packet.syncStack = heldItem;
        packet.syncFlag = (heldItem != null);
        NetworkHandler.sendPacketToClient(packet);
    }

    public static void sendPlaySound(final EntityPlayer player, final  String soundLocation)
    {
        PacketClientSync packet = PacketClientSync.newPacket(player, PacketClientSync.MODE_SOUND);
        packet.syncString = soundLocation;
        NetworkHandler.sendPacketToClient(packet);
    }

    @Override
    protected void readData(final ByteBuf stream)
    {
        switch (this.mode)
        {
            case MODE_PLAYER_HELD:
                this.syncFlag = stream.readBoolean();
                if (this.syncFlag)
                {
                    this.syncStack = PacketBase.readItemStack(stream);
                }
                else
                {
                    this.syncStack = null;
                }
                break;

            case MODE_SOUND:
                this.syncString = PacketBase.readString(stream);
                break;
        }
    }

    @Override
    protected void wrappedExecute()
    {
        switch (this.mode)
        {
            case MODE_PLAYER_HELD:
                this.player.inventory.setItemStack(this.syncStack);
                break;

            case MODE_SOUND:
                Util.playClientSound(null, this.syncString);
        }
    }

    @Override
    protected void writeData(final ByteBuf stream)
    {
        switch (this.mode)
        {
            case MODE_PLAYER_HELD:
                stream.writeBoolean(this.syncFlag);
                if(this.syncFlag)
                {
                    PacketBase.writeItemStack(this.syncStack, stream);
                }
                break;

            case MODE_SOUND:
                PacketBase.writeString(this.syncString, stream);
                break;
        }
    }
}
