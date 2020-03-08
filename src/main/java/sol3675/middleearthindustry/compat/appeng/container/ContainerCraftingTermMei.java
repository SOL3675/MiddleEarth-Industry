package sol3675.middleearthindustry.compat.appeng.container;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.ViewItems;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.PlayerSource;
import appeng.api.networking.storage.IBaseMonitor;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import sol3675.middleearthindustry.compat.appeng.container.slot.SlotCraftingTermResult;
import sol3675.middleearthindustry.compat.appeng.container.slot.SlotTable;
import sol3675.middleearthindustry.compat.appeng.container.slot.SlotViewCell;
import sol3675.middleearthindustry.compat.appeng.network.PacketClientCraftingTermMei;
import sol3675.middleearthindustry.compat.appeng.network.PacketClientSync;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiTerm;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.util.Util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ContainerCraftingTermMei extends ContainerBaseAE implements IMEMonitorHandlerReceiver<IAEItemStack>
{

    private final PartMeiTerm meiTerm;
    private final SlotTable tableSlot;
    private final PlayerSource playerSource;
    private final SlotCraftingTermResult outputSlot;
    private ItemStack table;
    private IMEMonitor<IAEItemStack> monitor;
    private int firstCraftingSlotNumer = -1, lastCraftingSlotNumber = -1, firstViewSlotNumber = -1, lastViewSlotNumber = -1;
    private Constant.TableFaction tableFaction = null;
    private SortOrder cachedSortOrder = PartMeiTerm.DEFAULT_SORT_ORDER;
    private SortDir cachedSortDir = PartMeiTerm.DEFAULT_SORT_DIR;
    private ViewItems cachedViewMode = PartMeiTerm.DEFAULT_VIEW_MODE;

    private static int CRAFTING_GRID_SIZE = 3;

    public ContainerCraftingTermMei(final PartMeiTerm terminal, final EntityPlayer player)
    {
        super(player);
        this.meiTerm = terminal;
        this.playerSource = new PlayerSource(this.player, terminal);
        this.bindPlayerInventory(player.inventory, 162, 220);

        Slot craftingSlot = null;
        for(int row = 0; row < ContainerCraftingTermMei.CRAFTING_GRID_SIZE; row++)
        {
            for(int column = 0; column < ContainerCraftingTermMei.CRAFTING_GRID_SIZE; column++)
            {
                int slotIndex = (row * ContainerCraftingTermMei.CRAFTING_GRID_SIZE) + column;
                craftingSlot = new Slot(terminal, slotIndex, 37 + (column * 18), -72 + (row * 18));
                this.addSlotToContainer(craftingSlot);
                if((row + column) == 0)
                {
                    this.firstCraftingSlotNumer = craftingSlot.slotNumber;
                }
            }
        }
        if(craftingSlot != null)
        {
            this.lastCraftingSlotNumber = craftingSlot.slotNumber;
        }

        this.tableSlot = new SlotTable(terminal, PartMeiTerm.TABLE_SLOT_INDEX, 55, -90);
        this.addSlotToContainer(this.tableSlot);

        this.outputSlot = new SlotCraftingTermResult(player, this, terminal, terminal, PartMeiTerm.RESULT_SLOT_INDEX, 131, -72 + 18);
        this.addSlotToContainer(this.outputSlot);

        SlotViewCell viewSlot = null;
        for(int viewSlotID = PartMeiTerm.VIEW_SLOT_MIN; viewSlotID <= PartMeiTerm.VIEW_SLOT_MAX; viewSlotID++)
        {
            int row = viewSlotID - PartMeiTerm.VIEW_SLOT_MIN;
            int yPos = 8 + (row * 18);
            viewSlot = new SlotViewCell(terminal, viewSlotID, 206, yPos);
            this.addSlotToContainer(viewSlot);
            if(row == 0)
            {
                this.firstViewSlotNumber = viewSlot.slotNumber;
            }
        }
        if(viewSlot != null)
        {
            this.lastViewSlotNumber = viewSlot.slotNumber;
        }

        if(Util.isServer())
        {
            this.registerForUpdates();
        }
        this.attachToMonitor();
    }

    private boolean attachToMonitor()
    {
        if(Util.isClient())
        {
            return false;
        }

        if(this.monitor != null)
        {
            this.monitor.removeListener(this);
        }

        IGrid grid = this.meiTerm.getGridBlock().getGrid();
        if(grid != null)
        {
            this.monitor = this.meiTerm.getItemInventory();
            if(this.monitor != null)
            {
                this.monitor.addListener(this, grid.hashCode());
                return true;
            }
        }
        return false;
    }

    private boolean clearCraftingGrid(final boolean sendUpdate)
    {
        if(Util.isClient())
        {
            return false;
        }
        boolean clearedAll = true;

        for(int index = this.firstCraftingSlotNumer; index <= this.lastCraftingSlotNumber; index++)
        {
            Slot slot = (Slot)this.inventorySlots.get(index);
            if((slot == null) || (!slot.getHasStack()))
            {
                continue;
            }
            ItemStack slotStack = slot.getStack();
            boolean didMerge = this.mergeWithMENetwork(slotStack);
            if(!didMerge)
            {
                clearedAll = false;
                continue;
            }

            if((slotStack == null) || (slotStack.stackSize == 0))
            {
                slot.putStack(null);
            }
            else
            {
                clearedAll = false;
                slot.onSlotChanged();
            }
        }
        this.detectAndSendChanges();
        return clearedAll;
    }

    private void doShiftAutoCrafting(final EntityPlayer player)
    {
        boolean didMerge;
        int autoCraftCounter = 0;
        ItemStack resultStack = this.outputSlot.getStack();
        ItemStack slotStackOriginal = resultStack.copy();

        for (autoCraftCounter = slotStackOriginal.stackSize; autoCraftCounter <= 64; autoCraftCounter += slotStackOriginal.stackSize)
        {
            didMerge = (this.mergeSlotWithPlayerInventory(resultStack) || (this.mergeSlotWithHotbarInventory(resultStack)));
            if(didMerge)
            {
                this.outputSlot.onPickupFromSlotViaTransfer(player, resultStack);
                this.onCraftMatrixChanged(null);
                resultStack = this.outputSlot.getStack();
                if((resultStack == null) || (resultStack.stackSize == 0))
                {
                    break;
                }
                if(!resultStack.getItem().equals(slotStackOriginal.getItem()))
                {
                    break;
                }
            }
            else
            {
                break;
            }
        }
        if(autoCraftCounter > 0)
        {
            this.outputSlot.onSlotChanged();
            this.detectAndSendChanges();
        }
    }

    private boolean doStackMatch(final IAEItemStack keyStack, final IAEItemStack potentialMatch)
    {
        if(keyStack.getItemStack().isItemEqual(potentialMatch.getItemStack()))
        {
            return true;
        }

        int[] keyIDs = OreDictionary.getOreIDs(keyStack.getItemStack());
        int[] matchIDs = OreDictionary.getOreIDs(potentialMatch.getItemStack());

        if((keyIDs.length == 0) || (matchIDs.length == 0))
        {
            return false;
        }

        for (int keyID : keyIDs)
        {
            for (int matchID : matchIDs)
            {
                if(keyID == matchID)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private ItemStack findMatchingResult()
    {
        InventoryCrafting craftingInventory = new InventoryCrafting(new ContainerInternalCrafting(), ContainerCraftingTermMei.CRAFTING_GRID_SIZE, ContainerCraftingTermMei.CRAFTING_GRID_SIZE);
        for (int slotIndex = 0; slotIndex < 9; slotIndex ++)
        {
            craftingInventory.setInventorySlotContents(slotIndex, this.meiTerm.getStackInSlot(slotIndex));
        }
        final IRecipe recipe = Util.findRecipe(craftingInventory, this.meiTerm.getWorldObj(), this.tableFaction);
        return recipe.getCraftingResult(craftingInventory);
    }

    private ItemStack[] getViewCells()
    {
        List<ItemStack> viewCells = new ArrayList<ItemStack>();
        Slot viewSlot;
        for(int viewSlotIndex = this.firstViewSlotNumber; viewSlotIndex <= this.lastViewSlotNumber; viewSlotIndex++)
        {
            viewSlot = this.getSlotOrNull(viewSlotIndex);
            if((viewSlot == null) || !viewSlot.getHasStack())
            {
                continue;
            }
            viewCells.add(viewSlot.getStack());
        }
        return viewCells.toArray(new ItemStack[viewCells.size()]);
    }

    private void getTable()
    {
        if(this.table == this.tableSlot.getStack())
        {
            return;
        }
        if(Util.isFactionTable(this.table))
        {
            this.table = this.tableSlot.getStack();
            this.tableFaction = Constant.getFactionFromTable(this.table);
            return;
        }
        this.table = null;
        this.tableFaction = null;
    }

    private boolean mergeWithMENetwork(final ItemStack itemStack)
    {
        IAEItemStack toInject = AEApi.instance().storage().createItemStack(itemStack);
        IAEItemStack leftOver = this.monitor.injectItems(toInject, Actionable.MODULATE, this.playerSource);
        if((leftOver != null) && (leftOver.getStackSize() > 0))
        {
            if(leftOver.getStackSize() == toInject.getStackSize())
            {
                return false;
            }
            itemStack.stackSize = (int)leftOver.getStackSize();
            return true;
        }
        itemStack.stackSize = 0;
        return true;
    }

    private boolean mergeWithViewCells(final ItemStack itemStack)
    {
        if(!this.meiTerm.isItemValidForSlot(PartMeiTerm.VIEW_SLOT_MIN, itemStack))
        {
            return false;
        }
        Slot viewSlot;
        for(int viewSlotIndex = this.firstViewSlotNumber; viewSlotIndex <= this.lastViewSlotNumber; viewSlotIndex++)
        {
            viewSlot = this.getSlotOrNull(viewSlotIndex);
            if(viewSlot == null)
            {
                continue;
            }
            if(viewSlot.getHasStack())
            {
                continue;
            }
            viewSlot.putStack(itemStack.copy());
            itemStack.stackSize = 0;
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    private void updateGUIViewCells()
    {
        Gui gui = Minecraft.getMinecraft().currentScreen;
        //TODO
    }

    @Override
    protected boolean detectAndSendChangesMP(@Nonnull EntityPlayerMP playerMP) {
        boolean sendModeUpdate = false;
        if(cachedSortOrder != this.meiTerm.getSortingOrder())
        {
            this.cachedSortOrder = this.meiTerm.getSortingOrder();
            sendModeUpdate = true;
        }
        if(cachedSortDir != this.meiTerm.getSortingDirection())
        {
            this.cachedSortDir = this.meiTerm.getSortingDirection();
            sendModeUpdate = true;
        }
        if(this.cachedViewMode != this.meiTerm.getViewMode())
        {
            sendModeUpdate = true;
        }

        if(sendModeUpdate)
        {
            PacketClientCraftingTermMei.sendModeChange(this.player, this.cachedSortOrder, this.cachedSortDir, this.cachedViewMode);
        }

        if(this.monitor == null)
        {
            if(this.attachToMonitor())
            {
                this.onClientRequestFullUpdate(this.player);
            }
        }
        return false;
    }

    protected boolean slotClickedWasInCraftingInventory(final int slotNumber)
    {
        return (slotNumber >= this.firstCraftingSlotNumer) && (slotNumber <= this.lastCraftingSlotNumber);
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player)
    {
        if(this.meiTerm != null)
        {
            return this.meiTerm.isUseableByPlayer(player);
        }
        return false;
    }

    public void changeSlotsYOffset(final int deltaY)
    {
        for(Object slotObj : this.inventorySlots)
        {
            Slot slot = (Slot)slotObj;
            if((slot.slotNumber >= this.firstViewSlotNumber) && (slot.slotNumber <= this.lastViewSlotNumber))
            {
                continue;
            }
            slot.yDisplayPosition += deltaY;
        }
    }

    @Override
    public boolean isValid(final Object authToken)
    {
        if(this.monitor == null)
        {
            return false;
        }

        IGrid grid = this.meiTerm.getGridBlock().getGrid();
        if(grid != null)
        {
            if(grid.hashCode() == (Integer)authToken)
            {
                return true;
            }
        }
        this.monitor = null;
        this.onClientRequestFullUpdate(this.player);
        return false;
    }

    public void onClientRequestAutoCraft(final EntityPlayer player, final IAEItemStack result)
    {
        TileEntity tileEntity = this.meiTerm.getHostTile();
        //TODO GUI
    }

    public void onClientRequestClearCraftingGrid(final EntityPlayer player)
    {
        this.clearCraftingGrid(true);
    }

    public void onClientRequestDeposit(final EntityPlayer player, final int mouseButton)
    {
        if((player == null) || (this.monitor == null))
        {
            return;
        }
        ItemStack playerHolding = player.inventory.getItemStack();
        if(playerHolding == null)
        {
            return;
        }
        IAEItemStack toInjectStack = AEApi.instance().storage().createItemStack(playerHolding);

        boolean depositOne = (mouseButton == Constant.MOUSE_BUTTON_RIGHT) || (mouseButton == Constant.MOUSE_WHEEL);
        if(depositOne)
        {
            toInjectStack.setStackSize(1);
        }

        IAEItemStack leftOvetStack = this.monitor.injectItems(toInjectStack, Actionable.MODULATE, this.playerSource);

        if((leftOvetStack != null) && (leftOvetStack.getStackSize() > 0))
        {
            if(toInjectStack.getStackSize() == 1)
            {
                return;
            }
            player.inventory.setItemStack(leftOvetStack.getItemStack());
        }
        else
        {
            if((depositOne) && (playerHolding.stackSize > 1))
            {
                playerHolding.stackSize--;
                player.inventory.setItemStack(playerHolding);
                leftOvetStack = AEApi.instance().storage().createItemStack(playerHolding);
            }
            else
            {
                player.inventory.setItemStack(null);
            }
        }
    }

    public void onClientRequestDepositRegion(final EntityPlayer player, final int slotNumber)
    {
        List<Slot> slotsToDeposit = null;
        if(this.slotClickedWasInPlayerInventory(slotNumber))
        {
            slotsToDeposit = this.getNonEmptySlotsFromPlayerInventory();
        }
        else if(this.slotClickedWasInHotbarInventory(slotNumber))
        {
            slotsToDeposit = this.getNonEmptySlotsFromHotbar();
        }

        if(slotsToDeposit != null)
        {
            for(Slot slot : slotsToDeposit)
            {
                if((slot == null) || (!slot.getHasStack()))
                {
                    continue;
                }
                ItemStack slotStack = slot.getStack();
                boolean didMerge = this.mergeWithMENetwork(slotStack);
                if(!didMerge)
                {
                    continue;
                }
                if((slotStack == null) || (slotStack.stackSize == 0))
                {
                    slot.putStack(null);
                }
                else
                {
                    slot.onSlotChanged();
                }
            }
            this.detectAndSendChanges();
        }
    }

    public void onClientRequestExtract(final EntityPlayer player, final  IAEItemStack requestAEStack, final int mouseButton, final boolean isShiftHeld)
    {
        if((player == null) || (this.monitor == null) || (requestAEStack == null) || (requestAEStack.getStackSize() == 0))
        {
            return;
        }

        Slot targetSlot = null;
        ItemStack cursorStack = null;
        ItemStack requestStack = requestAEStack.getItemStack();
        int amoutToExtract = 0;

        switch (mouseButton)
        {
            case Constant.MOUSE_BUTTON_LEFT:
                if(isShiftHeld)
                {
                    targetSlot = this.locateMergeSlot(requestStack);
                    if(targetSlot != null)
                    {
                        int slotRoom = 0;
                        if(targetSlot.getHasStack())
                        {
                            ItemStack slotStack = targetSlot.getStack();
                            slotRoom = slotStack.getMaxStackSize() - slotStack.stackSize;
                        }
                        else
                        {
                            slotRoom = requestStack.getMaxStackSize();
                        }
                        amoutToExtract = Math.min(requestStack.stackSize, slotRoom);
                    }
                }
                else
                {
                    amoutToExtract = Math.min(requestStack.getMaxStackSize(), requestStack.stackSize);
                }
                break;

            case Constant.MOUSE_BUTTON_RIGHT:
                if(isShiftHeld)
                {
                    amoutToExtract = 1;
                }
                else
                {
                    double halfRequest = Math.ceil(requestStack.stackSize / 2.0D);
                    double halfMax = Math.ceil(requestStack.getMaxStackSize() / 2.0D);
                    amoutToExtract = (int)Math.min(halfMax, halfRequest);
                }
                break;

            case Constant.MOUSE_BUTTON_MIDDLE:
                if(player.capabilities.isCreativeMode)
                {
                    requestStack.stackSize = requestStack.getMaxStackSize();
                    player.inventory.setItemStack(requestStack);
                    PacketClientSync.sendPlayerHeldItem(player, requestStack);
                }
                return;

            case Constant.MOUSE_WHEEL:
                if(isShiftHeld)
                {
                    amoutToExtract = 1;
                }
        }

        if((targetSlot == null) && (amoutToExtract > 0))
        {
            cursorStack = player.inventory.getItemStack();
            if(cursorStack != null)
            {
                if(!cursorStack.isItemEqual(requestStack))
                {
                    return;
                }
                int cursorRoom = (cursorStack.getMaxStackSize() - cursorStack.stackSize);
                amoutToExtract = Math.min(cursorRoom, amoutToExtract);
            }
        }

        if(amoutToExtract <= 0)
        {
            return;
        }

        IAEItemStack toExtract = requestAEStack.copy();
        toExtract.setStackSize(amoutToExtract);
        IAEItemStack extractedStack = this.monitor.extractItems(toExtract, Actionable.MODULATE, this.playerSource);

        if((extractedStack != null) && (extractedStack.getStackSize() > 0))
        {
            if(targetSlot != null)
            {
                if(targetSlot.getHasStack())
                {
                    targetSlot.getStack().stackSize += (int)extractedStack.getStackSize();
                }
                else
                {
                    targetSlot.putStack(extractedStack.getItemStack());
                }
            }
            else
            {
                if(cursorStack != null)
                {
                    cursorStack.stackSize += (int)extractedStack.getStackSize();
                }
                else
                {
                    this.player.inventory.setItemStack(extractedStack.getItemStack());
                }
                PacketClientSync.sendPlayerHeldItem(this.player, this.player.inventory.getItemStack());
            }
        }
    }

    public void onClientRequestFullUpdate(final EntityPlayer player)
    {
        if((this.monitor != null) && (this.meiTerm.isActive()))
        {
            IItemList<IAEItemStack> fullList = this.monitor.getStorageList();
            PacketClientCraftingTermMei.sendAllNetworkItems(player, fullList);
        }
        else
        {
            PacketClientCraftingTermMei.sendAllNetworkItems(player, AEApi.instance().storage().createItemList());
        }
    }

    public void onClientRequestSetSort(final SortOrder order, final SortDir dir, final ViewItems viewMode)
    {
        this.meiTerm.setSorts(order, dir, viewMode);
    }

    @Override
    public void onContainerClosed(final EntityPlayer player)
    {
        super.onContainerClosed(player);

        if(this.meiTerm != null)
        {
            this.meiTerm.removeListener(this);
        }

        if(Util.isServer())
        {
            if(this.monitor != null)
            {
                this.monitor.removeListener(this);
            }
        }
    }

    @Override
    public void onListUpdate()
    {
        if(!this.meiTerm.isActive())
        {
            PacketClientCraftingTermMei.sendAllNetworkItems(this.player, AEApi.instance().storage().createItemList());
        }
    }

    public void onViewCellChange()
    {
        if(Util.isClient())
        {
            this.updateGUIViewCells();
        }
    }

    @Override
    public void postChange(final IBaseMonitor<IAEItemStack> monitor, final Iterable<IAEItemStack> changes, final BaseActionSource actionSource)
    {
        if(this.monitor == null)
        {
            return;
        }

        for (IAEItemStack change : changes)
        {
            IAEItemStack newAmount = this.monitor.getStorageList().findPrecise(change);
            if(newAmount == null)
            {
                newAmount = change.copy();
                newAmount.setStackSize(0);
            }
            PacketClientCraftingTermMei.stackAmountChanged(this.player, newAmount);
        }
    }

    public void registerForUpdates()
    {
        this.meiTerm.registerListener(this);
    }

    public ItemStack requestCraftingReplenishment(final ItemStack itemStack)
    {
        if(this.monitor == null)
        {
            return null;
        }

        ItemStack replenishmentPlayer = this.takeItemFromPlayer(itemStack, 1);
        if(replenishmentPlayer != null)
        {
            return replenishmentPlayer;
        }

        IAEItemStack requestStack = AEApi.instance().storage().createItemStack(itemStack);
        requestStack.setStackSize(1);
        IAEItemStack replenishmentAE = this.monitor.extractItems(requestStack, Actionable.MODULATE, this.playerSource);
        if(replenishmentAE != null)
        {
            return replenishmentAE.getItemStack();
        }

        IItemList<IAEItemStack> networkItems = this.monitor.getStorageList();

        for(IAEItemStack potentialMatch : networkItems)
        {
            if(this.doStackMatch(requestStack, potentialMatch))
            {
                requestStack = potentialMatch.copy();
                requestStack.setStackSize(1);
                replenishmentAE = this.monitor.extractItems(requestStack, Actionable.MODULATE, this.playerSource);
                if((replenishmentAE != null) && (replenishmentAE.getStackSize() > 0))
                {
                    return replenishmentAE.getItemStack();
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack transferStackInSlot(final EntityPlayer player, final int slotNumber)
    {
        if(Util.isClient())
        {
            return null;
        }
        Slot slot = this.getSlotOrNull(slotNumber);
        if((slot != null) && (slot.getHasStack()))
        {
            boolean didMerge = false;
            ItemStack slotStack = slot.getStack();
            if(this.slotClickedWasInCraftingInventory(slotNumber))
            {
                didMerge = this.mergeWithMENetwork(slotStack);
                if(!didMerge)
                {
                    didMerge = this.mergeSlotWithHotbarInventory(slotStack);
                    if(!didMerge)
                    {
                        didMerge = this.mergeSlotWithPlayerInventory(slotStack);
                    }
                }
            }
            else if(this.slotClickedWasInPlayerInventory(slotNumber) || this.slotClickedWasInHotbarInventory(slotNumber))
            {
                if(this.tableSlot.isItemValid(slotStack))
                {
                    didMerge = this.mergeItemStack(slotStack, this.tableSlot.slotNumber, this.tableSlot.slotNumber + 1, false);
                }

                if(!didMerge)
                {
                    didMerge = this.mergeWithViewCells(slotStack);
                    if(!didMerge)
                    {
                        didMerge = this.mergeWithMENetwork(slotStack);
                        if(!didMerge)
                        {
                            didMerge = this.mergeItemStack(slotStack, this.firstCraftingSlotNumer, this.lastCraftingSlotNumber + 1, false);
                            if(!didMerge)
                            {
                                didMerge = this.swapSlotInventoryHotbar(slotNumber, slotStack);
                            }
                        }
                    }
                }
            }
            else if(slot == this.outputSlot)
            {
                this.doShiftAutoCrafting(player);
                return null;
            }
            else if(slot == this.tableSlot)
            {
                didMerge = this.mergeSlotWithHotbarInventory(slotStack);
                if(!didMerge)
                {
                    didMerge = this.mergeSlotWithPlayerInventory(slotStack);
                    if(!didMerge)
                    {
                        didMerge = this.mergeWithMENetwork(slotStack);
                    }
                }
            }
            else if((slotNumber >= this.firstViewSlotNumber) && (slotNumber <= this.lastViewSlotNumber))
            {
                didMerge = this.mergeSlotWithHotbarInventory(slotStack);
                if(!didMerge)
                {
                    this.mergeSlotWithPlayerInventory(slotStack);
                }
            }

            if(didMerge)
            {
                if((slotStack == null) || (slotStack.stackSize == 0))
                {
                    slot.putStack(null);
                }
                else
                {
                    slot.onSlotChanged();
                }
                this.detectAndSendChanges();
            }
        }
        return null;
    }

    @Override
    public void onCraftMatrixChanged(final IInventory inventory)
    {
        this.getTable();
        ItemStack craftResult = this.findMatchingResult();
        this.meiTerm.setInventoryContentsWithoutNotify(PartMeiTerm.RESULT_SLOT_INDEX, craftResult);
    }
}
