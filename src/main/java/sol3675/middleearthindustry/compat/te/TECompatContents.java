package sol3675.middleearthindustry.compat.te;

import sol3675.middleearthindustry.common.items.ItemMeiBase;
import sol3675.middleearthindustry.compat.te.items.UpgradeKit;

public class TECompatContents
{

    public static ItemMeiBase upgradeKit;

    public static void preInit()
    {
        upgradeKit = new UpgradeKit();
    }
}
