package sol3675.middleearthindustry.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import sol3675.middleearthindustry.common.MeiContents;
import sol3675.middleearthindustry.config.MeiCfg;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event)
    {
        MeiCfg.configurate(event.getSuggestedConfigurationFile());
        MeiContents.preInit();
    }

    public void init(FMLInitializationEvent event)
    {
        MeiContents.init();
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
