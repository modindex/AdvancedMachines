package jaminv.advancedmachines.machine;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import jaminv.advancedmachines.util.ModConfig;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class MachineHelper {
	
	public static boolean handleBucket(IItemHandler inventory, int slotIndex, IFluidHandler tank, IOState state) {
		ItemStack stack = inventory.getStackInSlot(slotIndex);
		if (stack == null) { return false; }

		FluidActionResult result = null;
		if (state == IOState.INPUT) {
			result = FluidUtil.tryEmptyContainer(stack, tank, Integer.MAX_VALUE, null, true);
		} else {
			result = FluidUtil.tryFillContainer(stack, tank, Integer.MAX_VALUE, null, true);
		}
		
		if (result != null && result.success) {
			inventory.extractItem(slotIndex, stack.getCount(), false);
			inventory.insertItem(slotIndex, result.getResult(), false);
			return true;
		}
		return false;
	}
	
	/* Properties */
	
	public static BlockStateContainer.Builder addCommonProperties(BlockStateContainer.Builder builder) {
		return builder.add(Properties.EXPANSION_VARIANT)
		.add(Properties.BORDER_TOP, Properties.BORDER_BOTTOM) 
		.add(Properties.BORDER_NORTH, Properties.BORDER_SOUTH)
		.add(Properties.BORDER_EAST, Properties.BORDER_WEST);
	}
	
	public static IExtendedBlockState withCommonProperties(IExtendedBlockState ext, VariantExpansion variant, MultiblockBorders borders) {
		return (IExtendedBlockState) ext.withProperty(Properties.EXPANSION_VARIANT, variant)
        	.withProperty(Properties.BORDER_TOP, borders.getTop()).withProperty(Properties.BORDER_BOTTOM, borders.getBottom())
        	.withProperty(Properties.BORDER_NORTH, borders.getNorth()).withProperty(Properties.BORDER_SOUTH, borders.getSouth())
        	.withProperty(Properties.BORDER_EAST, borders.getEast()).withProperty(Properties.BORDER_WEST, borders.getWest());
	}
	
	public static EnumFacing getExtendedFacing(IBlockState state) {
		if (!(state instanceof IExtendedBlockState)) { return null; }
		return ((IExtendedBlockState)state).getValue(Properties.FACING);
	}	
}
