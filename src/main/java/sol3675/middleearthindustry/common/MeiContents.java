package sol3675.middleearthindustry.common;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import sol3675.middleearthindustry.api.MultiblockHandler;
import sol3675.middleearthindustry.compat.ie.IECompatContents;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.references.ModInfo;

public class MeiContents {

    public static Item iconMei;

    public static void preInit()
    {
        //Add Items
        iconMei = new Item().setUnlocalizedName("iconMei").setTextureName(ModInfo.TEXTUREPREFIX + ":iconMei");
        GameRegistry.registerItem(iconMei, "iconMei");

        if(Loader.isModLoaded(ImmersiveEngineering.MODID) && MeiCfg.IECompatModule)
        {
            IECompatContents.preInit();
        }
    }

    public static void init()
    {
        //Add Multiblocks
        //MultiblockHandler.registerMultiblock(MultiblockName.instance);
    }

}
