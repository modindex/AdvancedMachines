package jaminv.advancedmachines.lib.dialog.control.enums;

import jaminv.advancedmachines.lib.dialog.control.DialogToggleButton.IEnumIterable;

public class BooleanIterable implements IEnumIterable<BooleanIterable> {
	private final boolean state;
	public BooleanIterable(boolean state) {
		this.state = state;
	}
	
	public boolean getState() { return state; }
	
	@Override
	public BooleanIterable next() {
		return new BooleanIterable(!state);
	}
}
