package sol3675.middleearthindustry.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabMei extends CreativeTabs
{
    public CreativeTabMei(String label)
    {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return MeiContents.iconMei;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel(){
        return "TabMei";
    }
}
