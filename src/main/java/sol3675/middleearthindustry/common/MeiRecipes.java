package sol3675.middleearthindustry.common;

import cpw.mods.fml.common.registry.GameRegistry;
import lotr.common.LOTRMod;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class MeiRecipes
{

    private static List[] commonOrcRecipes = {LOTRRecipes.morgulRecipes, LOTRRecipes.urukRecipes, LOTRRecipes.angmarRecipes, LOTRRecipes.dolGuldurRecipes, LOTRRecipes.gundabadRecipes};
    private static List[] commonMorgulRecipes = {LOTRRecipes.morgulRecipes, LOTRRecipes.angmarRecipes, LOTRRecipes.dolGuldurRecipes};
    private static List[] commonElfRecipes = {LOTRRecipes.elvenRecipes, LOTRRecipes.woodElvenRecipes, LOTRRecipes.highElvenRecipes, LOTRRecipes.rivendellRecipes};

    public static void addMeiRecipes()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe (new ItemStack(MeiContents.itemResource, 1, 0), new Object[]{
                "BGB",
                "GRG",
                "BGB",
                'B', "ingotBronze",
                'G', "gearGate",
                'R', "blockRedstone"
        }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 0), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', Blocks.crafting_table,
                'C', "chestWood"
        }));

        LOTRRecipes.morgulRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 1), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.morgulTable,
                'C', "chestWood"
        }));

        LOTRRecipes.elvenRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 2), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.elvenTable,
                'C', "chestWood"
        }));

        LOTRRecipes.dwarvenRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 3), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.dwarvenTable,
                'C', "chestWood"
        }));

        LOTRRecipes.urukRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 4), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.urukTable,
                'C', "chestWood"
        }));

        LOTRRecipes.woodElvenRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 5), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.woodElvenTable,
                'C', "chestWood"
        }));

        LOTRRecipes.gondorianRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 6), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.gondorianTable,
                'C', "chestWood"
        }));

        LOTRRecipes.rohirricRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 7), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.rohirricTable,
                'C', "chestWood"
        }));

        LOTRRecipes.dunlendingRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 8), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.dunlendingTable,
                'C', "chestWood"
        }));

        LOTRRecipes.angmarRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 9), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.angmarTable,
                'C', "chestWood"
        }));

        LOTRRecipes.nearHaradRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 10), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.nearHaradTable,
                'C', "chestWood"
        }));

        LOTRRecipes.highElvenRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 11), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.highElvenTable,
                'C', "chestWood"
        }));

        LOTRRecipes.blueMountainsRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 12), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.blueDwarvenTable,
                'C', "chestWood"
        }));

        LOTRRecipes.rangerRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 13), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.rangerTable,
                'C', "chestWood"
        }));

        LOTRRecipes.dolGuldurRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 14), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.dolGuldurTable,
                'C', "chestWood"
        }));

        LOTRRecipes.gundabadRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable1, 1, 15), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.gundabadTable,
                'C', "chestWood"
        }));

        LOTRRecipes.halfTrollRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 0), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.halfTrollTable,
                'C', "chestWood"
        }));

        LOTRRecipes.dolAmrothRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 1), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.dolAmrothTable,
                'C', "chestWood"
        }));

        LOTRRecipes.moredainRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 2), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.moredainTable,
                'C', "chestWood"
        }));

        LOTRRecipes.tauredainRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 3), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.tauredainTable,
                'C', "chestWood"
        }));

        LOTRRecipes.daleRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 4), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.daleTable,
                'C', "chestWood"
        }));

        LOTRRecipes.dorwinionRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 5), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.dorwinionTable,
                'C', "chestWood"
        }));

        LOTRRecipes.hobbitRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 6), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.hobbitTable,
                'C', "chestWood"
        }));

        LOTRRecipes.rhunRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 7), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.rhunTable,
                'C', "chestWood"
        }));

        LOTRRecipes.rivendellRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 8), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.rivendellTable,
                'C', "chestWood"
        }));

        LOTRRecipes.umbarRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 9), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.umbarTable,
                'C', "chestWood"
        }));

        LOTRRecipes.gulfRecipes.add(new ShapedOreRecipe(new ItemStack(MeiContents.autoCraftingTable2, 1, 10), new Object[]{
                "STS",
                "SGS",
                "SCS",
                'S', "stone",
                'T', LOTRMod.gulfTable,
                'C', "chestWood"
        }));

    }

    public static void registerOreDictionary()
    {
        OreDictionary.registerOre("gearGate", LOTRMod.gateGear);
    }

    private static void addRecipeTo(List[] rList, IRecipe recipe)
    {
        for(List list : rList)
        {
            list.add(recipe);
        }
    }
}
