package sol3675.middleearthindustry.compat.appeng.part;

import appeng.api.config.Upgrades;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.Map;

public enum PartsEnum
{
    MORGUL_CRAFTING_TERMINAL("meipart.craftingterm.morgul");

    public static final PartsEnum[] VALUES = PartsEnum.values();

    private final String unlocalizedName;
    private Class<? extends  PartMeiBase> partClass;
    private String groupName;
    private Map<Upgrades, Integer> upgrades = new HashMap<Upgrades, Integer>();

    private PartsEnum(String _unlocalizedName)
    {
        this.unlocalizedName = _unlocalizedName;
    }

    public static PartsEnum getPartFromDamageValue(final ItemStack itemStack)
    {
        int clamped = MathHelper.clamp_int(itemStack.getItemDamage(), 0, PartsEnum.VALUES.length - 1);
        return PartsEnum.VALUES[clamped];
    }

    public PartMeiBase createPartInstance(final ItemStack itemStack) throws InstantiationException, IllegalAccessException
    {
        PartMeiBase part = this.partClass.newInstance();
        part.setupPartFromItem(itemStack);
        return part;
    }

    public ItemStack getStack()
    {
        return ItemsEnum.ITEM_MEIPART.getDamageStack(this.ordinal());
    }

    public String getUnlocalizedName()
    {
        return this.unlocalizedName;
    }

    public String getLocalizedName()
    {
        return StatCollector.translateToLocal(this.unlocalizedName + ".name");
    }

    public String getGroupName()
    {
        return this.groupName;
    }

    public Map<Upgrades, Integer> getUpgrades()
    {
        return this.upgrades;
    }
}
