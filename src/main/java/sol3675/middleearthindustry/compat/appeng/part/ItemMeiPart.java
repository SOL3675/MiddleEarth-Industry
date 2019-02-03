package sol3675.middleearthindustry.compat.appeng.part;

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
import sol3675.middleearthindustry.references.ModInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemMeiPart extends Item implements IPartItem, IItemGroup
{
    public ItemMeiPart()
    {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);

        AEApi.instance().partHelper().setItemBusRenderer(this);
        Map<Upgrades, Integer> possibleUpgradesList;
        for(PartsEnum part : PartsEnum.VALUES)
        {
            possibleUpgradesList = part.getUpgrades();
            for(Upgrades upgrade : possibleUpgradesList.keySet())
            {
                upgrade.registerItem(new ItemStack(this, 1, part.ordinal()), possibleUpgradesList.get(upgrade).intValue());
            }
        }
    }

    @Nullable
    @Override
    public IPart createPartFromItemStack(ItemStack itemStack)
    {
        IPart newPart = null;
        PartsEnum part = PartsEnum.getPartFromDamageValue(itemStack);
        try
        {
            newPart = part.createPartInstance(itemStack);
        }
        catch (Throwable e)
        {
            //ERROR
        }
        return newPart;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List itemList)
    {
        for(int i = 0; i < PartsEnum.VALUES.length; ++i)
        {
            itemList.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedGroupName(Set<ItemStack> set, ItemStack itemStack)
    {
        return PartsEnum.getPartFromDamageValue(itemStack).getGroupName();
    }

    @Override
    public String getUnlocalizedName()
    {
        return ModInfo.MODID + ".item.partmei";
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return PartsEnum.getPartFromDamageValue(itemStack).getUnlocalizedName();
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return AEApi.instance().partHelper().placeBus(itemStack, x, y, z, side, player, world);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack)
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
    public void registerIcons(IIconRegister p_94581_1_)
    {
    }
}
