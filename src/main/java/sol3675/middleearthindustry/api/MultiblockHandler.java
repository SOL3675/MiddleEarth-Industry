package sol3675.middleearthindustry.api;

import java.util.ArrayList;

public class MultiblockHandler {

    static ArrayList<IMultiblock> multiblocks = new ArrayList<IMultiblock>();

    public static void registerMultiblock(IMultiblock multiblock)
    {
        multiblocks.add(multiblock);
    }

    public static ArrayList<IMultiblock> getMultiblocks()
    {
        return multiblocks;
    }
}
