package sol3675.middleearthindustry.compat.appeng.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.MiddleEarthIndustry;

public enum ItemAEEnum
{
    ITEM_AE_PART("part.base", new ItemAEPart());

    public static final ItemAEEnum[] VALUES = ItemAEEnum.values();

    private final String internalName;
    private Item item;

    private ItemAEEnum(final String internalName, final Item item)
    {
        this.internalName = internalName;
        this.item = item;
        this.item.setCreativeTab(MiddleEarthIndustry.TABMEI);
    }

    public ItemStack getDamageStack(final int damageValue)
    {
        return this.getDamageStack(damageValue, 1);
    }

    public ItemStack getDamageStack(final int damageValue, final int size)
    {
        return new ItemStack(this.item, size, damageValue);
    }

    public String getInternalName()
    {
        return this.internalName;
    }

    public Item getItem()
    {
        return this.item;
    }

    public ItemStack getStack()
    {
        return this.getStack(1);
    }

    public ItemStack getStack(final int size)
    {
        return new ItemStack(this.item, size);
    }
}
