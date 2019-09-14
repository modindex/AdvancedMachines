package jaminv.advancedmachines.lib.fluid;

import java.util.function.Consumer;

import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class BucketHandler implements DialogBucketToggle.Toggle {
	
	protected Consumer<IOState> callback;
	
	public BucketHandler setCallback(Consumer<IOState> callback) {
		this.callback = callback;
		return this;
	}

	protected IOState bucketState = IOState.INPUT;
	@Override public IOState getBucketState() { return bucketState; }

	@Override
	public void setBucketState(IOState state) {
		bucketState = state;
		if (callback != null) { callback.accept(state); }
	}
	
	public boolean handleBucket(IItemHandler inventory, int slotIndex, IFluidHandler tank) {
		ItemStack stack = inventory.getStackInSlot(slotIndex);
		if (stack == null) { return false; }

		FluidActionResult result = null;
		if (bucketState == IOState.INPUT) {
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
	
	public boolean onBlockActivate(EntityPlayer player, EnumHand hand, IFluidHandler handler) {
		if (FluidUtil.getFluidHandler(player.getHeldItem(hand)) == null) { return false; }
		FluidUtil.interactWithFluidHandler(player, hand, handler);
		return true;
	}
}
