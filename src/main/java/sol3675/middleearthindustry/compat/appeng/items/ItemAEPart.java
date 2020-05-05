package sol3675.middleearthindustry.compat.appeng.items;

import appeng.api.AEApi;
import appeng.api.config.Upgrades;
import appeng.api.implementations.items.IItemGroup;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sol3675.middleearthindustry.compat.appeng.part.PartsEnum;
import sol3675.middleearthindustry.references.ModInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemAEPart extends Item implements IPartItem, IItemGroup
{
    public ItemAEPart()
    {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        AEApi.instance().partHelper().setItemBusRenderer(this);
        Map<Upgrades, Integer> possibleUpgradeList;
        for (PartsEnum part : PartsEnum.VALUES)
        {
            possibleUpgradeList = part.getUpgrades();
            for (Upgrades upgrade : possibleUpgradeList.keySet())
            {
                upgrade.registerItem(new ItemStack(this, 1, part.ordinal()), possibleUpgradeList.get(upgrade).intValue());
            }
        }
    }

    @Nullable
    @Override
    public IPart createPartFromItemStack(final ItemStack itemStack)
    {
        IPart newPart = null;
        PartsEnum part = PartsEnum.getPartFromDamageValue(itemStack);

        try
        {
            newPart = part.createPartInstance(itemStack);
        }
        catch (Throwable e)
        {
        }
        return newPart;
    }

    @Override
    public EnumRarity getRarity(final ItemStack itemStack)
    {
        return EnumRarity.rare;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getSpriteNumber()
    {
        return 0;
    }

    @Override
    public void getSubItems(final Item item, final CreativeTabs tab, final List itemList)
    {
        int count = PartsEnum.VALUES.length;
        for (int i= 0; i < count; i++)
        {
            itemList.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedGroupName(final Set<ItemStack> set, final ItemStack itemStack)
    {
        return PartsEnum.getPartFromDamageValue(itemStack).getGroupName();
    }

    @Override
    public String getUnlocalizedName()
    {
        return ModInfo.MODID + ".item.aeparts";
    }

    @Override
    public String getUnlocalizedName(final ItemStack itemStack)
    {
        return PartsEnum.getPartFromDamageValue(itemStack).getUnlocalizedName();
    }

    @Override
    public boolean onItemUse(final ItemStack itemStack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ )
    {
        return AEApi.instance().partHelper().placeBus(itemStack, x, y, z, side, player, world);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister p_94581_1_)
    {
    }
}
