package jaminv.advancedmachines.util.enums;

public enum EnumGui {
	PURIFIER(1),
	ALLOY(2),
	FURNACE(3),
	GRINDER(4),
	MACHINE_INVENTORY(10),
	MACHINE_POWER(11),
	MACHINE_TANK(20),
	PRIORITY(101);
	
	private final int id;
	private EnumGui(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}