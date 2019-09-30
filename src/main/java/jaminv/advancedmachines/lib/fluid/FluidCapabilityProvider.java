package jaminv.advancedmachines.lib.fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidCapabilityProvider implements ICapabilityProvider {
	
	final ItemStack stack;
	final FluidContainerItem container;
	
	public FluidCapabilityProvider(ItemStack stack, FluidContainerItem container) {
		this.stack = stack;
		this.container = container;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(new IFluidHandlerItem() {

			@Override
			public IFluidTankProperties[] getTankProperties() {
				return container.getTankProperties(stack);
			}

			@Override
			public int fill(FluidStack resource, boolean doFill) {
				return container.fill(stack, resource, doFill);
			}

			@Nullable
			@Override
			public FluidStack drain(FluidStack resource, boolean doDrain) {
				return container.drain(stack, resource, doDrain);
			}

			@Nullable
			@Override
			public FluidStack drain(int maxDrain, boolean doDrain) {
				return container.drain(stack, maxDrain, doDrain);
			}

			@Nonnull
			@Override
			public ItemStack getContainer() {
				return stack;
			}
		});
	}

}
