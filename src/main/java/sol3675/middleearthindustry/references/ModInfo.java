package sol3675.middleearthindustry.references;

import cpw.mods.fml.common.ModMetadata;

public class ModInfo {
    public static final String MODID = "mei";
    public static final String MODNAME = "Middle-Earth Industry";
    public static final String VERSION = "0.0.6";
    public static final String DEPENDENCIES = "";

    public static final String TEXTUREPREFIX = "middleearthindustry:";
    public static final String MODLABEL = "MiddleEarthIndustry";

    public static void loadInfo(ModMetadata meta)
    {
        meta.modId = MODID;
        meta.name = MODNAME;
        meta.description = "TODO";
        meta.version = VERSION;
        meta.url = "TODO";
        meta.updateUrl = "TODO";
        meta.authorList.add("Coding: SOL3675");
        meta.credits = "TODO";
        meta.logoFile = "";
        meta.autogenerated = false;
    }
}
