package sol3675.middleearthindustry.compat.ie.tileentities;

import blusunrize.immersiveengineering.api.energy.ThermoelectricHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityThermoelectricGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import sol3675.middleearthindustry.config.MeiCfg;

public class TileEntityCompressedThermoelectricGen extends TileEntityThermoelectricGen
{
    @Override
    public void updateEntity()
    {
        if(!worldObj.isRemote) {
            int energy = 0;
            for (ForgeDirection fd : new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.SOUTH, ForgeDirection.EAST})
                if (!worldObj.isAirBlock(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ) && !worldObj.isAirBlock(xCoord + fd.getOpposite().offsetX, yCoord + fd.getOpposite().offsetY, zCoord + fd.getOpposite().offsetZ)) {
                    int temp0 = getTemperature(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ);
                    int temp1 = getTemperature(xCoord + fd.getOpposite().offsetX, yCoord + fd.getOpposite().offsetY, zCoord + fd.getOpposite().offsetZ);
                    if (temp0 > -1 && temp1 > -1) {
                        int diff = Math.abs(temp0 - temp1);
                        energy += (int) (Math.sqrt(diff) / 2 * MeiCfg.compressedThermoelectricGenOutput);
                    }
                }
            outputEnergy(energy);
        }
    }

    int getTemperature(int x, int y, int z)
    {
        Fluid f = getFluid(x,y,z);
        if(f!=null)
            return f.getTemperature(worldObj, x, y, z);
        return ThermoelectricHandler.getTemperature(worldObj.getBlock(x,y,z), worldObj.getBlockMetadata(x,y,z));
    }
    Fluid getFluid(int x, int y, int z)
    {
        Block b = worldObj.getBlock(x, y, z);
        Fluid f = FluidRegistry.lookupFluidForBlock(b);
        if(f==null && b instanceof BlockDynamicLiquid && worldObj.getBlockMetadata(x, y, z)==0)
            if(b.getMaterial().equals(Material.water))
                f = FluidRegistry.WATER;
            else if(b.getMaterial().equals(Material.lava))
                f = FluidRegistry.LAVA;
        if(b instanceof IFluidBlock && !((IFluidBlock)b).canDrain(worldObj, x, y, z))
            return null;
        if(b instanceof BlockStaticLiquid && worldObj.getBlockMetadata(x, y, z)!=0)
            return null;
        if(f==null)
            return null;
        return f;
    }

}