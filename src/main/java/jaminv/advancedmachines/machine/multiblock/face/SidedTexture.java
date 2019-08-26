package jaminv.advancedmachines.machine.multiblock.face;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.util.enums.EnumGui;

public enum SidedTexture {
	SIDE(0, "side"),
	TOP(1, "top");
	
    private final int index;
    private final String name;
    
    private SidedTexture(int index, String name) {
    	this.index = index;
    	this.name = name;
    }
    
    public int getIndex() { return index; }
    public String getName() { return name; }
}
