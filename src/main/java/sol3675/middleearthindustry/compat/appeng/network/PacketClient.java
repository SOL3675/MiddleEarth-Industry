package sol3675.middleearthindustry.compat.appeng.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import sol3675.middleearthindustry.util.Util;

public abstract class PacketClient extends PacketBase
{
    @SideOnly(Side.CLIENT)
    private final void preWrap()
    {
        this.player = Minecraft.getMinecraft().thePlayer;
        this.wrappedExecute();
    }

    @Override
    protected boolean includePlayerInStream()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected abstract void wrappedExecute();

    @Override
    public void execute()
    {
        if(Util.isClient())
        {
            this.preWrap();
        }
    }
}
