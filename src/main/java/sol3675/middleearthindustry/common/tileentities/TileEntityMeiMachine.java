package sol3675.middleearthindustry.common.tileentities;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import sol3675.middleearthindustry.config.MeiCfg;

public abstract class TileEntityMeiMachine extends TileEntityMieBase implements IEnergyReceiver
{
    public int[] sideConfig = {0, 0, 0, 0, 0, 0};
    public EnergyStorage energyStorage = new EnergyStorage(MeiCfg.AutocraftRequireRF ? 1000000 : 0);


}
