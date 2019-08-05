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

import jaminv.advancedmachines.util.enums.EnumGui;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.Vec3i;
import scala.actors.threadpool.Arrays;

public enum MachineType {
	
	NONE(0, "none", -1),
	FURNACE(1, "furnace", EnumGui.FURNACE.getId()),
	GRINDER(2, "grinder", EnumGui.GRINDER.getId()),
	PURIFIER(3, "purifier", EnumGui.PURIFIER.getId()),
	ALLOY(4, "alloy", EnumGui.ALLOY.getId()),
	MELTER(5, "melter", EnumGui.MELTER.getId());
	
    private final int index;
    private final String name;
    private int guiid;    
    
    private static Map<String, MachineType> val = new HashMap<String, MachineType>();

    private MachineType(int index, String name, int guiid) {
    	this.index = index;
    	this.name = name;
    	this.guiid = guiid;
    }
    
    static {
        for (MachineType face : values()) {
            val.put(face.name, face);
        }
    }    
    
    public int getIndex() { return index; }
    public String getName() { return name; }
    public int getGuiId() { return guiid; }
    
    public static MachineType lookup(String name) {
    	return val.get(name);
    }
}
