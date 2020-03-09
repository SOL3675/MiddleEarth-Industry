package sol3675.middleearthindustry.compat.appeng.textures;

import net.minecraft.util.ResourceLocation;

public enum AEStateIcons
{
    REDSTONE_LOW (0, 0),
    REDSTONE_HIGH (16, 0),
    REDSTONE_PULSE (32, 0),
    REDSTONE_IGNORE (48, 0),

    VIEW_CELL_BACKGROUND (224, 64),
    UPGRADE_CARD_BACKGROUND (240, 208),
    ME_CELL_BACKGROUND (240, 0),
    PATTERN_CELL_BACKGROUND (240, 128),

    REGULAR_BUTTON (240, 240),
    TAB_BUTTON (208, 0, 22, 22),

    SORT_MODE_ALPHABETIC (0, 64),
    SORT_MODE_AMOUNT (16, 64),
    SORT_MODE_MOD (80, 64),
    SORT_MODE_INVTWEAK (64, 64),

    SORT_DIR_ASC (0, 48),
    SORT_DIR_DEC (16, 48),

    VIEW_TYPE_STORED (0, 16),
    VIEW_TYPE_ALL (32, 16),
    VIEW_TYPE_CRAFT (48, 16),

    TERM_STYLE_TALL (0, 208),
    TERM_STYLE_SMALL (16, 208),

    SEARCH_MODE_AUTO (48, 32),
    SEARCH_MODE_MANUAL (64, 32),
    SEARCH_MODE_NEI_AUTO (80, 32),
    SEARCH_MODE_NEI_MANUAL (96, 32),

    WRENCH (32, 64),
    DISABLED (0, 128),
    ENABLED (16, 128),
    SAVE (0, 176),
    DELETE (0, 192),
    CLEAR_GRID (96, 0),
    ARROW_DOWN (128, 0);

    public static final ResourceLocation AE_STATES_TEXTURE = new ResourceLocation("appliedenergistics2", "textures/guis/states.png");

    public static final int STANDARD_ICON_SIZE = 16;

    private int minU, minV;
    private int width, height;

    private AEStateIcons(final int u, final int v)
    {
        this(u, v, STANDARD_ICON_SIZE, STANDARD_ICON_SIZE);
    }

    private AEStateIcons(final int u, final int v, final int width, final int height)
    {
        this.minU = u;
        this.minV = v;
        this.width = width;
        this.height = height;
    }

    public int getU()
    {
        return this.minU;
    }

    public int getV()
    {
        return this.minV;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public ResourceLocation getTexture()
    {
        return AEStateIcons.AE_STATES_TEXTURE;
    }

}
