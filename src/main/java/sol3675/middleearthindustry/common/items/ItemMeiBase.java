package sol3675.middleearthindustry.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import sol3675.middleearthindustry.MiddleEarthIndustry;
import sol3675.middleearthindustry.references.ModInfo;

import java.util.List;

public class ItemMeiBase extends Item{

    public String name;
    public String[] subNames;
    public boolean[] hiddenMeta;
    public IIcon[] icons;

    public ItemMeiBase(String name, int stacksize, String... subNames)
    {
        this.setUnlocalizedName(ModInfo.MODID + "." + name);
        this.setHasSubtypes(subNames != null && subNames.length>0);
        this.setCreativeTab(MiddleEarthIndustry.TABMEI);
        this.setMaxStackSize(stacksize);
        this.name = name;
        this.subNames = subNames;
        this.icons = new IIcon[this.subNames != null?this.subNames.length:1];
        this.hiddenMeta = new boolean[icons.length];
        GameRegistry.registerItem(this, name);
    }

    public String[] getSubNames()
    {
        return subNames;
    }

    public ItemMeiBase setMetaHidden(int... array)
    {
        for(int i : array)
        {
            if(i>=0 && i<hiddenMeta.length)
            {
                this.hiddenMeta[i] = true;
            }
        }
        return this;
    }

    public ItemMeiBase setMetaUnhidden(int... array)
    {
        for(int i : array)
        {
            if(i>=0 && i<hiddenMeta.length)
            {
                this.hiddenMeta[i] = false;
            }
        }
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir)
    {
        if(getSubNames()!=null)
        {
            for(int i=0; i<icons.length; ++i)
            {
                this.icons[i] = ir.registerIcon(ModInfo.TEXTUREPREFIX + name + "_" + getSubNames()[i]);
            }
        }
        else
        {
            this.icons[0] = ir.registerIcon(ModInfo.TEXTUREPREFIX + name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta)
    {
        if(getSubNames()!=null)
        {
            if(meta >= 0 && meta < icons.length)
            {
                return this.icons[meta];
            }
        }
        return icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        if(getSubNames()!=null)
        {
            for(int i=0; i<getSubNames().length; ++i)
            {
                if((i >= 0 && i < hiddenMeta.length)?!hiddenMeta[i]:true)
                {
                    list.add(new ItemStack(this));
                }
            }
        }
        else
        {
            list.add(new ItemStack(this));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if(getSubNames()!=null)
        {
            String subName = stack.getItemDamage()<getSubNames().length?getSubNames()[stack.getItemDamage()]:"";
            return this.getUnlocalizedName() + "." + subName;
        }
        return this.getUnlocalizedName();
    }
}
