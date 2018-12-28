package sol3675.middleearthindustry.common.tileentities;

public abstract class TileEntityMeiMachine extends TileEntityMieBase implements cofh.api.tileentity.IRedstoneControl
{
    public boolean isActive;
    protected boolean isPowered;
    protected boolean wasPowered;

    protected ControlMode redstoneMode = ControlMode.DISABLED;


}
