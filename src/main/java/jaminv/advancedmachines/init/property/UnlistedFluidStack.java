package jaminv.advancedmachines.init.property;

import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;

public class UnlistedFluidStack implements IUnlistedProperty<FluidStack> {
	
	protected final String name;
	
	public UnlistedFluidStack(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(FluidStack value) {
		return true;
	}

	@Override
	public Class<FluidStack> getType() {
		return FluidStack.class;
	}

	@Override
	public String valueToString(FluidStack value) {
		return value.toString();
	}

}
