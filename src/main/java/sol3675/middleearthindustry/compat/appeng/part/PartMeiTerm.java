package sol3675.middleearthindustry.compat.appeng.part;

import appeng.api.config.SecurityPermissions;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartRenderHelper;
import appeng.api.parts.PartItemStack;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.IConfigManager;
import appeng.util.Platform;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import sol3675.middleearthindustry.references.ModInfo;

import java.io.IOException;
import java.util.List;

public class PartMeiTerm extends PartMeiBase implements IInventory, ITerminalHost, IGridTickable
{
    private static final String NBT_KEY_ROT_DIR = "partRotation";
    private byte renderRotation = 0;

    private static final int INVENTORY_SIZE = 16;
    private static final String NBT_KEY_INVENTORY = "MIE_CT_Inventory";
    private static final String NBT_KEY_SLOT = "Slot#";
    private static final String NBT_KEY_SORT_ORDER = "SortOrder";
    private static final String NBT_KEY_SORT_DIRECTION = "SortDirection";
    private static final String NBT_KEY_VIEW_MODE = "ViewMode";
    private static final double IDLE_POWER_DRAIN = 0.5D;
    public static final SortOrder DEFAULT_SORT_ORDER = SortOrder.NAME;
    public static final SortDir DEFAULT_SORT_DIR = SortDir.ASCENDING;
    public static final ViewItems DEFAULT_VIEW_MODE = ViewItems.ALL;

    private final ItemStack[] slots = new ItemStack[PartMeiTerm.INVENTORY_SIZE];
    //Crafting Matrix: 0~8, Output: 9, Tables slot: 10, View cell slot: 11~16

    private SortOrder sortingOrder = PartMeiTerm.DEFAULT_SORT_ORDER;
    private SortDir sortingDirection = PartMeiTerm.DEFAULT_SORT_DIR;
    private ViewItems viewMode = PartMeiTerm.DEFAULT_VIEW_MODE;

    public PartMeiTerm(final PartsEnum associatedPart, final SecurityPermissions... interactionPermissions)
    {
        super(associatedPart, interactionPermissions);
    }

    protected void rotateRenderer(final RenderBlocks renderer, final boolean reset)
    {
        int rot = (reset ? 0 : this.renderRotation);
        renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth =renderer.uvRotateTop = renderer.uvRotateWest = rot;
    }

    public SortDir getSortingDirection()
    {
        return this.sortingDirection;
    }

    public SortOrder getSortingOrder()
    {
        return this.sortingOrder;
    }

    public ViewItems getViewMode()
    {
        return this.viewMode;
    }

    public World getWorldObj()
    {
        return this.getHostTile().getWorldObj();
    }

    public void setSorts(final SortOrder order, final SortDir dir, final ViewItems viewMode)
    {
        this.sortingDirection = dir;
        this.sortingOrder = order;
        this.viewMode = viewMode;
        this.markDirty();
    }

    private boolean isSlotInRange(final int slotIndex)
    {
        return ((slotIndex >= 0) && (slotIndex < PartMeiTerm.INVENTORY_SIZE));
    }

    @Override
    public boolean onActivate(final EntityPlayer player, final Vec3 position)
    {
        //TODO

        TileEntity tile = this.getHostTile();
        if(!player.isSneaking() && Platform.isWrench(player, player.inventory.getCurrentItem(), tile.xCoord, tile.yCoord, tile.zCoord))
        {
            if(FMLCommonHandler.instance().getEffectiveSide().isServer())
            {
                if((this.renderRotation > 3) || (this.renderRotation < 0))
                {
                    this.renderRotation = 0;
                }
                switch (this.renderRotation)
                {
                    case 0:
                        this.renderRotation = 1;
                        break;
                    case 1:
                        this.renderRotation = 3;
                        break;
                    case 2:
                        this.renderRotation = 0;
                        break;
                    case 3:
                        this.renderRotation = 2;
                        break;
                }
                this.markForUpdate();
                this.markForSave();
            }
            return true;
        }
        return super.onActivate(player, position);
    }

