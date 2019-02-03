package sol3675.middleearthindustry.compat.appeng.part;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.MiddleEarthIndustry;

public enum ItemsEnum
{
    ITEM_MEIPART("part.base", new ItemMeiPart());

    public static final ItemsEnum[] VALUES = ItemsEnum.values();
    private final String INTERNALNAME;
    private Item item;

    private ItemsEnum(final String internalName, final Item item)
    {
        this.INTERNALNAME = internalName;
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
        return this.INTERNALNAME;
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
