package jaminv.advancedmachines.machine.multiblock.face;

import java.util.HashMap;
import java.util.Map;

import jaminv.advancedmachines.proxy.GuiProxy;

public enum MachineType {
	
	NONE(0, "none", -1),
	FURNACE(1, "furnace", GuiProxy.FURNACE),
	GRINDER(2, "grinder", GuiProxy.GRINDER),
	PURIFIER(3, "purifier", GuiProxy.PURIFIER),
	ALLOY(4, "alloy", GuiProxy.ALLOY),
	MELTER(5, "melter", GuiProxy.MELTER),
	STABILIZER(6, "stabilizer", GuiProxy.STABILIZER),
	INJECTOR(7, "injector", GuiProxy.INJECTOR),
	PRESS(8, "press", GuiProxy.PRESS);
	
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
