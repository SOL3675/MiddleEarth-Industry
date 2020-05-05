package sol3675.middleearthindustry.compat.appeng.gui;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.GuiScrollbar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sol3675.middleearthindustry.references.Constant;

@SideOnly(Side.CLIENT)
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
        if( ((mouseX - this.guiLeft) >= this.scrollParams.scrollbarPosX) && ((mouseX - this.guiLeft) <= this.scrollParams.scrollbarPosX + this.scrollbar.getWidth()) &&
                ((mouseY - this.guiTop) >= this.scrollParams.scrollbarPosY) && ((mouseY - this.guiTop) <= this.scrollParams.scrollbarPosY + this.scrollbar.getHeight()) )
        {
            this.isScrollBarHeld = true;
            this.scrollHeldPrevY = mouseY;
            this.scrollbar.click(this.aeGuiBridge, mouseX - this.guiLeft, mouseY - this.guiTop);
            return;
        }
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
        if(this.isScrollBarHeld)
        {
            if(Mouse.isButtonDown(Constant.MOUSE_BUTTON_LEFT))
            {
                return;
            }
            boolean correctForZero = false;

            this.scrollHeldPrevY = mouseY;
            int repY = mouseY - this.guiTop;

            if(repY > this.scrollParams.scrollbarVerticalBound)
            {
                repY = this.scrollParams.scrollbarVerticalBound;
            }
            else if(repY <= this.scrollParams.scrollbarPosY)
            {
                repY = this.scrollParams.scrollbarPosY;
                correctForZero = true;
            }
            this.scrollbar.click(this.aeGuiBridge, this.scrollParams.scrollbarPosX + 1, repY);

            if(correctForZero)
            {
                this.scrollbar.wheel(1);
            }
            this.onScrollbarMoved();
        }
        else
        {
            this.isScrollBarHeld = false;
        }
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();

        int deltaZ = Mouse.getEventDWheel();
        if(deltaZ != 0)
        {
            int mouseX = (Mouse.getEventX() * this.width) / this.mc.displayWidth;
            int mouseY = ((Mouse.getEventY() * this.height) / this.mc.displayHeight) - 1;
            this.onMouseWheel(deltaZ, mouseX, mouseY);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.scrollParams = this.getScrollbarParameters();
        this.scrollbar.setLeft(this.scrollParams.scrollbarPosX).setTop(this.scrollParams.scrollbarPosY).setHeight(this.scrollParams.scrollbarHeight);
        this.scrollbar.setRange(0, 0, 1);
        this.aeGuiBridge = new AEBaseGui(this.inventorySlots)
        {
            @Override
            public void bindTexture(final String file)
            {
                this.bindTexture("appliedenergistics2", file);
            }

            @Override
            public void bindTexture(final String base, final String file)
            {
                GuiScrollBase.this.mc.getTextureManager().bindTexture(new ResourceLocation(base, "textures/" + file));
            }

            @Override
            public void drawFG(final int i, final int i1, final int i2, final int i3)
            {
            }

            @Override
            public void drawBG(final int i, final int i1, final int i2, final int i3)
            {
            }

            @Override
            public void drawTexturedModalRect(final int posX, final int posY, final int sourceOffsetX, final int sourceOffsetY, final int width, final int height)
            {
                GuiScrollBase.this.drawTexturedModalRect(posX, posY, sourceOffsetX, sourceOffsetY, width, height);
            }

            @Override
            protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
            {
            }
        };
    }
}
