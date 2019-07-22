package jaminv.advancedmachines.objects.blocks.machine.multiblock.face;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.Vec3i;
import scala.actors.threadpool.Arrays;

public enum MachineParent {
	
	NONE(0, "none"),
	FURNACE(1, "furnace"),
	GRINDER(2, "grinder"),
	PURIFIER(3, "purifier"),
	ALLOY(4, "alloy");
	
    private final int index;
    private final String name;
    
    private static Map<String, MachineParent> val = new HashMap<String, MachineParent>();

    private MachineParent(int index, String name) {
    	this.index = index;
    	this.name = name;
    }
    
    static {
        for (MachineParent face : values()) {
            val.put(face.name, face);
        }
    }    
    
    public int getIndex() { return index; }
    public String getName() { return name; }
    
    public static MachineParent lookup(String name) {
    	return val.get(name);
    }
}
