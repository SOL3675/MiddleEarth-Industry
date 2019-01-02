package sol3675.middleearthindustry.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
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
import sol3675.middleearthindustry.references.ModInfo;

public class CommonProxy {

    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);

    public void preInit(FMLPreInitializationEvent event)
    {
        MeiCfg.configurate(event.getSuggestedConfigurationFile());
        MeiContents.preInit();
    }

    public void init(FMLInitializationEvent event)
    {
        MeiContents.init();
        int messageId = 0;
        packetHandler.registerMessage(MessageTileSync.Handler.class, MessageTileSync.class, ++messageId, Side.SERVER);
        NetworkRegistry.INSTANCE.registerGuiHandler(MiddleEarthIndustry.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
