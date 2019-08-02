package jaminv.advancedmachines.objects.blocks.machine.multiblock;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;

public enum MultiblockBorderType {
	NONE(0, "none"),
	SINGLE(1, "single"),
	SOLID(2, "solid");

    private static Map<String, MultiblockBorderType> val = new HashMap<String, MultiblockBorderType>();
	
    private final int index;
    private final String name;

    private MultiblockBorderType(int index, String name) {
    	this.index = index;
    	this.name = name;
    }
    
    public int getIndex() { return index; }
    public String getName() { return name; }
    
    static {
        for (MultiblockBorderType border : values()) {
            val.put(border.name, border);
        }
    }    
    
    public static MultiblockBorderType lookup(String name) {
    	return val.get(name);
    }    
}
