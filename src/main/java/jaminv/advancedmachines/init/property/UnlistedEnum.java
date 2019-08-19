package jaminv.advancedmachines.init.property;

import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;

public class UnlistedEnum<V> implements IUnlistedProperty<V> {
	
	protected final String name;
	protected final Class<V> enumClass;
	
	public UnlistedEnum(String name, Class<V> enumClass) {
		this.name = name;
		this.enumClass = enumClass;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(V value) {
		return true;
	}

	@Override
	public Class<V> getType() {
		return enumClass;
	}

	@Override
	public String valueToString(V value) {
		return value.toString();
	}

}
