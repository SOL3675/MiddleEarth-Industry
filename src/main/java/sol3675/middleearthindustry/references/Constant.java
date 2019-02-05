package sol3675.middleearthindustry.references;

import lotr.common.LOTRFaction;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class Constant {

    //block texture helper
    public static final String[] BLOCK_SIDE_4D = {"_bottom", "_top", "_front", "_side"};

    //GUI ID
    public static final int GUI_AUTO_CRAFTING_TABLE = 0;
    public static final int SIDE_CONFIG_ITEM_INPUT = 1;
    public static final int SIDE_CONFIG_ITEM_OUTPUT = 2;
    public static final int GUI_CRAFTING_TERM = 3;

    public enum TableFaction
    {
        Morgul,
        Elven,
        Dwarven,
        Uruk,
        WoodElven,
        Gondorian,
        Rohirric,
        Dunlending,
        Angmar,
        NearHarad,
        HighElven,
        BlueDwarven,
        Ranger,
        DolGuldur,
        Gundabad,
        HalfTroll,
        DolAmroth,
        Moredain,
        Tauredain,
        Dale,
        Dorwinion,
        Hobbit,
        Rhun,
        Rivendell,
        Umbar,
        Gulf
    }

    public static LOTRFaction getFaction(TableFaction tableFaction)
    {
        switch (tableFaction) {
            case Morgul:
                return LOTRFaction.MORDOR;
            case Elven:
                return LOTRFaction.GALADHRIM;
            case Dwarven:
                return LOTRFaction.DWARF;
            case Uruk:
                return LOTRFaction.URUK_HAI;
            case WoodElven:
                return LOTRFaction.WOOD_ELF;
            case Gondorian:
                return LOTRFaction.GONDOR;
            case Rohirric:
                return LOTRFaction.ROHAN;
            case Dunlending:
                return LOTRFaction.DUNLAND;
            case Angmar:
                return LOTRFaction.ANGMAR;
            case NearHarad:
                return LOTRFaction.NEAR_HARAD;
            case HighElven:
                return LOTRFaction.HIGH_ELF;
            case BlueDwarven:
                return LOTRFaction.BLUE_MOUNTAINS;
            case Ranger:
                return LOTRFaction.RANGER_NORTH;
            case DolGuldur:
                return LOTRFaction.DOL_GULDUR;
            case Gundabad:
                return LOTRFaction.GUNDABAD;
            case HalfTroll:
                return LOTRFaction.HALF_TROLL;
            case DolAmroth:
                return LOTRFaction.GONDOR;
            case Moredain:
                return LOTRFaction.MOREDAIN;
            case Tauredain:
                return LOTRFaction.TAUREDAIN;
            case Dale:
                return LOTRFaction.DALE;
            case Dorwinion:
                return LOTRFaction.DORWINION;
            case Hobbit:
                return LOTRFaction.HOBBIT;
            case Rhun:
                return LOTRFaction.RHUN;
            case Rivendell:
                return LOTRFaction.HIGH_ELF;
            case Umbar:
                return LOTRFaction.NEAR_HARAD;
            case Gulf:
                return LOTRFaction.NEAR_HARAD;
            default:
                return null;

        }
    }

    public static List<IRecipe> getRecipe(TableFaction tableFaction)
    {
        switch (tableFaction) {
            case Morgul:
                return LOTRRecipes.morgulRecipes;
            case Elven:
                return LOTRRecipes.elvenRecipes;
            case Dwarven:
                return LOTRRecipes.dwarvenRecipes;
            case Uruk:
                return LOTRRecipes.urukRecipes;
            case WoodElven:
                return LOTRRecipes.woodElvenRecipes;
            case Gondorian:
                return LOTRRecipes.gondorianRecipes;
            case Rohirric:
                return LOTRRecipes.rohirricRecipes;
            case Dunlending:
                return LOTRRecipes.dunlendingRecipes;
            case Angmar:
                return LOTRRecipes.angmarRecipes;
            case NearHarad:
                return LOTRRecipes.nearHaradRecipes;
            case HighElven:
                return LOTRRecipes.highElvenRecipes;
            case BlueDwarven:
                return LOTRRecipes.blueMountainsRecipes;
            case Ranger:
                return LOTRRecipes.rangerRecipes;
            case DolGuldur:
                return LOTRRecipes.dolGuldurRecipes;
            case Gundabad:
                return LOTRRecipes.gundabadRecipes;
            case HalfTroll:
                return LOTRRecipes.halfTrollRecipes;
            case DolAmroth:
                return LOTRRecipes.dolAmrothRecipes;
            case Moredain:
                return LOTRRecipes.moredainRecipes;
            case Tauredain:
                return LOTRRecipes.tauredainRecipes;
            case Dale:
                return LOTRRecipes.daleRecipes;
            case Dorwinion:
                return LOTRRecipes.dorwinionRecipes;
            case Hobbit:
                return LOTRRecipes.hobbitRecipes;
            case Rhun:
                return LOTRRecipes.rhunRecipes;
            case Rivendell:
                return LOTRRecipes.rivendellRecipes;
            case Umbar:
                return LOTRRecipes.umbarRecipes;
            case Gulf:
                return LOTRRecipes.gulfRecipes;
            default:
                return null;
        }
    }

    public static String getUnlocalizedTableName(TableFaction tableFaction)
    {
        switch (tableFaction) {
            case Morgul:
                return "morgulCraftTable";
            case Elven:
                return "elvenCraftTable";
            case Dwarven:
                return "dwarvenCraftTable";
            case Uruk:
                return "urukCraftTable";
            case WoodElven:
                return "woodElvenCraftTable";
            case Gondorian:
                return "gondorianCraftTable";
            case Rohirric:
                return "rohirricCraftTable";
            case Dunlending:
                return "dunlendingCraftTable";
            case Angmar:
                return "angmarCraftTable";
            case NearHarad:
                return "nearHaradCraftTable";
            case HighElven:
                return "highElvenCraftTable";
            case BlueDwarven:
                return "blueDwarvenCraftTable";
            case Ranger:
                return "rangerCraftTable";
            case DolGuldur:
                return "dolGuldurCraftTable";
            case Gundabad:
                return "gundabadCraftTable";
            case HalfTroll:
                return "halfTrollCraftTable";
            case DolAmroth:
                return "dolAmrothCraftTable";
            case Moredain:
                return "moredainCraftTable";
            case Tauredain:
                return "tauredainCraftTable";
            case Dale:
                return "daleCraftTable";
            case Dorwinion:
                return "dorwinionCraftTable";
            case Hobbit:
                return "hobbitCraftTable";
            case Rhun:
                return "rhunCraftTable";
            case Rivendell:
                return "rivendellCraftTable";
            case Umbar:
                return "umbarCraftTable";
            case Gulf:
                return "gulfCraftTable";
            default:
                return null;
        }
    }
}
