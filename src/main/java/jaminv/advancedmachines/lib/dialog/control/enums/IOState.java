package jaminv.advancedmachines.lib.dialog.control.enums;

import jaminv.advancedmachines.lib.dialog.control.DialogToggleButton.IEnumIterable;

public enum IOState implements IEnumIterable<IOState> {
	INPUT(true, "dialog.common.input"), OUTPUT(false, "dialog.common.output");
	private static IOState[] vals = values();
	
	private final boolean input;
	private final String name;
	private IOState(boolean input, String name) {
		this.input = input;
		this.name = name;
	}
	
	public boolean getState() { return input; }
	public String getName() { return name; }
	
	@Override
	public IOState next() {
		return vals[(this.ordinal() + 1) % vals.length];
	}
	
	public static IOState find(boolean state) {
		return state ? INPUT : OUTPUT;
	}
}	