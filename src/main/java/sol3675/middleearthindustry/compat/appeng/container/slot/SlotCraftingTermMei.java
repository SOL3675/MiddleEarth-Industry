package sol3675.middleearthindustry.compat.appeng.container.slot;

import appeng.api.config.Actionable;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.container.ContainerNull;
import appeng.container.slot.AppEngCraftingSlot;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import appeng.helpers.InventoryAction;
import appeng.items.storage.ItemViewCell;
import appeng.util.InventoryAdaptor;
import appeng.util.Platform;
import appeng.util.inv.AdaptorPlayerHand;
import appeng.util.item.AEItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SlotCraftingTermMei extends AppEngCraftingSlot
{

    private final Constant.TableFaction faction;

    private final IInventory craftInv;
    private final IInventory pattern;

    private final BaseActionSource mySrc;
    private final IEnergySource energySrc;
    private final IStorageMonitorable storage;
    private final IContainerCraftingPacket container;

    public SlotCraftingTermMei(final Constant.TableFaction faction, final EntityPlayer player, final BaseActionSource mySrc, final IEnergySource energySrc, final IStorageMonitorable storage, final IInventory cMatrix, final IInventory secondMatrix, final IInventory output, final int x, final int y, final IContainerCraftingPacket ccp)
    {
        super(player, cMatrix, output, 0, x, y);
        this.faction = faction;
        this.energySrc = energySrc;
        this.storage = storage;
        this.mySrc = mySrc;
        this.pattern = cMatrix;
        this.craftInv = secondMatrix;
        this.container = ccp;
    }

    public IInventory getCraftingMatrix()
    {
        return this.craftInv;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack item)
    {
    }

    public void doClick(final InventoryAction action, final EntityPlayer player)
    {
        if(this.getStack() == null)
        {
            return;
        }
        if(Platform.isClient())
        {
            return;
        }

        final IMEMonitor<IAEItemStack> inventory = this.storage.getItemInventory();
        final int howManyPerCraft = this.getStack().stackSize;
        int maxTimesToCraft = 0;

        InventoryAdaptor ia = null;
        if(action == InventoryAction.CRAFT_SHIFT)
        {
            ia = InventoryAdaptor.getAdaptor(player, null);
            maxTimesToCraft = (int)Math.floor((double)this.getStack().getMaxStackSize() / (double)howManyPerCraft);
        }
        else if(action == InventoryAction.CRAFT_STACK)
        {
            ia = new AdaptorPlayerHand(player);
            maxTimesToCraft = (int)Math.floor((double)this.getStack().getMaxStackSize() / (double)howManyPerCraft);
        }
        else
        {
            ia = new AdaptorPlayerHand(player);
            maxTimesToCraft = 1;
        }

        maxTimesToCraft = this.capCraftingAttempts(maxTimesToCraft);

        if(ia == null)
        {
            return;
        }

        final ItemStack result = Platform.cloneItemStack(this.getStack());
        if(result == null)
        {
            return;
        }

        for(int x = 0; x < maxTimesToCraft; x++)
        {
            if(ia.simulateAdd(result) == null)
            {
                final IItemList<IAEItemStack> all = inventory.getStorageList();
                final ItemStack extra = ia.addItems(this.craftItem(player, result, inventory, all));
                if(extra != null)
                {
                    final List<ItemStack> drops = new ArrayList<ItemStack>();
                    drops.add(extra);
                    Platform.spawnDrops(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ, drops);
                    return;
                }
            }
        }
    }

    private int capCraftingAttempts(final int maxTimesToCraft)
    {
        return maxTimesToCraft;
    }

    private ItemStack craftItem(final EntityPlayer player, final ItemStack request, final IMEMonitor<IAEItemStack> inventory, final IItemList all)
    {
        ItemStack itemStack = this.getStack();

        if(itemStack != null && Platform.isSameItem(request, itemStack))
        {
            final ItemStack[] set = new ItemStack[this.getPattern().getSizeInventory()];

            if(Platform.isServer())
            {
                final InventoryCrafting ic = new InventoryCrafting(new ContainerNull(), 3, 3);
                for(int x = 0; x < 9; x++)
                {
                    ic.setInventorySlotContents(x, this.getPattern().getStackInSlot(x));
                }

                final IRecipe recipe = Util.findRecipe(ic, player.worldObj, this.faction);

                if(recipe == null)
                {
                    final Item target = request.getItem();
                    if(target.isDamageable() && target.isRepairable())
                    {
                        boolean isBad = false;
                        for(int x = 0; x < ic.getSizeInventory(); x++)
                        {
                            final ItemStack pis = ic.getStackInSlot(x);
                            if(pis == null)
                            {
                                continue;
                            }
                            if(pis.getItem() != target)
                            {
                                isBad = true;
                            }
                        }
                        if(!isBad)
                        {
                            super.onPickupFromSlot(player, itemStack);
                            player.openContainer.onCraftMatrixChanged(this.craftInv);
                            return request;
                        }
                    }
                    return null;
                }

                itemStack = recipe.getCraftingResult(ic);

                if(inventory != null)
                {
                    for(int x = 0; x < this.getPattern().getSizeInventory(); x++)
                    {
                        if(this.getPattern().getStackInSlot(x) != null)
                        {
                            set[x] = Platform.extractItemsByRecipe(this.energySrc, this.mySrc, inventory, player.worldObj, recipe, itemStack, ic, this.getPattern().getStackInSlot(x), x, all, Actionable.MODULATE, ItemViewCell.createFilter(this.container.getViewCells()));
                            ic.setInventorySlotContents(x, set[x]);
                        }
                    }
                }
            }

            if(this.preCraft(player, inventory, set, itemStack))
            {
                this.makeItem(player, itemStack);
                this.postCraft(player, inventory, set, itemStack);
            }
            player.openContainer.onCraftMatrixChanged(this.craftInv);
            return itemStack;
        }

        return null;
    }

    private boolean preCraft(final EntityPlayer player, final IMEMonitor<IAEItemStack> inventory, final ItemStack[] set, final ItemStack result)
    {
        return true;
    }

    private void makeItem(final EntityPlayer player, final ItemStack itemStack)
    {
        super.onPickupFromSlot(player, itemStack);
    }

    private void postCraft(final EntityPlayer player, final  IMEMonitor<IAEItemStack> inventory, final ItemStack[] set, final ItemStack result)
    {
        final List<ItemStack> drops = new ArrayList<ItemStack>();

        if(Platform.isServer())
        {
            for (int slot = 0; slot < this.craftInv.getSizeInventory(); slot++)
            {
                if(this.craftInv.getStackInSlot(slot) == null)
                {
                    this.craftInv.setInventorySlotContents(slot, set[slot]);
                }
                else if(set[slot] != null)
                {
                    final IAEItemStack fail = inventory.injectItems(AEItemStack.create(set[slot]), Actionable.MODULATE, this.mySrc);
                    if(fail != null)
                    {
                        drops.add(fail.getItemStack());
                    }
                }
            }
        }

        if(drops.size() > 0)
        {
            Platform.spawnDrops(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ, drops);
        }
    }

    IInventory getPattern()
    {
        return this.pattern;
    }
}
