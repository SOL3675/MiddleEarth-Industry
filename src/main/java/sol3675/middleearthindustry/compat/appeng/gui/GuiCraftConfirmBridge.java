package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.client.gui.implementations.GuiCraftConfirm;
import appeng.core.localization.GuiText;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import sol3675.middleearthindustry.api.IAECraftingIssuerHost;

public class GuiCraftConfirmBridge extends GuiCraftConfirm
{
    protected GuiButton buttonCancel;
    protected EntityPlayer player;
    protected IAECraftingIssuerHost host;

    public GuiCraftConfirmBridge(final EntityPlayer player, final IAECraftingIssuerHost craftingHost)
    {
        super(player.inventory, craftingHost);
        this.player = player;
        this.host = craftingHost;
    }

    @Override
    protected void actionPerformed(final GuiButton button)
    {
        if(button == null)
        {
            return;
        }
        super.actionPerformed(button);
        if((button == this.buttonCancel) || (button.displayString == GuiText.Start.getLocal()))
        {
            this.host.launchGUI(this.player);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();

        for(int i = 0; i < this.buttonList.size(); i++)
        {
            Object button = this.buttonList.get(i);
            if(button == null)
            {
                this.buttonList.remove(i--);
            }
        }
        this.buttonList.add(this.buttonCancel = new GuiButton(0, this.guiLeft + 6, (this.guiTop + this.ySize) - 25, 50, 20, GuiText.Cancel.getLocal()));
    }
}
