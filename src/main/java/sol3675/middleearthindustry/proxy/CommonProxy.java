package sol3675.middleearthindustry.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import sol3675.middleearthindustry.common.MeiContents;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event)
    {
        MeiContents.registerPreInit();
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
