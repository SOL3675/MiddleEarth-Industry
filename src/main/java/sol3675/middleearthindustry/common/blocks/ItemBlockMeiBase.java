package sol3675.middleearthindustry.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMeiBase extends ItemBlock
{
    public ItemBlockMeiBase(Block block)
    {
        super(block);
        if(((BlockMeiBase)block).subNames.length > 1)
        {
            setHasSubtypes(true);
        }
    }

    @Override
    public int getMetadata(int damageValue)
    {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if(((BlockMeiBase)field_150939_a).subNames != null && ((BlockMeiBase)field_150939_a).subNames.length > 0)
        {
            return getUnlocalizedName() + "." + ((BlockMeiBase)field_150939_a).subNames[Math.min(((BlockMeiBase)field_150939_a).subNames.length - 1, itemStack.getItemDamage())];
        }
        return super.getUnlocalizedName(itemStack);
    }
}
