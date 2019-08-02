package jaminv.advancedmachines.init.property;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedTileEntity<V> implements IUnlistedProperty<V> {
	
	protected final String name;
	private final Class<V> teClass;
	
	public UnlistedTileEntity(String name, Class<V> teClass) {
		this.name = name;
		this.teClass = teClass;
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
		return teClass;
	}

	@Override
	public String valueToString(V value) {
		return value.toString();
	}

}
