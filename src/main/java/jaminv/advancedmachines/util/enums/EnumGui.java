package jaminv.advancedmachines.util.enums;

public enum EnumGui {
	PURIFIER(1),
	ALLOY(2),
	MACHINE_INVENTORY(10),
	MACHINE_POWER(11),
	PRIORITY(101);
	
	private final int id;
	private EnumGui(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}