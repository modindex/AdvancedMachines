package jaminv.advancedmachines.machine;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.util.ModConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
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
	
	public static TextureAtlasSprite getParticleTexture(String type, String variant) {
		if (variant.equals("normal")) { return RawTextures.get(type + ".basic.all"); }
		return RawTextures.get(type + "." + variant + ".all");
	}
}
