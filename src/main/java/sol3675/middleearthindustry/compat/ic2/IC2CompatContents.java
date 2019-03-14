package sol3675.middleearthindustry.compat.ic2;

import sol3675.middleearthindustry.compat.ic2.recipes.IC2CompatRecipes;

public class IC2CompatContents
{
    public static void postInit()
    {
        IC2CompatRecipes.addIC2Recipes();
    }
}
