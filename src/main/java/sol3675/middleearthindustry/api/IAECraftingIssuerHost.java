package sol3675.middleearthindustry.api;

import appeng.api.storage.ITerminalHost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IAECraftingIssuerHost extends ITerminalHost
{
    ItemStack getIcon();

    void launchGUI(EntityPlayer player);
}
