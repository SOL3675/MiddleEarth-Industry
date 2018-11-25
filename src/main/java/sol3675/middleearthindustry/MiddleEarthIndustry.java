package sol3675.middleearthindustry;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import sol3675.middleearthindustry.common.CreativeTabMei;
import sol3675.middleearthindustry.proxy.CommonProxy;
import sol3675.middleearthindustry.references.ModInfo;

@Mod(modid = ModInfo.MODID, name = ModInfo.MODNAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES)

public class MiddleEarthIndustry {

    @Mod.Metadata(ModInfo.MODID)
    public static ModMetadata meta;

    @SidedProxy(clientSide = "sol3675.middleearthindustry.proxy.ClientProxy", serverSide = "sol3675.middleearthindustry.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static MiddleEarthIndustry instance = new MiddleEarthIndustry();

    public static final CreativeTabs TABMEI = new CreativeTabMei(ModInfo.MODLABEL);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModInfo.loadInfo(meta);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
