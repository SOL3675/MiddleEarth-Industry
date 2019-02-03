package sol3675.middleearthindustry.compat.appeng.part;

import appeng.api.config.SecurityPermissions;

public abstract class PartMeiTermBase extends PartMeiBase
{
    private static final String NBT_KEY_ROT_DIR = "partRotation";
    private byte renderRotation = 0;

    public PartMeiTermBase(final PartsEnum associatedPart, final SecurityPermissions... interactionPermissions)
    {
        super(associatedPart, interactionPermissions);
    }
    

}
