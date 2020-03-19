package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.client.gui.implementations.GuiCraftAmount;
import appeng.client.gui.widgets.GuiNumberBox;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.core.localization.GuiText;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import sol3675.middleearthindustry.api.IAECraftingIssuerHost;
import sol3675.middleearthindustry.compat.appeng.network.PacketServerConfirmCraftingJob;

import java.lang.reflect.Field;

public class GuiCraftAmountBridge extends GuiCraftAmount
{
    protected IAECraftingIssuerHost host;
    protected GuiTabButton buttonReturnToTerminalHost;
    protected EntityPlayer player;
    protected GuiButton buttonNext;
    protected GuiNumberBox amountToCraft;

    public GuiCraftAmountBridge(final EntityPlayer player, final IAECraftingIssuerHost craftingHost)
    {
        super(player.inventory, craftingHost);
        this.host = craftingHost;
        this.player = player;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if(button == this.buttonReturnToTerminalHost)
        {
            this.host.launchGUI(this.player);
        }
        else if(button == this.buttonNext)
        {
            try
            {
                long amount = Long.parseLong(this.amountToCraft.getText());
                PacketServerConfirmCraftingJob.sendConfirmAutoCraft(this.player, amount, isShiftKeyDown());
            }
            catch (final NumberFormatException e)
            {
                this.amountToCraft.setText("1");
            }
        }
        else
        {
            super.actionPerformed(button);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();

        ItemStack myIcon = this.host.getIcon();

        this.buttonReturnToTerminalHost = new GuiTabButton(this.guiLeft + 154, this.guiTop, myIcon, myIcon.getDisplayName(), itemRender);
        this.buttonList.add(this.buttonReturnToTerminalHost);

        for( Object buttonObj : this.buttonList )
        {
            if( buttonObj instanceof GuiButton )
            {
                GuiButton button = (GuiButton)buttonObj;
                if( button.displayString == GuiText.Next.getLocal() )
                {
                    this.buttonNext = button;
                    break;
                }
            }
        }

        Field atcField;
        try
        {
            atcField = GuiCraftAmount.class.getDeclaredField( "amountToCraft" );
            atcField.setAccessible( true );
            this.amountToCraft = (GuiNumberBox)atcField.get( this );
        }
        catch( Exception e )
        {
        }
    }
}
