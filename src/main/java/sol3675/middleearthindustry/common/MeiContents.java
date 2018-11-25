package sol3675.middleearthindustry.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import sol3675.middleearthindustry.references.ModInfo;

public class MeiContents {

    public static Item iconMei;

    public static void registerPreInit()
    {
        iconMei = new Item().setUnlocalizedName("iconMei").setTextureName(ModInfo.TEXTUREPREFIX + ":iconMei");
        GameRegistry.registerItem(iconMei, "iconMei");
    }

}
