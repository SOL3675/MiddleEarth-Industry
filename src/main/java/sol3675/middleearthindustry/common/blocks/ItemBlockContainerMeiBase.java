package sol3675.middleearthindustry.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockContainerMeiBase extends ItemBlock
{
    public ItemBlockContainerMeiBase(Block block)
    {
        super(block);
        if(((BlockContainerMeiBase)block).subNames.length>1)
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
    public String getUnlocalizedName(ItemStack itemstack)
    {
        if(((BlockContainerMeiBase)field_150939_a).subNames!=null && ((BlockContainerMeiBase)field_150939_a).subNames.length>0)
        {
            return getUnlocalizedName() + "." + ((BlockContainerMeiBase)field_150939_a).subNames[ Math.min(((BlockContainerMeiBase)field_150939_a).subNames.length-1, itemstack.getItemDamage())];
        }
        return super.getUnlocalizedName(itemstack);
    }
}
