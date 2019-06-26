package sol3675.middleearthindustry.compat.ie;

import cpw.mods.fml.common.registry.GameRegistry;
import sol3675.middleearthindustry.common.blocks.BlockContainerMeiBase;
import sol3675.middleearthindustry.compat.ie.blocks.BlockIECompat;
import sol3675.middleearthindustry.compat.ie.recipes.IECompatRecipes;
import sol3675.middleearthindustry.compat.ie.tileentities.TileEntityCompressedThermoelectricGen;
import sol3675.middleearthindustry.compat.ie.tileentities.TileEntityDoubleCompressedThermoelectricGen;

public class IECompatContents {

    public static BlockContainerMeiBase blockIECompat;

    public static void preInit()
    {
        blockIECompat = new BlockIECompat();
        GameRegistry.registerTileEntity(TileEntityCompressedThermoelectricGen.class, "CommpressedThermoelectricGen");
        GameRegistry.registerTileEntity(TileEntityDoubleCompressedThermoelectricGen.class, "DoubleCompressedThermoelectricGen");
    }

    public static void postInit()
    {
        IECompatRecipes.addWirePressRecipes();
        IECompatRecipes.addGeneralRecipes();
    }
}
