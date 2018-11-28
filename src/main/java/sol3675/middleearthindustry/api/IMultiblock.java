package sol3675.middleearthindustry.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMultiblock {

    public String getMultiblockName();

    public boolean isTrigger(Block block, int meta);

    public boolean createMultiblock(World world, int x, int y, int z, int side, EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public boolean overwriteBlockRender(ItemStack itemstack, int iterator);

}
