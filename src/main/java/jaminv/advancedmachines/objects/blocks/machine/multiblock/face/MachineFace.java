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

import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.Vec3i;
import scala.actors.threadpool.Arrays;

public enum MachineFace {
	
	NONE(0, "none"),
	F2x2(1, "f2x2"),
	F3x3(2, "f3x3");
	
    private final int index;
    private final String name;
    
    private static Map<String, MachineFace> val = new HashMap<String, MachineFace>();

    private MachineFace(int index, String name) {
    	this.index = index;
    	this.name = name;
    }
    
    static {
        for (MachineFace face : values()) {
            val.put(face.name, face);
        }
    }    
    
    public int getIndex() { return index; }
    public String getName() { return name; }
    
    public static MachineFace lookup(String name) {
    	return val.get(name.substring(0, 4));
    }
}
