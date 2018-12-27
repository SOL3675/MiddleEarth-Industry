package sol3675.middleearthindustry.common.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sol3675.middleearthindustry.MiddleEarthIndustry;
import sol3675.middleearthindustry.references.ModInfo;

import java.util.List;

public abstract class BlockContainerMeiBase extends BlockContainer
{

    public String name;
    public String[] subNames;
    public IIcon[][] icons;
    protected final int iconDimensions;
    public int[] lightOpacities;

    protected BlockContainerMeiBase(String name, Material material, int iconDimensions, Class<? extends ItemBlockContainerMeiBase> itemBlock, String... subNames)
    {
        super(material);
        this.name = name;
        this.subNames = subNames;
        this.iconDimensions = iconDimensions;
        this.icons = new IIcon[subNames.length][iconDimensions];
        this.lightOpacities = new int[subNames.length];
        this.setBlockName(ModInfo.MODID + "." + name);
        GameRegistry.registerBlock(this, itemBlock, name);
        this.setCreativeTab(MiddleEarthIndustry.TABMEI);
    }

    public BlockContainerMeiBase setMetaLightOpacity(int meta, int opacity)
    {
        if(meta >= 0 && meta < this.lightOpacities.length)
        {
            this.lightOpacities[meta] = opacity;
        }
        return this;
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z)
    {
        if(!(world instanceof World))
        {
            return getLightOpacity();
        }
        World w = (World) world;
        if(!w.blockExists(x, y, z))
        {
            return getLightOpacity();
        }
        int meta = world.getBlockMetadata(x, y, z);
        if(meta>=0 && meta < this.lightOpacities.length)
        {
            return this.lightOpacities[meta];
        }
        return getLightOpacity();
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(int i=0; i<subNames.length; ++i)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {

        int meta = world.getBlockMetadata(x, y, z);
        if(meta<icons.length)
        {
            return icons[meta][getSideForTexture(side)];
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if(meta<icons.length)
        {
            return icons[meta][getSideForTexture(side)];
        }
        return null;
    }

    protected int getSideForTexture(int side)
    {
        if(iconDimensions==2)
            return side==0||side==1?0: 1;
        if(iconDimensions==4)
            return side<2?side: side==2||side==3?2: 3;
        return Math.min(side, iconDimensions-1);
    }
}
