package jaminv.advancedmachines.init.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

import jaminv.advancedmachines.objects.material.MaterialBase;
import jaminv.advancedmachines.objects.material.MaterialBase.MaterialRegistry;
import jaminv.advancedmachines.objects.material.MaterialBase.MaterialType;
import net.minecraft.block.properties.IProperty;
import scala.actors.threadpool.Arrays;

public class PropertyMaterial implements IProperty<MaterialBase> {
	
	protected final String name;
	protected final MaterialBase.MaterialType type;
	
	protected PropertyMaterial(String name, MaterialBase.MaterialType type) {
		this.name = name;
		this.type = type;
	}
	
    public static PropertyMaterial create(String name, MaterialBase.MaterialType type) {
        return new PropertyMaterial(name, type);
    }	

	@Override
	public Collection getAllowedValues() {
		return new ArrayList<>(Arrays.asList(MaterialBase.values(this.type)));
	}
	
	
	@Override
	public Class getValueClass() {
		return MaterialBase.class;
	}
	
	@Override
	public Optional parseValue(String value) {
		return Optional.of(MaterialRegistry.lookupName(this.type, value));
	}

	@Override
	public String getName(MaterialBase value) {
		return value.getName();
	}

	@Override
	public String getName() {
		return this.name;
	}
}
