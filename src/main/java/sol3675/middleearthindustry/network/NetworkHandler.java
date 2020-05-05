package sol3675.middleearthindustry.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import sol3675.middleearthindustry.compat.appeng.network.*;
import sol3675.middleearthindustry.references.ModInfo;

import java.util.HashMap;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
    private static short messageId = 0;

    private static HashMap<Class, Short> ClassToID = new HashMap<Class, Short>();
    private static HashMap<Short, Class> IDToClass = new HashMap<Short, Class>();

    public static void registerPackets()
    {
        packetHandler.registerMessage(MessageTileSync.Handler.class, MessageTileSync.class, ++messageId, Side.SERVER);
    }

    private static void registerPacket(final Class<? extends PacketBase> packetClass)
    {

    }

    public static Class getPacketClassFromID(final short id)
    {
        return NetworkHandler.IDToClass.getOrDefault(id, null);
    }

    public static short getPacketID(final PacketBase packet)
    {
        return NetworkHandler.ClassToID.getOrDefault(packet.getClass(), (short)-1);
    }

    public static void sendPacketToClient(final PacketClient clientPacket)
    {
        PacketWrapper wrapper = new PacketWrapperClient(clientPacket);
        packetHandler.sendTo(wrapper, (EntityPlayerMP)clientPacket.player);
    }

    public static void sendPacketToServer(final PacketServer serverPacket)
    {
        PacketWrapper wrapper = new PacketWrapperServer(serverPacket);
        packetHandler.sendToServer(wrapper);
    }
}
