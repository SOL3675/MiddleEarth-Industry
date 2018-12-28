package sol3675.middleearthindustry.common.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public abstract class ContainerAutoCraft extends Container
{


    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
