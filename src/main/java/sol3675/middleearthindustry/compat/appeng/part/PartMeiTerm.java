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
import appeng.items.storage.ItemViewCell;
import appeng.util.Platform;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import lotr.common.block.LOTRBlockCraftingTable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import sol3675.middleearthindustry.compat.appeng.textures.BlockTextureManager;
import sol3675.middleearthindustry.references.ModInfo;
import sol3675.middleearthindustry.util.Util;

import java.io.IOException;
import java.util.List;

public class PartMeiTerm extends PartMeiRotatable implements IInventory, ITerminalHost, IGridTickable
{
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
    //Crafting Matrix: 0~8, Output: 9, Tables slot: 10, View cell slot: 11~15
    public static final int RESULT_SLOT_INDEX = 9, TABLE_SLOT_INDEX = 10, VIEW_SLOT_MIN = 11, VIEW_SLOT_MAX = 15;

    private SortOrder sortingOrder = PartMeiTerm.DEFAULT_SORT_ORDER;
    private SortDir sortingDirection = PartMeiTerm.DEFAULT_SORT_DIR;
    private ViewItems viewMode = PartMeiTerm.DEFAULT_VIEW_MODE;

    public PartMeiTerm(final PartsEnum associatedPart)
    {
        super(associatedPart, SecurityPermissions.EXTRACT, SecurityPermissions.INJECT);
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

    private void notifyListners(final int slotIndex)
    {
        //TODO
    }

    @Override
    public ItemStack decrStackSize(final int slotIndex, final int amount)
    {
        ItemStack returnStack = null;
        if(this.isSlotInRange(slotIndex))
        {
            ItemStack slotStack = this.slots[slotIndex];
            if(slotStack != null)
            {
                if((amount >= slotStack.stackSize) || (slotIndex == PartMeiTerm.RESULT_SLOT_INDEX))
                {
                    returnStack = slotStack.copy();
                    this.slots[slotIndex].stackSize = 0;
                }
                else
                {
                    returnStack = slotStack.splitStack(amount);
                }
                if(this.slots[slotIndex].stackSize == 0)
                {
                    this.slots[slotIndex] = null;
                }
                this.notifyListners(slotIndex);
            }
        }
        return returnStack;
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
        if(this.isSlotInRange(slotIndex))
        {
            return this.slots[slotIndex];
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slotIndex)
    {
        if(this.isSlotInRange(slotIndex))
        {
            return this.slots[slotIndex];
        }
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
        if(this.setInventoryContentsWithoutNotify(slotIndex, slotStack))
        {
            this.notifyListners(slotIndex);
        }
    }

    public boolean setInventoryContentsWithoutNotify(final int slotIndex, final ItemStack slotStack)
    {
        if(this.isSlotInRange(slotIndex))
        {
            this.slots[slotIndex] = slotStack;
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(final int slotIndex, final ItemStack proposedStack)
    {
        if(this.isSlotInRange(slotIndex))
        {
            if(proposedStack == null)
            {
                return true;
            }
            if(slotIndex == PartMeiTerm.TABLE_SLOT_INDEX)
            {
                return Util.isFactionTable(proposedStack);
            }
            if((slotIndex >= PartMeiTerm.VIEW_SLOT_MIN) && (slotIndex <= PartMeiTerm.VIEW_SLOT_MAX))
            {
                return (proposedStack.getItem() instanceof ItemViewCell);
            }
            return true;
        }
        return false;
    }



    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventory(final IPartRenderHelper helper, final RenderBlocks renderer)
    {
        //TODO PartCraftingTerm
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
        if(wrenched)
        {
            return;
        }
        for(int slotIndex = 0; slotIndex < PartMeiTerm.INVENTORY_SIZE; slotIndex++)
        {
            if(slotIndex == PartMeiTerm.RESULT_SLOT_INDEX)
            {
                continue;
            }
            ItemStack slotStack = this.slots[slotIndex];
            if(slotStack != null)
            {
                drops.add(slotStack);
            }
        }
    }

    @Override
    public void getBoxes(final IPartCollisionHelper helper)
    {
        helper.addBox(2.0D, 2.0D, 14.0D, 14.0D, 14.0D, 16.0D);
        helper.addBox( 5.0D, 5.0D, 13.0D, 11.0D, 11.0D, 14.0D );
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
        if(data.hasKey(PartMeiTerm.NBT_KEY_INVENTORY))
        {
            NBTTagList nbtTagList = (NBTTagList)data.getTag(PartMeiTerm.NBT_KEY_INVENTORY);
            for(int listIndex = 0; listIndex < nbtTagList.tagCount(); listIndex++)
            {
                NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(listIndex);
                int slotIndex = nbtTagCompound.getByte(PartMeiTerm.NBT_KEY_SLOT);
                if(this.isSlotInRange(slotIndex))
                {
                    ItemStack slotStack = ItemStack.loadItemStackFromNBT(nbtTagCompound);
                    if(slotIndex == PartMeiTerm.TABLE_SLOT_INDEX)
                    {
                        if(!Util.isFactionTable(slotStack))
                        {
                            slotStack = null;
                        }
                    }
                    this.slots[slotIndex] = slotStack;
                }
            }
        }
        if(data.hasKey(PartMeiTerm.NBT_KEY_SORT_ORDER))
        {
            this.sortingOrder = SortOrder.values()[data.getInteger(PartMeiTerm.NBT_KEY_SORT_ORDER)];
        }
        if(data.hasKey(PartMeiTerm.NBT_KEY_SORT_DIRECTION))
        {
            this.sortingDirection = SortDir.values()[data.getInteger(PartMeiTerm.NBT_KEY_SORT_DIRECTION)];
        }
        if(data.hasKey(PartMeiTerm.NBT_KEY_VIEW_MODE))
        {
            this.viewMode = ViewItems.values()[data.getInteger(PartMeiTerm.NBT_KEY_VIEW_MODE)];
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound data, PartItemStack saveType) {
        super.writeToNBT(data, saveType);
        if((saveType != PartItemStack.World) && (saveType != PartItemStack.Wrench))
        {
            return;
        }
        NBTTagList nbtTagList = new NBTTagList();
        for(int slotId = 0; slotId < PartMeiTerm.INVENTORY_SIZE; slotId++)
        {
            if(this.slots[slotId] != null)
            {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte(PartMeiTerm.NBT_KEY_SLOT, (byte)slotId);
                this.slots[slotId].writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        if(nbtTagList.tagCount() > 0)
        {
            data.setTag(PartMeiTerm.NBT_KEY_INVENTORY, nbtTagList);
        }
        if(this.sortingDirection != PartMeiTerm.DEFAULT_SORT_DIR)
        {
            data.setInteger(PartMeiTerm.NBT_KEY_SORT_DIRECTION, this.sortingDirection.ordinal());
        }
        if(this.sortingOrder != PartMeiTerm.DEFAULT_SORT_ORDER)
        {
            data.setInteger(PartMeiTerm.NBT_KEY_SORT_ORDER, this.sortingOrder.ordinal());
        }
        if(this.viewMode != PartMeiTerm.DEFAULT_VIEW_MODE)
        {
            data.setInteger(PartMeiTerm.NBT_KEY_VIEW_MODE, this.viewMode.ordinal());
        }
    }

    @Override
    public TickingRequest getTickingRequest(IGridNode iGridNode) {
        return new TickingRequest(2, 20, false, false);
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode iGridNode, int i) {
        return TickRateModulation.IDLE;
    }
}
