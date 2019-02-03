package sol3675.middleearthindustry.compat.appeng.textures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import scala.actors.threadpool.Arrays;
import sol3675.middleearthindustry.references.ModInfo;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public enum BlockTextureManager
{
    BUS_COLOR(TextureTypes.Part, new String[]{"bus.color.border", "bus.color.light", "bus.color.side"}),
    MORGUL_CRAFTING_TERMINAL(TextureTypes.Part, new String[]{"meipart.craftingterm.morgul"});

    private enum TextureTypes
    {
        Block,
        Part;
    }

    public static final List<BlockTextureManager> ALLVALUES = Collections.unmodifiableList(Arrays.asList(BlockTextureManager.values()));
    private TextureTypes textureTypes;
    private String[] textureNames;
    private IIcon[] textures;

    private BlockTextureManager(final TextureTypes textureTypes, final String[] textureNames)
    {
        this.textureTypes = textureTypes;
        this.textureNames = textureNames;
        this.textures = new IIcon[this.textureNames.length];
    }

    public IIcon getTexture()
    {
        return this.textures[0];
    }

    public IIcon[] getTextures()
    {
        return this.textures;
    }

    public void registerTexture(final TextureMap textureMap)
    {
        if(textureMap.getTextureType() == 0)
        {
            String header = ModInfo.TEXTUREPREFIX;
            if(this.textureTypes == TextureTypes.Part)
            {
                header += "parts/";
            }
            for(int i = 0; i < this.textureNames.length; ++i)
            {
                this.textures[i] = textureMap.registerIcon(header + this.textureNames[i]);
            }
        }
    }
}
