package sol3675.middleearthindustry.compat.ie;

import sol3675.middleearthindustry.common.blocks.BlockContainerMeiBase;
import sol3675.middleearthindustry.compat.ie.blocks.BlockIECompat;

public class IECompatContents {

    public static BlockContainerMeiBase blockIECompat;

    public static void preInit()
    {
        blockIECompat = new BlockIECompat();
    }
}
