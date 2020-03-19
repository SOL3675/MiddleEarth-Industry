package sol3675.middleearthindustry.compat.appeng.network;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingJob;
import appeng.container.ContainerOpenContext;
import appeng.container.implementations.ContainerCraftAmount;
import appeng.container.implementations.ContainerCraftConfirm;
import appeng.core.AELog;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.compat.appeng.gui.GuiAEHandler;
import sol3675.middleearthindustry.network.NetworkHandler;
import sol3675.middleearthindustry.references.Constant;

import java.util.concurrent.Future;

public class PacketServerConfirmCraftingJob extends PacketServer
{
    private static final byte MODE_REQUEST_CONFIRM = 1;

    private long amount;
    private boolean heldShift;

    public static void sendConfirmAutoCraft(final EntityPlayer player, final long amount, final boolean isShiftHeld)
    {
        PacketServerConfirmCraftingJob packet = new PacketServerConfirmCraftingJob();
        packet.player = player;
        packet.mode = PacketServerConfirmCraftingJob.MODE_REQUEST_CONFIRM;
        packet.amount = amount;
        packet.heldShift = isShiftHeld;
        NetworkHandler.sendPacketToServer(packet);
    }

    @Override
    protected void readData(final ByteBuf stream)
    {
        this.amount = stream.readLong();
        this.heldShift = stream.readBoolean();
    }

    @Override
    protected void writeData(final ByteBuf stream)
    {
        stream.writeLong(this.amount);
        stream.writeBoolean(this.heldShift);
    }

    @Override
    public void execute()
    {
        if(this.mode != PacketServerConfirmCraftingJob.MODE_REQUEST_CONFIRM)
        {
            return;
        }

        if(this.player.openContainer instanceof ContainerCraftAmount)
        {
            final ContainerCraftAmount cca = (ContainerCraftAmount)this.player.openContainer;
            final Object target = cca.getTarget();
            if(target instanceof IGridHost)
            {
                final IGridHost gh = (IGridHost)target;
                final IGridNode gn = gh.getGridNode( ForgeDirection.UNKNOWN );
                if( gn == null )
                {
                    return;
                }
                final IGrid g = gn.getGrid();
                if( ( g == null ) || ( cca.getItemToCraft() == null ) )
                {
                    return;
                }
                cca.getItemToCraft().setStackSize( this.amount );

                Future<ICraftingJob> futureJob = null;
                try
                {
                    final ICraftingGrid cg = g.getCache(ICraftingGrid.class);
                    futureJob = cg.beginCraftingJob( cca.getWorld(), cca.getGrid(), cca.getActionSrc(), cca.getItemToCraft(), null );
                    final ContainerOpenContext context = cca.getOpenContext();
                    if( context != null )
                    {
                        GuiAEHandler.launchAEGui(Constant.GUI_AUTO_CRAFTING_CONFIRM, this.player, this.player.worldObj, 0, 0, 0);
                        if(this.player.openContainer instanceof ContainerCraftConfirm)
                        {
                            final ContainerCraftConfirm ccc = (ContainerCraftConfirm)this.player.openContainer;
                            ccc.setAutoStart(this.heldShift);
                            ccc.setJob(futureJob);
                            cca.detectAndSendChanges();
                        }
                    }
                }
                catch (final Throwable e)
                {
                    if(futureJob != null)
                    {
                        futureJob.cancel(true);
                    }
                    AELog.error(e);
                }
            }
        }
    }
}
