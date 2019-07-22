package jaminv.advancedmachines.objects.blocks.machine.multiblock.face;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

import jaminv.advancedmachines.util.material.MaterialBase.MaterialRegistry;
import net.minecraft.block.properties.IProperty;
import scala.actors.threadpool.Arrays;

public class PropertyMachineFace implements IProperty<MachineFace> {

	protected final String name;
	
	protected PropertyMachineFace(String name) {
		this.name = name;
	}
	
    public static PropertyMachineFace create(String name) {
        return new PropertyMachineFace(name);
    }	

	@Override
	public Collection getAllowedValues() {
		return new ArrayList<>(Arrays.asList(MachineFace.values()));
	}
	
	
	@Override
	public Class getValueClass() {
		return MachineFace.class;
	}
	
	@Override
	public Optional parseValue(String value) {
		return Optional.of(MachineFace.lookup(value));
	}

	@Override
	public String getName(MachineFace value) {
		return value.getName();
	}

	@Override
	public String getName() {
		return this.name;
	}
}