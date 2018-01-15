package jaminv.advancedmachines.objects.items.material;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Optional;

import jaminv.advancedmachines.objects.items.material.MaterialBase.MaterialRegistry;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.IStringSerializable;
import scala.actors.threadpool.Arrays;

public class PropertyMaterial implements IProperty<MaterialBase> {
	
	MaterialBase.MaterialType type;
	
	protected PropertyMaterial(MaterialBase.MaterialType type) {
		this.type = type;
	}
	
    public static PropertyMaterial create(MaterialBase.MaterialType type) {
        return new PropertyMaterial(type);
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
		return null;
	}	

}
