package sol3675.middleearthindustry.compat.ie.tileentities;

import sol3675.middleearthindustry.config.MeiCfg;

public class TileEntityDoubleCompressedThermoelectricGen extends TileEntityCompressedThermoelectricGen
{
    @Override
    double getMagnification()
    {
        return MeiCfg.doubleCompressedThermoelectricGenOutput;
    }
}
