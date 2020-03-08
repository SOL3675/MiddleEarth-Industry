package sol3675.middleearthindustry.proxy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import sol3675.middleearthindustry.MiddleEarthIndustry;
import sol3675.middleearthindustry.common.MeiContents;
import sol3675.middleearthindustry.common.gui.GuiHandler;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.network.MessageTileSync;
import sol3675.middleearthindustry.network.NetworkHandler;
import sol3675.middleearthindustry.references.ModInfo;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event)
    {
        MeiCfg.configurate(event.getSuggestedConfigurationFile());
        MeiContents.preInit();
    }

    public void init(FMLInitializationEvent event)
    {
        MeiContents.init();
        NetworkHandler.registerPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(MiddleEarthIndustry.instance, new GuiHandler());
        if(Loader.isModLoaded("Waila"))
        {
            FMLInterModComms.sendMessage("Waila", "register", "sol3675.middleearthindustry.compat.waila.MeiWailaProvider.callbackRegister");
        }
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        MeiContents.postInit();
    }
}
