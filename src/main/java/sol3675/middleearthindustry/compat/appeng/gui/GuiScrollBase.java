package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.GuiScrollbar;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;

public abstract class GuiScrollBase extends GuiBaseAE
{
    public class ScrollbarParams
    {
        int scrollbarPosX, scrollbarPosY, scrollbarHeight, scrollbarVerticalBound;

        public ScrollbarParams(final int x, final int y, final int height)
        {
            this.scrollbarPosX = x;
            this.scrollbarPosY = y;
            this.setHeight(height);
        }

        void setHeight(final int height)
        {
            this.scrollbarHeight = height;
            this.scrollbarVerticalBound = this.scrollbarHeight + this.scrollbarPosY;
        }
    }

    protected final GuiScrollbar scrollbar;
    private AEBaseGui aeGuiBridge;
    private boolean isScrollBarHeld = false;
    private int scrollHeldPrevY = 0;
    private ScrollbarParams scrollParams;

    public GuiScrollBase(final Container container)
    {
        super(container);
        this.scrollbar = new GuiScrollbar();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.scrollbar.draw(this.aeGuiBridge);
    }

    protected abstract ScrollbarParams getScrollbarParameters();

    @Override
    protected void keyTyped(final char key, final int keyID)
    {
        if(keyID == Keyboard.KEY_HOME)
        {
            this.scrollbar.click(this.aeGuiBridge, this.scrollParams.scrollbarPosX + 1, this.scrollParams.scrollbarPosY + 1);
            this.scrollbar.wheel(1);
            this.onScrollbarMoved();
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
    {
        //TODO
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected abstract void onMouseWheel(int deltaZ, int mouseX, int mouseY);

    protected abstract void onScrollbarMoved();

    protected void setScrollbarHeight(final int newHeight)
    {
        this.scrollParams.setHeight(newHeight);
        this.scrollbar.setHeight(newHeight);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float mouseButton)
    {
        super.drawScreen(mouseX, mouseY, mouseButton);
        //TODO
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        //TODO
    }

    @Override
    public void initGui()
    {
        super.initGui();
        //TODO
    }
}
