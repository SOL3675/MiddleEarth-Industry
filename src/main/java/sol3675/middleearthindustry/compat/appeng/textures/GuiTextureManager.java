package sol3675.middleearthindustry.compat.appeng.textures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import sol3675.middleearthindustry.references.ModInfo;

@SideOnly(Side.CLIENT)
public enum GuiTextureManager
{
    CRAFTING_TERMINAL_MEI("craftingtermmei");

    private ResourceLocation texture;

    private GuiTextureManager(final String textureName)
    {
        this.texture = new ResourceLocation(ModInfo.MODID, "textures/gui/" + textureName + ".png");
    }

    public ResourceLocation getTexture()
    {
        return this.texture;
    }
}
