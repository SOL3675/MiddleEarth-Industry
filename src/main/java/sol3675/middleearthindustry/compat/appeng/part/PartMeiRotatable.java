package sol3675.middleearthindustry.compat.appeng.part;

import appeng.api.config.SecurityPermissions;
import appeng.api.parts.PartItemStack;
import appeng.util.Platform;
import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;

import java.io.IOException;

public abstract class PartMeiRotatable extends PartMeiBase
{
    private static final String NBT_KEY_ROT_DIR = "partRotation";
    private byte renderRotation = 0;

    public PartMeiRotatable(final PartsEnum associatedPart, final SecurityPermissions ... interactionPermissions)
    {
        super(associatedPart, interactionPermissions);
    }

    protected void rotateRenderer(final RenderBlocks renderer, final boolean reset)
    {
        int rot = (reset ? 0 : this.renderRotation);
        renderer.uvRotateBottom = renderer.uvRotateEast = renderer.uvRotateNorth = renderer.uvRotateSouth =renderer.uvRotateTop = renderer.uvRotateWest = rot;
    }

    @Override
    public boolean onActivate(EntityPlayer player, Vec3 position) {
        TileEntity tile = this.getHostTile();
        if(!player.isSneaking() && Platform.isWrench(player, player.inventory.getCurrentItem(), tile.xCoord, tile.yCoord, tile.zCoord))
        {
            if(FMLCommonHandler.instance().getEffectiveSide().isServer())
            {
                if((this.renderRotation > 3) || (this.renderRotation < 0))
                {
                    this.renderRotation = 0;
                }
                switch (this.renderRotation)
                {
                    case 0:
                        this.renderRotation = 1;
                        break;
                    case 1:
                        this.renderRotation = 3;
                        break;
                    case 2:
                        this.renderRotation = 0;
                        break;
                    case 3:
                        this.renderRotation = 2;
                        break;
                }
                this.markForUpdate();
                this.markForSave();
            }
            return true;
        }
        return super.onActivate(player, position);
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        if(data.hasKey(PartMeiRotatable.NBT_KEY_ROT_DIR))
        {
            this.renderRotation = data.getByte(PartMeiRotatable.NBT_KEY_ROT_DIR);
        }
    }

    @Override
    public boolean readFromStream(ByteBuf stream) throws IOException
    {
        boolean redraw = false;
        redraw |= super.readFromStream(stream);
        byte streamRot = stream.readByte();
        if(this.renderRotation != streamRot)
        {
            this.renderRotation = streamRot;
            redraw |= true;
        }
        return redraw;
    }

    @Override
    public void writeToNBT(NBTTagCompound data, PartItemStack saveType) {
        super.writeToNBT(data, saveType);
        if((saveType == PartItemStack.World) && (this.renderRotation != 0))
        {
            data.setByte(PartMeiRotatable.NBT_KEY_ROT_DIR, this.renderRotation);
        }
    }

    @Override
    public void writeToStream(ByteBuf stream) throws IOException
    {
        super.writeToStream(stream);
        stream.writeByte(this.renderRotation);
    }
}
