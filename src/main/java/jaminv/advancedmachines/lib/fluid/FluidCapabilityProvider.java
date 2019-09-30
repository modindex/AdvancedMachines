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

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(new IFluidHandlerItem() {

			@Override
			public IFluidTankProperties[] getTankProperties() {

				return new IFluidTankProperties[] { new FluidTankProperties(container.getFluid(stack), container.getCapacity(stack), canFill, canDrain) };
			}

			@Override
			public int fill(FluidStack resource, boolean doFill) {

				return container.fill(stack, resource, doFill);
			}

			@Nullable
			@Override
			public FluidStack drain(FluidStack resource, boolean doDrain) {

				if (FluidHelper.isFluidEqual(resource, container.getFluid(stack))) {
					return container.drain(stack, resource.amount, doDrain);
				}
				return null;
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
