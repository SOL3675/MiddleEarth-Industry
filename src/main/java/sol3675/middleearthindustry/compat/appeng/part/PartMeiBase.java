package sol3675.middleearthindustry.compat.appeng.part;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.events.MENetworkChannelChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import sol3675.middleearthindustry.compat.appeng.grid.GridBlockMeiBase;
import sol3675.middleearthindustry.compat.appeng.textures.BlockTextureManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class PartMeiBase implements IPart, IGridHost, IActionHost, IPowerChannelState
{
    private final static String NBT_KEY_OWNER = "Owner";
    protected final static int INVENTORY_OVERLAY_COLOR = AEColor.White.whiteVariant;
    protected final static int ACTIVE_FACE_BRIGHTNESS = 0xD000D0;
    protected final static int ACTIVE_TERMINAL_LIGHT_LEVEL = 9;
    private final SecurityPermissions[] interactPermissions;

    private IPartHost host;
    private TileEntity hostTile;
    private ForgeDirection cableSide;
    private boolean isActive;
    private boolean isPowered;
    private final GridBlockMeiBase gridBlock;
    private IGridNode node;
    private int ownerID = -1;
    public final ItemStack associatedItem;

    public PartMeiBase(final PartsEnum associatedPart, final SecurityPermissions... interactPermissions)
    {
        this.associatedItem = associatedPart.getStack();

        if((interactPermissions != null) && (interactPermissions.length > 0))
        {
            this.interactPermissions = interactPermissions;
        }
        else
        {
            this.interactPermissions = null;
        }

        if(FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            this.gridBlock = new GridBlockMeiBase(this);
        }
        else
        {
            this.gridBlock = null;
        }

    }

    private void updateStatus()
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            return;
        }
        if(this.node != null)
        {
            boolean currentlyActive = this.node.isActive();
            if(currentlyActive != this.isActive)
            {
                this.isActive = currentlyActive;
                this.host.markForUpdate();
            }
        }
        this.onNeighborChanged();
    }

    protected boolean doesPlayerHavePermission(final EntityPlayer player, final SecurityPermissions permissions)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            return false;
        }
        ISecurityGrid grid = this.gridBlock.getSecurityGrid();
        if(grid == null)
        {
            return false;
        }
        return grid.hasPermission(player, permissions);
    }

    protected boolean doesPlayerHavePermission(final int playerID, final SecurityPermissions permissions)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            return false;
        }
        ISecurityGrid grid = this.gridBlock.getSecurityGrid();
        if(grid == null)
        {
            return false;
        }
        return grid.hasPermission(playerID, permissions);
    }

    protected TileEntity getFacingTile()
    {
        if(this.hostTile == null)
        {
            return null;
        }
        World world = this.hostTile.getWorldObj();
        int x = this.hostTile.xCoord;
        int y = this.hostTile.yCoord;
        int z = this.hostTile.zCoord;

        return world.getTileEntity(x + this.cableSide.offsetX, y + this.cableSide.offsetY, z + this.cableSide.offsetZ);
    }

    public void setupPartFromItem(final ItemStack itemPart)
    {
        if(itemPart.hasTagCompound())
        {
            this.readFromNBT(itemPart.getTagCompound());
        }
    }

    public final DimensionalCoord getLocation()
    {
        return new DimensionalCoord(this.hostTile.getWorldObj(), this.hostTile.xCoord, this.hostTile.yCoord, this.hostTile.zCoord);
    }

    public Object getClientGuiElement(final EntityPlayer player)
    {
        return null;
    }

    public Object getServerGuiElement(final EntityPlayer player)
    {
        return null;
    }

    public GridBlockMeiBase getGridBlock()
    {
        return this.gridBlock;
    }

    public final IPartHost getHost()
    {
        return this.host;
    }

    public final TileEntity getHostTile()
    {
        return this.hostTile;
    }

    public abstract double getIdlePowerUsage();

    public ForgeDirection getSide()
    {
        return this.cableSide;
    }

    public String getUnlocalizedName()
    {
        return this.associatedItem.getUnlocalizedName() + ".name";
    }

    public final boolean isPartUseableByPlayer(final EntityPlayer player)
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            return false;
        }
        if((this.hostTile == null) || (this.host == null))
        {
            return false;
        }
        TileEntity tile = this.hostTile.getWorldObj().getTileEntity(this.hostTile.xCoord, this.hostTile.yCoord, this.hostTile.zCoord);
        if(tile == null)
        {
            return false;
        }
        if(!(player.getDistanceSq(this.hostTile.xCoord + 0.5D, this.hostTile.yCoord + 0.5D, this.hostTile.zCoord + 0.5D) <= 64.0D))
        {
            return false;
        }
        if(this.host.getPart(this.cableSide) != this)
        {
            return false;
        }
        if(this.interactPermissions != null)
        {
            ISecurityGrid grid = this.gridBlock.getSecurityGrid();
            if(grid == null)
            {
                return false;
            }
            for(SecurityPermissions permission : this.interactPermissions)
            {
                if(!grid.hasPermission(player, permission))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isReceivingRedstonePower()
    {
        if(this.host != null)
        {
            return this.host.hasRedstone(this.cableSide);
        }
        return false;
    }

    public final void markForSave()
    {
        if(this.host != null)
        {
            this.host.markForSave();
        }
    }

    public final void markForUpdate()
    {
        if(this.host != null)
        {
            this.host.markForUpdate();
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderInventoryBusLights(final IPartRenderHelper helper, final RenderBlocks renderer)
    {
        helper.setInvColor(0xFFFFFF);
        IIcon busColorTexture = BlockTextureManager.BUS_COLOR.getTextures()[0];
        IIcon sideTexture = BlockTextureManager.BUS_COLOR.getTextures()[2];
        helper.setTexture(busColorTexture, busColorTexture, sideTexture, sideTexture, busColorTexture, busColorTexture);
        helper.renderInventoryBox(renderer);
        Tessellator.instance.setBrightness(0xD000D0);
        helper.setInvColor(AEColor.Transparent.blackVariant);
        IIcon lightTexture = BlockTextureManager.BUS_COLOR.getTextures()[1];
        helper.renderInventoryFace(lightTexture, ForgeDirection.UP, renderer);
        helper.renderInventoryFace(lightTexture, ForgeDirection.DOWN, renderer);
        helper.renderInventoryFace(lightTexture, ForgeDirection.NORTH, renderer);
        helper.renderInventoryFace(lightTexture, ForgeDirection.EAST, renderer);
        helper.renderInventoryFace(lightTexture, ForgeDirection.SOUTH, renderer);
        helper.renderInventoryFace(lightTexture, ForgeDirection.WEST, renderer);
    }

    @SideOnly(Side.CLIENT)
    public void renderStaticBusLights(final int x, final int y, final int z, final IPartRenderHelper helper, final RenderBlocks renderer)
    {
        IIcon busColorTexture = BlockTextureManager.BUS_COLOR.getTextures()[0];
        IIcon sideTexture = BlockTextureManager.BUS_COLOR.getTextures()[2];
        helper.setTexture( busColorTexture, busColorTexture, sideTexture, sideTexture, busColorTexture, busColorTexture );
        helper.renderBlock( x, y, z, renderer );

        if(this.isActive())
        {
            Tessellator.instance.setBrightness(0xD000D0);
            Tessellator.instance.setColorOpaque_I(this.host.getColor().blackVariant);
        }
        else
        {
            Tessellator.instance.setColorOpaque_I(0);
        }

        IIcon lightTexture = BlockTextureManager.BUS_COLOR.getTextures()[1];
        helper.renderFace( x, y, z, lightTexture, ForgeDirection.UP, renderer );
        helper.renderFace( x, y, z, lightTexture, ForgeDirection.DOWN, renderer );
        helper.renderFace( x, y, z, lightTexture, ForgeDirection.NORTH, renderer );
        helper.renderFace( x, y, z, lightTexture, ForgeDirection.EAST, renderer );
        helper.renderFace( x, y, z, lightTexture, ForgeDirection.SOUTH, renderer );
        helper.renderFace( x, y, z, lightTexture, ForgeDirection.WEST, renderer );
    }

    @MENetworkEventSubscribe
    public final void setPower(final MENetworkPowerStatusChange event)
    {
        this.updateStatus();
    }

    @MENetworkEventSubscribe
    public void updateChannels(final MENetworkChannelChanged event)
    {
        this.updateStatus();
    }

    @Override
    public void addToWorld()
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            return;
        }
        this.node = AEApi.instance().createGridNode(this.gridBlock);
        this.node.setPlayerID(this.ownerID);
        if((this.hostTile != null) && (this.host != null) && (this.hostTile.getWorldObj() != null))
        {
            try
            {
                this.node.updateState();
            }
            catch (Exception e)
            {
                //ERROR
            }
        }
        this.updateStatus();
    }

    @Override
    public int cableConnectionRenderTo()
    {
        return 0;
    }

    @Override
    public boolean canBePlacedOn(BusSupport busSupport)
    {
        return busSupport == BusSupport.CABLE;
    }

    @Override
    public boolean canConnectRedstone()
    {
        return false;
    }

    @Override
    public IGridNode getActionableNode() {
        return this.node;
    }

    @Override
    public abstract void getBoxes(IPartCollisionHelper iPartCollisionHelper);

    @Override
    public abstract IIcon getBreakingTexture();

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection)
    {
        return AECableType.SMART;
    }

    @Override
    public void getDrops(List<ItemStack> list, boolean b)
    {
    }

    @Override
    public IGridNode getExternalFacingNode()
    {
        return null;
    }

    @Override
    public IGridNode getGridNode()
    {
        return this.node;
    }

    @Override
    public IGridNode getGridNode(final ForgeDirection direction)
    {
        return this.node;
    }

    @Override
    public ItemStack getItemStack(PartItemStack type)
    {
        ItemStack itemStack = this.associatedItem.copy();
        if(type == PartItemStack.Wrench)
        {
            NBTTagCompound itemNBT = new NBTTagCompound();
            this.writeToNBT(itemNBT, PartItemStack.Wrench);
            if(!itemNBT.hasNoTags())
            {
                itemStack.setTagCompound(itemNBT);
            }
        }
        return itemStack;
    }

    @Override
    public abstract int getLightLevel();

    @Override
    public boolean isActive()
    {
        if(FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            if(this.node != null)
            {
                this.isActive = this.node.isActive();
            }
            else
            {
                this.isActive = false;
            }
        }
        return this.isActive;
    }

    @Override
    public boolean isLadder(EntityLivingBase entityLivingBase)
    {
        return false;
    }

    @Override
    public boolean isPowered()
    {
        try
        {
            if(FMLCommonHandler.instance().getEffectiveSide().isServer() && (this.gridBlock != null))
            {
                IEnergyGrid grid = this.gridBlock.getEnergyGrid();
                if(grid != null)
                {
                    this.isPowered = grid.isNetworkPowered();
                }
                else this.isPowered = false;
            }
        }
        catch (Exception e){}
        return this.isPowered;
    }

    @Override
    public int isProvidingStrongPower()
    {
        return 0;
    }

    @Override
    public int isProvidingWeakPower()
    {
        return 0;
    }

    @Override
    public boolean isSolid()
    {
        return false;
    }

    @Override
    public boolean onActivate(EntityPlayer player, Vec3 position)
    {
        if(player.isSneaking())
        {
            return false;
        }
        if(FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            //TODO
            //openGui
        }
        return true;
    }

    @Override
    public boolean onShiftActivate(EntityPlayer entityPlayer, Vec3 vec3)
    {
        return false;
    }

    @Override
    public void onEntityCollision(Entity entity)
    {
    }

    @Override
    public void onNeighborChanged()
    {
    }

    @Override
    public void onPlacement(EntityPlayer player, ItemStack held, ForgeDirection side)
    {
        this.ownerID = AEApi.instance().registries().players().getID(player.getGameProfile());
    }

    @Override
    public void randomDisplayTick(World world, int i, int i1, int i2, Random random)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound data)
    {
        if(data.hasKey(PartMeiBase.NBT_KEY_OWNER))
        {
            this.ownerID = data.getInteger(PartMeiBase.NBT_KEY_OWNER);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        this.writeToNBT(nbtTagCompound, PartItemStack.World);
    }

    public void writeToNBT(final NBTTagCompound data, final PartItemStack saveType)
    {
        if(saveType == PartItemStack.World)
        {
            data.setInteger(PartMeiBase.NBT_KEY_OWNER, this.ownerID);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean readFromStream(ByteBuf stream)throws IOException
    {
        boolean oldActive = this.isActive;
        boolean oldPowered = this.isPowered;
        this.isActive = stream.readBoolean();
        this.isPowered = stream.readBoolean();
        return ((oldActive != this.isActive) || (oldPowered != this.isPowered));
    }

    @Override
    public void writeToStream(ByteBuf stream)throws IOException
    {
        stream.writeBoolean(this.isActive());
        stream.writeBoolean(this.isPowered());
    }

    @Override
    public void removeFromWorld()
    {
        if(this.node != null)
        {
            this.node.destroy();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderDynamic(double v, double v1, double v2, IPartRenderHelper iPartRenderHelper, RenderBlocks renderBlocks)
    {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventory(IPartRenderHelper iPartRenderHelper, RenderBlocks renderBlocks)
    {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderStatic(int i, int i1, int i2, IPartRenderHelper iPartRenderHelper, RenderBlocks renderBlocks)
    {
    }

    @Override
    public boolean requireDynamicRender()
    {
        return false;
    }

    @Override
    public void securityBreak()
    {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(this.getItemStack(PartItemStack.Break));
        this.getDrops(drops, false);
        appeng.util.Platform.spawnDrops(this.hostTile.getWorldObj(), this.hostTile.xCoord, this.hostTile.yCoord, this.hostTile.zCoord, drops);
        this.host.removePart(this.cableSide, false);
    }

    @Override
    public void setPartHostInfo(ForgeDirection side, IPartHost host, TileEntity tileEntity)
    {
        this.cableSide = side;
        this.host = host;
        this.hostTile = tileEntity;
    }


}
