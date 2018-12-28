package sol3675.middleearthindustry.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class MeiCfg {

    private static final String CATEGORY_VERSION = "~version~";
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_COMPAT_IE = "Compat_IE";

    public static int configVer = 1;
    public static boolean AutocraftRequireRF = true;

    public static boolean IECompatModule = true;
    public static double compressedThermoelectricGenOutput = 8d;
    public static double doubleCompressedThermoelectricGenOutput = 64d;

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
            AutocraftRequireRF = cfg.getBoolean("AutocraftRequireRF", CATEGORY_GENERAL, true, "If it true, Autocraft require RF.");

            IECompatModule = cfg.getBoolean("IECompat", CATEGORY_COMPAT_IE, true, "Set false to disable IE compat module.");
            compressedThermoelectricGenOutput = cfg.get(CATEGORY_COMPAT_IE, "compressedThermoelectricGenOutput", 8d, "Output magnification of Compressed Thermoelectric Generator.", 1d, 1024d).getDouble(compressedThermoelectricGenOutput);
            doubleCompressedThermoelectricGenOutput = cfg.get(CATEGORY_COMPAT_IE, "doubleCompressedThermoelectricGenOutput", 64d, "Output magnification of Compressed Thermoelectric Generator.", 1d, 1024d).getDouble(doubleCompressedThermoelectricGenOutput);
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
