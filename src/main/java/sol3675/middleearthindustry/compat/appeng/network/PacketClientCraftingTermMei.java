package sol3675.middleearthindustry.compat.appeng.network;

import appeng.api.AEApi;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import sol3675.middleearthindustry.network.NetworkHandler;

import java.util.Iterator;

public class PacketClientCraftingTermMei extends PacketClient
{
    private static final byte MODE_RECEIVE_CHANGE = 0, MODE_RECEIEVE_FULL_LIST = 1, MODE_RECIEVE_SORTS = 3;
    private IAEItemStack changedStack;
    private IItemList<IAEItemStack> fullList;
    private SortOrder sortingOrder;
    private SortDir sortingDirection;
    private ViewItems viewMode;

    private static PacketClientCraftingTermMei newPacket(final EntityPlayer player, final byte mode)
    {
        PacketClientCraftingTermMei packet = new PacketClientCraftingTermMei();
        packet.player = player;
        packet.mode = mode;
        return packet;
    }

    public static void sendAllNetworkItems(final EntityPlayer player, final IItemList<IAEItemStack> fullList)
    {
        PacketClientCraftingTermMei packet = newPacket(player, PacketClientCraftingTermMei.MODE_RECEIEVE_FULL_LIST);
        packet.useCompression = true;
        packet.fullList = fullList;
        NetworkHandler.sendPacketToClient(packet);
    }

    public static void sendModeChange(final EntityPlayer player, final SortOrder order, final SortDir direction, final ViewItems viewMode)
    {
        PacketClientCraftingTermMei packet = newPacket(player, PacketClientCraftingTermMei.MODE_RECIEVE_SORTS);
        packet.sortingDirection = direction;
        packet.sortingOrder = order;
        packet.viewMode = viewMode;
        NetworkHandler.sendPacketToClient(packet);
    }

    public static void stackAmountChanged(final EntityPlayer player, final IAEItemStack change)
    {
        PacketClientCraftingTermMei packet = newPacket(player, PacketClientCraftingTermMei.MODE_RECEIVE_CHANGE);
        packet.changedStack = change;
        NetworkHandler.sendPacketToClient(packet);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void wrappedExecute()
    {
        Gui gui = Minecraft.getMinecraft().currentScreen;
        //TODO
    }

    @Override
    protected void readData(final ByteBuf stream)
    {
        switch (this.mode)
        {
            case PacketClientCraftingTermMei.MODE_RECEIEVE_FULL_LIST:
                this.fullList = AEApi.instance().storage().createItemList();
                int count = stream.readInt();
                for(int i = 0; i < count; i++)
                {
                    if(stream.readableBytes() <= 0)
                    {
                        break;
                    }
                    IAEItemStack itemStack = PacketBase.readAEItemStack(stream);
                    if(itemStack != null)
                    {
                        this.fullList.add(itemStack);
                    }
                }
                break;

            case PacketClientCraftingTermMei.MODE_RECEIVE_CHANGE:
                int changeAmount = stream.readInt();
                this.changedStack = PacketBase.readAEItemStack(stream);
                this.changedStack.setStackSize(changeAmount);
                break;

            case PacketClientCraftingTermMei.MODE_RECIEVE_SORTS:
                this.sortingDirection = SortDir.values()[stream.readInt()];
                this.sortingOrder = SortOrder.values()[stream.readInt()];
                this.viewMode = ViewItems.values()[stream.readInt()];
                break;
        }
    }

    @Override
    protected void writeData(final ByteBuf stream)
    {
        switch (this.mode)
        {
            case PacketClientCraftingTermMei.MODE_RECEIEVE_FULL_LIST:
                if(this.fullList == null)
                {
                    stream.writeInt(0);
                    return;
                }
                stream.writeInt(this.fullList.size());
                Iterator<IAEItemStack> listIterator = this.fullList.iterator();
                while (listIterator.hasNext())
                {
                    PacketBase.writeAEItemStack(listIterator.next(), stream);
                }
                break;

            case PacketClientCraftingTermMei.MODE_RECEIVE_CHANGE:
                stream.writeInt((int)this.changedStack.getStackSize());
                PacketBase.writeAEItemStack(this.changedStack, stream);
                break;

            case PacketClientCraftingTermMei.MODE_RECIEVE_SORTS:
                stream.writeInt(this.sortingDirection.ordinal());
                stream.writeInt(this.sortingOrder.ordinal());
                stream.writeInt(this.viewMode.ordinal());
                break;
        }
    }
}
