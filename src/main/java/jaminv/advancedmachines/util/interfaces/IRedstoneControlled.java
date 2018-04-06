package jaminv.advancedmachines.util.interfaces;

import jaminv.advancedmachines.util.dialog.control.DialogToggleButton.IEnumIterable;

public interface IRedstoneControlled {
	public static enum RedstoneState implements IEnumIterable<RedstoneState> {
		IGNORE(0, "dialog.common.redstone.ignore"), ACTIVE(1, "dialog.common.redstone.active"), INACTIVE(2, "dialog.common.redstone.inactive");
		private static RedstoneState[] vals = values();
		
		private final int value;
		private final String name;
		private RedstoneState(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int getValue() { return value; }
		public String getName() { return name; }
		
		@Override
		public RedstoneState next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
		
		public static RedstoneState fromValue(int value) {
			return vals[value];
		}
	}
	
	public void setRedstoneState(RedstoneState state);
	public RedstoneState getRedstoneState();
}
