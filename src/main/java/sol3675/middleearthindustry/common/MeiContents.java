package sol3675.middleearthindustry.common;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import sol3675.middleearthindustry.common.blocks.*;
import sol3675.middleearthindustry.common.items.ItemMeiBase;
import sol3675.middleearthindustry.common.items.ItemMultiblockBuilder;
import sol3675.middleearthindustry.common.tileentities.TileEntityAutoCraftingTable;
import sol3675.middleearthindustry.compat.ic2.IC2CompatContents;
import sol3675.middleearthindustry.compat.ie.IECompatContents;
import sol3675.middleearthindustry.compat.te.TECompatContents;
import sol3675.middleearthindustry.config.MeiCfg;
import sol3675.middleearthindustry.references.ModInfo;

public class MeiContents {

    public static BlockContainerMeiBase autoCraftingTable1;
    public static BlockContainerMeiBase autoCraftingTable2;
    public static BlockMeiBase stoneGen;

    public static Item iconMei;
    public static ItemMeiBase itemMultiblockBuilder;
    public static ItemMeiBase itemUpgrade;
    public static ItemMeiBase itemResource;
    public static ItemMeiBase itemMisc;

    public static void preInit()
    {
        //Add Blocks
        autoCraftingTable1 = new BlockAutoCraftingTable1();
        autoCraftingTable2 = new BlockAutoCraftingTable2();
        stoneGen = new BlockStoneGen();

        //Add Items
        iconMei = new Item().setUnlocalizedName("iconMei").setTextureName(ModInfo.TEXTUREPREFIX + "iconMei");
        GameRegistry.registerItem(iconMei, "iconMei");

        itemMultiblockBuilder = new ItemMultiblockBuilder();
        itemUpgrade = new ItemMeiBase("upgrade", 64, "upgradeSpeed", "upgradeEfficient");
        itemResource = new ItemMeiBase("resource", 64, "gearBox", "plateMithril", "plateGalvorn", "plateOrcSteel", "plateDwarvenSteel", "plateUrukSteel", "plateMorgulSteel", "plateBlueDwarvenSteel", "plateBlackUrukSteel", "plateElvenSteel", "plateGildedIron");

        GameRegistry.registerTileEntity(TileEntityAutoCraftingTable.class, "AutoCraftingTable");

        if(MeiCfg.randomMiscMaterials)
        {
            itemMisc = new ItemMeiBase("misc", 64, "plateNickel", "plateInvar", "plateSilver", "platePlatinum", "plateElectrum", "plateSignalum", "plateLumium", "plateVoid", "wireCopper", "wireElectrum", "wireAluminum", "wireSteel", "moldWire", "lensLogic", "lensCalc", "lensEngineer", "lensSilicon");
        }

        if(Loader.isModLoaded("ImmersiveEngineering") && MeiCfg.IECompatModule)
        {
            IECompatContents.preInit();
        }

        if(Loader.isModLoaded("ThermalExpansion") && MeiCfg.TECompatModule)
        {
            TECompatContents.preInit();
        }
    }

    public static void init()
    {
        //Add Multiblocks
        //MultiblockHandler.registerMultiblock(MultiblockName.instance);
        MeiRecipes.registerOreDictionary();
        if(MeiCfg.randomMiscMaterials)
        {
            MeiRecipes.registerMiscOreDictionary();
        }
    }

    public static void postInit()
    {
        MeiRecipes.addMeiRecipes();
        if(Loader.isModLoaded("ImmersiveEngineering") && MeiCfg.IECompatModule)
        {
            IECompatContents.postInit();
        }
        if(Loader.isModLoaded("IC2"))
        {
            IC2CompatContents.postInit();
        }
    }
}
