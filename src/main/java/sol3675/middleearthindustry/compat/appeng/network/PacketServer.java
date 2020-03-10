package sol3675.middleearthindustry.compat.appeng.network;

public abstract class PacketServer extends PacketBase
{
    @Override
    protected boolean includePlayerInStream()
    {
        return true;
    }
}
