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

public enum MachineFace {
	
	NONE(0, "none"),
	F2x2P00(1, "f2x2p00"),
	F2x2P10(2, "f2x2p10"),
	F2x2P01(3, "f2x2p01"),
	F2x2P11(4, "f2x2p11"),
	F3x3P00(5, "f3x3p00"),
	F3x3P10(6, "f3x3p10"),
	F3x3P20(7, "f3x3p20"),
	F3x3P01(8, "f3x3p01"),
	F3x3P11(9, "f3x3p11"),
	F3x3P21(10, "f3x3p21"),
	F3x3P02(11, "f3x3p02"),
	F3x3P12(12, "f3x3p12"),
	F3x3P22(13, "f3x3p22");	
	
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
    	return val.get(name);
    }
}
