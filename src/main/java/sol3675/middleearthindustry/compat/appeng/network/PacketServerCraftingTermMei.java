package sol3675.middleearthindustry.compat.appeng.network;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.storage.data.IAEItemStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import sol3675.middleearthindustry.compat.appeng.container.ContainerCraftingTermMei;
import sol3675.middleearthindustry.network.NetworkHandler;

public class PacketServerCraftingTermMei extends PacketServer
{
    private static final byte MODE_REQUEST_FULL_LIST = 1;
    private static final byte MODE_REQUEST_EXTRACTION = 2;
    private static final byte MODE_REQUEST_DEPOSIT = 3;
    private static final byte MODE_REQUEST_CLEAR_GRID = 4;
    private static final byte MODE_REQUEST_DEPOSIT_REGION = 5;
    private static final byte MODE_REQUEST_SET_SORT = 6;
    private static final byte MODE_REQUEST_SET_GRID = 7;
    private static final byte MODE_REQUEST_AUTO_CRAFT = 8;

    private IAEItemStack itemStack;
    private int mouseButton;
    private boolean isShiftHeld;
    private int slotNumber;
    private SortOrder sortingOrder;
    private SortDir sortingDirection;
    private ViewItems viewMode;
    private IAEItemStack[] gridItems;

    private static PacketServerCraftingTermMei newPacket(final EntityPlayer player, final byte mode)
    {
        PacketServerCraftingTermMei packet = new PacketServerCraftingTermMei();
        packet.player = player;
        packet.mode = mode;
        return packet;
    }

    public static void sendAutoCraft(final EntityPlayer player, final IAEItemStack result)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_AUTO_CRAFT);
        packet.itemStack = result;
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendClearGrid(final EntityPlayer player)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_CLEAR_GRID);
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendDeposit(final EntityPlayer player, final int mouseButton)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_DEPOSIT);
        packet.mouseButton = mouseButton;
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendDepositRegion(final EntityPlayer player, final int slotNumber)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_DEPOSIT_REGION);
        packet.slotNumber = slotNumber;
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendExtract(final EntityPlayer player, final IAEItemStack itemStack, final int mouseButton, final boolean isShiftHeld)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_EXTRACTION);
        packet.itemStack = itemStack;
        packet.mouseButton = mouseButton;
        packet.isShiftHeld = isShiftHeld;
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendFullListRequest(final EntityPlayer player)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_FULL_LIST);
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendMode(final EntityPlayer player, final SortOrder order, final SortDir direction, final ViewItems viewMode)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_SET_SORT);
        packet.sortingDirection = direction;
        packet.sortingOrder = order;
        packet.viewMode = viewMode;
        NetworkHandler.sendPacketToServer(packet);
    }

    public static void sendSetCraftingNEI(final EntityPlayer player, final IAEItemStack[] items)
    {
        PacketServerCraftingTermMei packet = newPacket(player, MODE_REQUEST_SET_GRID);
        packet.gridItems = items;
        NetworkHandler.sendPacketToServer(packet);
    }

    @Override
    public void execute()
    {
        if((this.player != null) && (this.player.openContainer instanceof ContainerCraftingTermMei))
        {
            switch (this.mode)
            {
                case PacketServerCraftingTermMei.MODE_REQUEST_FULL_LIST:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestFullUpdate(this.player);
                    break;

                case PacketServerCraftingTermMei.MODE_REQUEST_EXTRACTION:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestExtract(this.player, this.itemStack, this.mouseButton, this.isShiftHeld);
                    break;

                case PacketServerCraftingTermMei.MODE_REQUEST_DEPOSIT:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestDeposit(this.player, this.mouseButton);
                    break;

                case PacketServerCraftingTermMei.MODE_REQUEST_CLEAR_GRID:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestClearCraftingGrid(this.player);
                    break;

                case PacketServerCraftingTermMei.MODE_REQUEST_DEPOSIT_REGION:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestDepositRegion(this.player, this.slotNumber);
                    break;

                case PacketServerCraftingTermMei.MODE_REQUEST_SET_SORT:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestSetSort(this.sortingOrder, this.sortingDirection, this.viewMode);
                    break;

                case PacketServerCraftingTermMei.MODE_REQUEST_AUTO_CRAFT:
                    ((ContainerCraftingTermMei)this.player.openContainer).onClientRequestAutoCraft(this.player, this.itemStack);
                    break;
            }
        }
    }

    @Override
    protected void readData(final ByteBuf stream)
    {
        switch (this.mode)
        {
            case PacketServerCraftingTermMei.MODE_REQUEST_EXTRACTION:
                this.itemStack = PacketBase.readAEItemStack(stream);
                this.mouseButton = stream.readInt();
                this.isShiftHeld = stream.readBoolean();
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_DEPOSIT:
                this.mouseButton = stream.readInt();
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_DEPOSIT_REGION:
                this.slotNumber = stream.readInt();
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_SET_SORT:
                this.sortingDirection = SortDir.values()[stream.readInt()];
                this.sortingOrder = SortOrder.values()[stream.readInt()];
                this.viewMode = ViewItems.values()[stream.readInt()];
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_SET_GRID:
                this.gridItems = new IAEItemStack[9];
                for(int slotIndex = 0; slotIndex < 9; slotIndex++)
                {
                    if(stream.readBoolean())
                    {
                        this.gridItems[slotIndex] = PacketBase.readAEItemStack(stream);
                    }
                }
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_AUTO_CRAFT:
                this.itemStack = PacketBase.readAEItemStack(stream);
                break;
        }
    }

    @Override
    protected void writeData(final ByteBuf stream)
    {
        switch (this.mode)
        {
            case PacketServerCraftingTermMei.MODE_REQUEST_EXTRACTION:
                PacketBase.writeAEItemStack(this.itemStack, stream);
                stream.writeInt(this.mouseButton);
                stream.writeBoolean(this.isShiftHeld);
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_DEPOSIT:
                stream.writeInt(this.mouseButton);
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_DEPOSIT_REGION:
                stream.writeInt(this.slotNumber);
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_SET_SORT:
                stream.writeInt(this.sortingDirection.ordinal());
                stream.writeInt(this.sortingOrder.ordinal());
                stream.writeInt(this.viewMode.ordinal());
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_SET_GRID:
                for(int slotIndex = 0; slotIndex < 9; slotIndex++)
                {
                    IAEItemStack slotItem = this.gridItems[slotIndex];
                    boolean hasItem = slotItem != null;
                    stream.writeBoolean(hasItem);
                    if(hasItem)
                    {
                        PacketBase.writeAEItemStack(slotItem, stream);
                    }
                }
                break;

            case PacketServerCraftingTermMei.MODE_REQUEST_AUTO_CRAFT:
                PacketBase.writeAEItemStack(this.itemStack, stream);
                break;
        }
    }
}
