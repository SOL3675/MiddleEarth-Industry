package sol3675.middleearthindustry.compat.ic2.recipes;

import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.common.MeiContents;
import sol3675.middleearthindustry.config.MeiCfg;

public class IC2CompatRecipes
{
    public static void addIC2Recipes()
    {
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotMithril"), null, new ItemStack(MeiContents.itemResource, 1, 1));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotGalvorn"), null, new ItemStack(MeiContents.itemResource, 1, 2));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotOrcSteel"), null, new ItemStack(MeiContents.itemResource, 1, 3));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotDwarvenSteel"), null, new ItemStack(MeiContents.itemResource, 1, 4));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotUrukSteel"), null, new ItemStack(MeiContents.itemResource, 1, 5));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotMorgulSteel"), null, new ItemStack(MeiContents.itemResource, 1, 6));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBlueDwarvenSteel"), null, new ItemStack(MeiContents.itemResource, 1, 7));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBlackUrukSteel"), null, new ItemStack(MeiContents.itemResource, 1, 8));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotElvenSteel"), null, new ItemStack(MeiContents.itemResource, 1, 9));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotGildedIron"), null, new ItemStack(MeiContents.itemResource, 1, 10));

        if(MeiCfg.randomMiscMaterials)
        {
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotNickel"), null, new ItemStack(MeiContents.itemMisc, 1, 0));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotInvar"), null, new ItemStack(MeiContents.itemMisc, 1, 1));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotSilver"), null, new ItemStack(MeiContents.itemMisc, 1, 2));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotPlatinum"), null, new ItemStack(MeiContents.itemMisc, 1, 3));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotElectrum"), null, new ItemStack(MeiContents.itemMisc, 1, 4));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotSignalum"), null, new ItemStack(MeiContents.itemMisc, 1, 5));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotLumium"), null, new ItemStack(MeiContents.itemMisc, 1, 6));
            Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotVoid"), null, new ItemStack(MeiContents.itemMisc, 1, 7));
        }
    }
}
