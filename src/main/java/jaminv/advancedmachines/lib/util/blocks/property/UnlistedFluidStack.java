package jaminv.advancedmachines.lib.util.blocks.property;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

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
		ToStringHelper helper = MoreObjects.toStringHelper(this);
		helper.add("fluid", value.getFluid().getName());
		helper.add("amount", value.amount);
		if (value.tag != null) {
			helper.add("nbt", value.tag.toString());
		}
		return helper.toString();
	}

}