    @Override
    public ItemStack decrStackSize(final int slotIndex, final int amount)
    {
        //TODO
        return null;
    }

    @Override
    public String getInventoryName()
    {
        return ModInfo.MODID + ".crafting.terminal.inventory";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return PartMeiTerm.INVENTORY_SIZE;
    }

    @Override
    public ItemStack getStackInSlot(final int slotIndex)
    {
        //TODO
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slotIndex)
    {
        //TODO
        return null;
    }

    @Override
    public TickingRequest getTickingRequest(final IGridNode grid)
    {
        return new TickingRequest(2, 20, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(final IGridNode node, final int ticksSinceLastCall)
    {
        //TODO
        return null;
    }

    @Override
    public IMEMonitor<IAEItemStack> getItemInventory()
    {
        return this.getGridBlock().getItemMonitor();
    }

    @Override
    public void setInventorySlotContents(final int slotIndex, final ItemStack slotStack)
    {
        //TODO
    }

    @Override
    public boolean isItemValidForSlot(final int slotIndex, final ItemStack proposedStack)
    {
        //TODO
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventory(final IPartRenderHelper helper, final RenderBlocks renderer)
    {
        //TODO
    }

    @Override
    public void renderStatic(final int x, final int y, final int z, final IPartRenderHelper helper, final RenderBlocks renderer)
    {
        //TODO
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer player)
    {
        return this.isPartUseableByPlayer(player);
    }

    @Override
    public int getLightLevel()
    {
        return (this.isActive() ? PartMeiBase.ACTIVE_TERMINAL_LIGHT_LEVEL : 0);
    }

    @Override
    public void getDrops(final List<ItemStack> drops, final boolean wrenched)
    {
        //TODO
    }

    @Override
    public void getBoxes(final IPartCollisionHelper helper)
    {
        //TODO
    }

    @Override
    public IIcon getBreakingTexture()
    {
        //TODO
        return null;
    }

    @Override
    public Object getClientGuiElement(final EntityPlayer player)
    {
        //TODO
        return super.getClientGuiElement(player);
    }

    @Override
    public Object getServerGuiElement(EntityPlayer player)
    {
        //TODO
        return super.getServerGuiElement(player);
    }

    @Override
    public IConfigManager getConfigManager()
    {
        return null;
    }

    @Override
    public int cableConnectionRenderTo()
    {
        return 3;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public IMEMonitor<IAEFluidStack> getFluidInventory()
    {
        return null;
    }

    @Override
    public void markDirty()
    {
        this.markForSave();
    }

    @Override
    public double getIdlePowerUsage() {
        return PartMeiTerm.IDLE_POWER_DRAIN;
    }

    @Override
    public void readFromNBT(NBTTagCompound data)
    {
        super.readFromNBT(data);
        if(data.hasKey(PartMeiTerm.NBT_KEY_ROT_DIR))
        {
            this.renderRotation = data.getByte(PartMeiTerm.NBT_KEY_ROT_DIR);
        }

        //TODO
    }

    @Override
    public boolean readFromStream(ByteBuf stream) throws IOException
    {
        boolean redraw = false;
        redraw |= super.readFromStream(stream);
        byte streamRot = stream.readByte();
        if(this.renderRotation != streamRot)
        {
            this.renderRotation = streamRot;
            redraw |= true;
        }
        return redraw;
    }

    @Override
    public void writeToNBT(NBTTagCompound data, PartItemStack saveType) {
        super.writeToNBT(data, saveType);
        if((saveType == PartItemStack.World) && (this.renderRotation != 0))
        {
            data.setByte(PartMeiTerm.NBT_KEY_ROT_DIR, this.renderRotation);
        }

        //TODO
    }

    @Override
    public void writeToStream(ByteBuf stream) throws IOException
    {
        super.writeToStream(stream);
        stream.writeByte(this.renderRotation);
    }
}
