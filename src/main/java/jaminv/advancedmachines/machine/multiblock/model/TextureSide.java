package jaminv.advancedmachines.machine.multiblock.model;

public enum TextureSide {
	SIDE(0, "side"),
	TOP(1, "top");
	
    private final int index;
    private final String name;
    
    private TextureSide(int index, String name) {
    	this.index = index;
    	this.name = name;
    }
    
    public int getIndex() { return index; }
    public String getName() { return name; }
}
