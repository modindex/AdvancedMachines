package jaminv.advancedmachines.lib.util.blocks.property;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedBoolean implements IUnlistedProperty<Boolean> {
	
	protected final String name;
	
	public UnlistedBoolean(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Boolean value) {
		return true;
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

	@Override
	public String valueToString(Boolean value) {
		return value.toString();
	}

}
