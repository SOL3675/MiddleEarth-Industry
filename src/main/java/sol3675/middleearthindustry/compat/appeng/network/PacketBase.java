package sol3675.middleearthindustry.compat.appeng.network;

import appeng.api.parts.IPartHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import com.google.common.base.Charsets;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.compat.appeng.part.PartMeiBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class PacketBase implements IMessage
{
    class ConfigurableGZIPOutputStream extends GZIPOutputStream
    {
        public ConfigurableGZIPOutputStream(final OutputStream out) throws IOException
        {
            super(out);
        }
        public void setLevel(final int level)
        {
            this.def.setLevel(level);
        }
    }

    private static final int COMPRESSED_BUFFER_SIZE = ( 1024 * 1024 ) * 1;

    public EntityPlayer player;
    protected byte mode;
    protected boolean useCompression;

    public PacketBase()
    {
        this.player = null;
        this.mode = -1;
        this.useCompression = false;
    }

    @SideOnly(Side.CLIENT)
    private static World getClientWorld()
    {
        return Minecraft.getMinecraft().theWorld;
    }

    protected static IAEItemStack readAEItemStack(final ByteBuf stream)
    {
        IAEItemStack itemStack;
        try
        {
            itemStack = AEItemStack.loadItemStackFromPacket(stream);
            return itemStack;
        }
        catch (IOException e)
        {
        }
        return null;
    }

    protected static ItemStack readItemStack(final ByteBuf stream)
    {
        return ByteBufUtils.readItemStack(stream);
    }

    protected static PartMeiBase readPart(final ByteBuf stream)
    {
        ForgeDirection side = ForgeDirection.getOrientation(stream.readInt());
        IPartHost host = (IPartHost)PacketBase.readTileEntity(stream);
        return (PartMeiBase) host.getPart(side);
    }

    protected static EntityPlayer readPlayer(final ByteBuf stream)
    {
        EntityPlayer player = null;
        if(stream.readBoolean())
        {
            World playerWorld = PacketBase.readWorld(stream);
            player = playerWorld.getPlayerEntityByName(PacketBase.readString(stream));
        }
        return player;
    }

    protected static String readString(final ByteBuf stream)
    {
        byte[] stringBytes = new byte[stream.readInt()];
        stream.readBytes(stringBytes);
        return new String(stringBytes, Charsets.UTF_8);
    }

    protected static TileEntity readTileEntity(final ByteBuf stream)
    {
        World world = PacketBase.readWorld(stream);
        return world.getTileEntity(stream.readInt(), stream.readInt(), stream.readInt());
    }

    protected static World readWorld(final ByteBuf stream)
    {
        World world = DimensionManager.getWorld(stream.readInt());
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            if(world == null)
            {
                world = PacketBase.getClientWorld();
            }
        }
        return world;
    }

    protected static void writeAEItemStack(final IAEItemStack itemStack, final ByteBuf stream)
    {
        if(itemStack != null)
        {
            try
            {
                itemStack.writeToPacket(stream);
            }
            catch (IOException e)
            {
            }
        }
    }

    protected static void writeItemStack(final ItemStack itemStack, final ByteBuf stream)
    {
        ByteBufUtils.writeItemStack(stream, itemStack);
    }

    protected static void writePart(final PartMeiBase part, final ByteBuf stream)
    {
        stream.writeInt(part.getSide().ordinal());
        PacketBase.writeTileEntity(part.getHost().getTile(), stream);
    }

    protected static void writePlayer(final EntityPlayer player, final ByteBuf stream)
    {
        boolean validPlayer = (player != null);
        stream.writeBoolean(validPlayer);
        if(validPlayer)
        {
            PacketBase.writeWorld(player.worldObj, stream);
            PacketBase.writeString(player.getCommandSenderName(), stream);
        }
    }

    protected static void writeString(final String string, final ByteBuf stream)
    {
        byte[] stringBytes = string.getBytes(Charsets.UTF_8);
        stream.writeInt(stringBytes.length);
        stream.writeBytes(stringBytes);
    }

    protected static void writeTileEntity(final TileEntity entity, final ByteBuf stream)
    {
        PacketBase.writeWorld(entity.getWorldObj(), stream);
        stream.writeInt(entity.xCoord);
        stream.writeInt(entity.yCoord);
        stream.writeInt(entity.zCoord);
    }

    protected static void writeWorld(final World world, final ByteBuf stream)
    {
        stream.writeInt(world.provider.dimensionId);
    }

    private void fromCompressedBytes(final ByteBuf packetStream)
    {
        ByteBuf decompressedStream = Unpooled.buffer(PacketBase.COMPRESSED_BUFFER_SIZE);
        try (
            InputStream inStream = new InputStream()
            {
                @Override
                public int read() throws IOException
                {
                    if(packetStream.readableBytes() <= 0)
                    {
                        return -1;
                    }
                    return packetStream.readByte() & 0xFF;
                }
            };
            GZIPInputStream decompressor = new GZIPInputStream(inStream))
        {
            byte[] holding = new byte[512];
            while (decompressor.available() != 0)
            {
                int bytesRead = decompressor.read(holding);
                if(bytesRead > 0)
                {
                    decompressedStream.writeBytes(holding, 0, bytesRead);
                }
            }
            decompressor.close();
            decompressedStream.readerIndex(0);
            this.readData(decompressedStream);
        }
        catch (IOException e)
        {
        }
    }

    private void toCompressedBytes(final ByteBuf packetStream)
    {
        ByteBuf streamToCompress = Unpooled.buffer(PacketBase.COMPRESSED_BUFFER_SIZE);
        this.writeData(streamToCompress);
        try(
                OutputStream outStream = new OutputStream()
                {
                    @Override
                    public void write(int b) throws IOException
                    {
                        packetStream.writeByte(b & 0xFF);
                    }
                };
                ConfigurableGZIPOutputStream compressor = new ConfigurableGZIPOutputStream(outStream)
                )
        {
            compressor.setLevel(Deflater.BEST_COMPRESSION);
            compressor.write(streamToCompress.array(), 0, streamToCompress.writerIndex());
            compressor.close();
        }
        catch (IOException e)
        {
        }
    }

    protected abstract boolean includePlayerInStream();

    protected abstract void readData(ByteBuf stream);

    protected abstract void writeData(ByteBuf stream);

    public abstract void execute();

    @Override
    public void fromBytes(final ByteBuf stream)
    {
        this.mode = stream.readByte();
        if(this.includePlayerInStream())
        {
            this.player = PacketBase.readPlayer(stream);
        }
        this.useCompression = stream.readBoolean();
        if(this.useCompression)
        {
            this.fromCompressedBytes(stream);
        }
        else
        {
            this.readData(stream);
        }
    }

    @Override
    public void toBytes(final ByteBuf stream)
    {
        stream.writeByte(this.mode);
        if(this.includePlayerInStream())
        {
            PacketBase.writePlayer(this.player, stream);
        }
        stream.writeBoolean(this.useCompression);
        if(this.useCompression)
        {
            this.toCompressedBytes(stream);
        }
        else
        {
            this.writeData(stream);
        }
    }
}
