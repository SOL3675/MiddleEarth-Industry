package sol3675.middleearthindustry.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class MeiCfg {

    private static final String CATEGORY_VERSION = "~version~";
    private static final String CATEGORY_COMPAT_IE = "Compat_IE";

    public static int configVer = 1;
    public static boolean IECompatModule = true;
    public static double compressedThermoelectricGenOutput = 8d;

    public static void configurate(File cfgfile)
    {
        Configuration cfg = new Configuration(cfgfile);

        try
        {
            cfg.load();

            if(configVer < 1)
            {
                //configUpdater
            }

            configVer = cfg.getInt("ConfigVersion", CATEGORY_VERSION, 1, 0, Integer.MAX_VALUE, "This is config version. Do not edit manually!");
            IECompatModule = cfg.getBoolean("IECompat", CATEGORY_COMPAT_IE, true, "False to disable IE compat module.");
            compressedThermoelectricGenOutput = cfg.get(CATEGORY_COMPAT_IE, "compressedThermoelectricGenOutput", 8d, "Output magnification of Compressed Thermoelectric Generator.", 1d, 1024d).getDouble(compressedThermoelectricGenOutput);
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
