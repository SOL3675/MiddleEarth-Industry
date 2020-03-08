package sol3675.middleearthindustry.compat.appeng.container.slot;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import sol3675.middleearthindustry.compat.appeng.container.ContainerCraftingTermMei;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.util.Util;

public class SlotCraftingTermResult extends SlotCrafting
{
    private IInventory terminalInventory;
    private Constant.TableFaction faction;
    private ContainerCraftingTermMei hostContainer;

    public SlotCraftingTermResult(final EntityPlayer player, final ContainerCraftingTermMei hostContainer, final IInventory terminalInventory, final IInventory slotInventory, final int slotIndex, final int xPos, final int yPos)
    {
        super(player, terminalInventory, slotInventory, slotIndex, xPos, yPos);
        this.terminalInventory = terminalInventory;
        this.hostContainer = hostContainer;
    }

    public Constant.TableFaction getFaction()
    {
        return this.faction;
    }

    public void setFaction(Constant.TableFaction faction)
    {
        this.faction = faction;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack)
    {

    }

    public void onPickupFromSlotViaTransfer(final EntityPlayer player, final ItemStack itemStack)
    {
        FMLCommonHandler.instance().firePlayerCraftingEvent(player, itemStack, this.terminalInventory);
        this.onCrafting(itemStack);
        if(Util.isClient())
        {
            return;
        }

        for(int slotIndex = 0; slotIndex < 9; slotIndex++)
        {
            ItemStack slotStack = this.terminalInventory.getStackInSlot(slotIndex);
            if(slotStack == null)
            {
                continue;
            }
            boolean shouldDecrement = true;
            if(slotStack.getItem().hasContainerItem(slotStack))
            {
                ItemStack slotContainerStack = slotStack.getItem().getContainerItem(slotStack);
                if(slotContainerStack.isItemStackDamageable())
                {
                    if(slotContainerStack.getItemDamage() >= slotContainerStack.getMaxDamage())
                    {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, slotContainerStack));
                        slotContainerStack = null;
                    }
                }

                if(slotContainerStack != null)
                {
                    if(!slotStack.getItem().doesContainerItemLeaveCraftingGrid(slotStack) || !player.inventory.addItemStackToInventory(slotContainerStack))
                    {
                        this.terminalInventory.setInventorySlotContents(slotIndex, slotContainerStack);
                        shouldDecrement = false;
                    }
                }
            }

            if(shouldDecrement && (slotStack.stackSize == 1))
            {
                //TODO
                //ItemStack replenishment = this.hostContainer.req
            }
        }
    }

}
