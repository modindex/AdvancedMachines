package jaminv.advancedmachines.util.enums;

public enum EnumComponent {
	PRIORITY_MACHINE_INVENTORY(101);
	
	private final int id;
	private EnumComponent(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}