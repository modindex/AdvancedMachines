package jaminv.advancedmachines.machine.multiblock.face;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.render.RawTextures;

public enum MachineFace {
	
	NONE(0, "none"),
	F2x2P00(1, "2x2[0][0]"),
	F2x2P10(2, "2x2[1][0]"),
	F2x2P01(3, "2x2[0][1]"),
	F2x2P11(4, "2x2[1][1]"),
	F3x3P00(5, "3x3[0][0]"),
	F3x3P10(6, "3x3[1][0]"),
	F3x3P20(7, "3x3[2][0]"),
	F3x3P01(8, "3x3[0][1]"),
	F3x3P11(9, "3x3[1][1]"),
	F3x3P21(10, "3x3[2][1]"),
	F3x3P02(11, "3x3[0][2]"),
	F3x3P12(12, "3x3[1][2]"),
	F3x3P22(13, "3x3[2][2]");	
	
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
    	MachineFace ret = val.get(name);
    	if (ret != null) { return ret; }
    	return NONE;
    }
    
    public static MachineFace build(int count, int x, int y) {
    	return lookup((count == 2 ? "2x2" : "3x3") + "[" + Integer.toString(x) + "][" + Integer.toString(y) + "]");
    }
    
    public @Nullable Texture getTexture(MachineType type, boolean active) {
    	if (type == MachineType.NONE) { return null; }
    	if (this == MachineFace.NONE) { return RawTextures.get(type.getName(), active ? "active" : "inactive"); }
    	return RawTextures.get(type.getName(), active ? "active" : "inactive", getName());
    }
}