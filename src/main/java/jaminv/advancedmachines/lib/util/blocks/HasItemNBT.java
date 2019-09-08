package jaminv.advancedmachines.lib.util.blocks;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface HasItemNBT {
	public void readItemNBT(NBTTagCompound compound);
	public NBTTagCompound writeItemNBT(NBTTagCompound compound);
	
	public static void placeItem(World worldIn, BlockPos pos, ItemStack stack) {
		if (stack.hasTagCompound()) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof HasItemNBT) {
				((HasItemNBT)te).readItemNBT(stack.getTagCompound());
			}
		}
	}
	
	public static void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, Class blockClass) {
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof HasItemNBT)) { return; }
		
		HasItemNBT tile = (HasItemNBT)te;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt = tile.writeItemNBT(nbt);
		for (ItemStack drop : drops) {
			if ((drop.getItem() instanceof ItemBlock) && blockClass.isInstance(((ItemBlock)drop.getItem()).getBlock())) {
				drop.setTagCompound(nbt);
			}
		}
	}	
}
