package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.container.AEBaseContainer;
import appeng.container.implementations.ContainerCraftAmount;
import appeng.container.implementations.ContainerCraftConfirm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.MiddleEarthIndustry;
import sol3675.middleearthindustry.api.IAECraftingIssuerHost;
import sol3675.middleearthindustry.api.IAECraftingIssuerContainer;
import sol3675.middleearthindustry.compat.appeng.container.ContainerBaseAE;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiBase;
import sol3675.middleearthindustry.references.Constant;
import sol3675.middleearthindustry.references.ModInfo;


public class GuiAEHandler
{

    private static final int DIRECTION_OFFSET = ForgeDirection.values().length;
    private static Object[] extraData = null;

    private static IAECraftingIssuerHost getCraftingIssuerHost(final EntityPlayer player)
    {
        if(player.openContainer instanceof IAECraftingIssuerContainer)
        {
            return ((IAECraftingIssuerContainer)player.openContainer).getCraftingHost();
        }
        if(player.openContainer instanceof ContainerBaseAE)
        {
            Object target = ((AEBaseContainer)player.openContainer).getTarget();
            if(target instanceof IAECraftingIssuerHost)
            {
                return (IAECraftingIssuerHost)target;
            }
        }
        return null;
    }

    private static IPart getPart(final ForgeDirection tileSide, final World world, final int x, final int y, final int z)
    {
        IPartHost partHost = (IPartHost)(world.getTileEntity(x, y, z));
        if(partHost == null)
        {
            return null;
        }
        return (partHost.getPart(tileSide));
    }

    private static IPart getPartFromSidedID(final int ID, final World world, final int x, final int y, final int z)
    {
        ForgeDirection side = ForgeDirection.getOrientation(ID % Constant.GUI_AE_STEP_AMOUNT);
        return GuiAEHandler.getPart(side, world, x, y, z);
    }

    public static Object getPartGuiElement(final ForgeDirection tileSide, final EntityPlayer player, final World world, final int x, final int y, final int z, final boolean isServerSide)
    {
        PartMeiBase part = (PartMeiBase)GuiAEHandler.getPart(tileSide, world, x, y, z);
        if(part == null)
        {
            return null;
        }
        if(isServerSide)
        {
            return part.getServerGuiElement(player);
        }
        return part.getClientGuiElement(player);
    }

    public static int generateSidedID(final int ID, final ForgeDirection side)
    {
        return ID + side.ordinal();
    }

    public static void launchAEGui(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
    {
        player.openGui(MiddleEarthIndustry.instance, ID + GuiAEHandler.DIRECTION_OFFSET, world, x, y, z);
    }

    public static void launchAEGui(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z, final Object[] extraData)
    {
        GuiAEHandler.extraData = extraData;
        player.openGui(MiddleEarthIndustry.instance, ID + GuiAEHandler.DIRECTION_OFFSET, world, x, y, z);
        GuiAEHandler.extraData = null;
    }

    public static void launchAEGui(final PartMeiBase part, final EntityPlayer player, final World world, final int x, final int y, final int z)
    {
        if(part.isPartUseableByPlayer(player))
        {
            player.openGui(MiddleEarthIndustry.instance, part.getSide().ordinal(), world, x, y, z);
        }
    }

    public static Object getClientGuiElementAE(int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
    {
        switch (ID)
        {
            case Constant.GUI_AUTO_CRAFTING_AMOUNT:
                IAECraftingIssuerHost amountHost = GuiAEHandler.getCraftingIssuerHost(player);
                if(amountHost != null)
                {
                    return new GuiCraftAmountBridge(player, amountHost);
                }
                return null;

            case Constant.GUI_AUTO_CRAFTING_CONFIRM:
                IAECraftingIssuerHost confirmHost = GuiAEHandler.getCraftingIssuerHost(player);
                if(confirmHost != null)
                {
                    return new GuiCraftConfirmBridge(player, confirmHost);
                }
                return null;
        }
        return null;
    }

    public static Object getServerGuiElementAE(int ID, final EntityPlayer player, final World world, final int x, final int y, final int z)
    {
        switch (ID)
        {
            case Constant.GUI_AUTO_CRAFTING_AMOUNT:
                IAECraftingIssuerHost amountHost = GuiAEHandler.getCraftingIssuerHost(player);
                if(amountHost != null)
                {
                    return new ContainerCraftAmount(player.inventory, amountHost);
                }
                return null;

            case Constant.GUI_AUTO_CRAFTING_CONFIRM:
                IAECraftingIssuerHost confirmHost = GuiAEHandler.getCraftingIssuerHost(player);
                if(confirmHost != null)
                {
                    return new ContainerCraftConfirm(player.inventory, confirmHost);
                }
                return null;
        }
        return null;
    }
}
