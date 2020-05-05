package sol3675.middleearthindustry.proxy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import sol3675.middleearthindustry.compat.appeng.textures.BlockTextureManager;
import sol3675.middleearthindustry.config.MeiCfg;

public class ClientProxy extends CommonProxy
{
    public ClientProxy()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @SubscribeEvent
    public void registerTextures(final TextureStitchEvent.Pre event)
    {
        if(Loader.isModLoaded("appliedenergistics2") && MeiCfg.AE2CompatModule)
        {
            for(BlockTextureManager texture : BlockTextureManager.ALLVALUES)
            {
                texture.registerTexture(event.map);
            }
        }
    }
}
