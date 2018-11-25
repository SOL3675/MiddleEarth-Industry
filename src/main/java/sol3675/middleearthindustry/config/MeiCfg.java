package sol3675.middleearthindustry.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class MeiCfg {

    public static void configurate(File cfgfile)
    {
        Configuration cfg = new Configuration(cfgfile);

        try
        {
            cfg.load();
        }
        catch (Exception e)
        {

        }
        finally
        {
            cfg.save();
        }
    }
}
