package sol3675.middleearthindustry.compat.appeng.part;

import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.RenderBlocks;
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

import java.io.IOException;
import java.util.List;
import java.util.Random;

public abstract class PartMeiBase implements IPart, IGridHost, IActionHost, IPowerChannelState
{
    @Override
    public void addToWorld()
    {
    }

    @Override
    public int cableConnectionRenderTo()
    {
        return 0;
    }

    @Override
    public boolean canBePlacedOn(BusSupport busSupport)
    {
        return false;
    }

    @Override
    public boolean canConnectRedstone()
    {
        return false;
    }

    @Override
    public IGridNode getActionableNode() {
        return null;
    }

    @Override
    public void getBoxes(IPartCollisionHelper iPartCollisionHelper)
    {
    }

    @Override
    public IIcon getBreakingTexture()
    {
        return null;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection)
    {
        return null;
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
        return null;
    }

    @Override
    public ItemStack getItemStack(PartItemStack partItemStack)
    {
        return null;
    }

    @Override
    public int getLightLevel()
    {
        return 0;
    }

    @Override
    public boolean isActive()
    {
        return false;
    }

    @Override
    public boolean isLadder(EntityLivingBase entityLivingBase)
    {
        return false;
    }

    @Override
    public boolean isPowered()
    {
        return false;
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
    public boolean onActivate(EntityPlayer entityPlayer, Vec3 vec3)
    {
        return false;
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
    public void onPlacement(EntityPlayer entityPlayer, ItemStack itemStack, ForgeDirection forgeDirection)
    {
    }

    @Override
    public void randomDisplayTick(World world, int i, int i1, int i2, Random random)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
    }

    @Override
    public boolean readFromStream(ByteBuf byteBuf)
    {
        return false;
    }

    @Override
    public void writeToStream(ByteBuf byteBuf)
    {
    }

    @Override
    public void removeFromWorld()
    {
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
    }

    @Override
    public void setPartHostInfo(ForgeDirection forgeDirection, IPartHost iPartHost, TileEntity tileEntity)
    {
    }


}
