package sol3675.middleearthindustry.common.tileentities;

import cofh.api.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import sol3675.middleearthindustry.config.MeiCfg;

import java.util.List;

public class TileEntityAutoCraftingTable extends TileEntityMeiMachine{
    public EnergyStorage energyStorage = new EnergyStorage(MeiCfg.AutocraftRequireRF ? 1000000 : 0);
    public ItemStack[] inventory = new ItemStack[9+1];
    public List<IRecipe> recipeList;
    public CrafterPatternInventory pattern;

    public TileEntityAutoCraftingTable()
    {
        recipeList = null;
        pattern = new CrafterPatternInventory(this);
    }

    public TileEntityAutoCraftingTable(List<IRecipe> recipeList)
    {
        this.recipeList = recipeList;
        pattern = new CrafterPatternInventory(this, recipeList);
    }
}
