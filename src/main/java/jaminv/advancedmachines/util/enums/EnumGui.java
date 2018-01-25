package jaminv.advancedmachines.util.enums;

public enum EnumGui {
	PURIFIER(1),
	ALLOY(2),
	MACHINE_INVENTORY(10);
	
	private final int id;
	private EnumGui(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}