package sol3675.middleearthindustry.compat.ie.recipes;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import sol3675.middleearthindustry.common.MeiContents;
import sol3675.middleearthindustry.compat.ie.IECompatContents;
import sol3675.middleearthindustry.util.Util;

public class IECompatRecipes
{
    public static void addWirePressRecipes()
    {
        ArrayListMultimap<String, ItemStack> registeredMoldBases = ArrayListMultimap.create();
        for(String name : OreDictionary.getOreNames())
        {
            if(name.startsWith("wire"))
            {
                String ore = name.substring("wire".length());
                if(Util.isExistingOreName("wire" + ore))
                {
                    registeredMoldBases.putAll("wire", OreDictionary.getOres(name));
                    MetalPressRecipe.addRecipe(IEApi.getPreferredOreStack(name), "ingot" + ore, new ItemStack(MeiContents.itemMisc, 1, 12), 2400);
                }
            }
        }
    }

    public static void addGeneralRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe (new ItemStack(IECompatContents.blockIECompat, 1, 0), new Object[]{
                "TTT",
                "TCT",
                "TTT",
                'T', new ItemStack(IEContent.blockMetalDevice, 1, 10),
                'C', new ItemStack(IEContent.blockStorage, 1, 9)
        }));

        GameRegistry.addRecipe(new ShapedOreRecipe (new ItemStack(IECompatContents.blockIECompat, 1, 1), new Object[]{
                "TTT",
                "TCT",
                "TTT",
                'T', new ItemStack(IECompatContents.blockIECompat, 1, 0),
                'C', new ItemStack(IEContent.blockStorage, 1, 10)
        }));
    }
}
