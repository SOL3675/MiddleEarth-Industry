package sol3675.middleearthindustry.references;

import cpw.mods.fml.common.ModMetadata;

public class ModInfo {
    public static final String MODID = "mei";
    public static final String MODNAME = "Middle-Earth Industry";
    public static final String VERSION = "0.2.0";
    public static final String DEPENDENCIES = "required-after:lotr;";

    public static final String TEXTUREPREFIX = "middleearthindustry:";
    public static final String MODLABEL = "MiddleEarthIndustry";

    public static void loadInfo(ModMetadata meta)
    {
        meta.modId = MODID;
        meta.name = MODNAME;
        meta.description = "Middle-Earth Industry is an addon to add some automation things to LotR Mod";
        meta.version = VERSION;
        meta.url = "";
        meta.updateUrl = "";
        meta.authorList.add("Coding: SOL3675, Artist: SOL3675");
        meta.credits = "";
        meta.logoFile = "assets/logo.png";
        meta.autogenerated = false;
    }
}
