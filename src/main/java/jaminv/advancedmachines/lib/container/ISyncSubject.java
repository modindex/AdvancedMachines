package jaminv.advancedmachines.lib.container;

import jaminv.advancedmachines.lib.machine.IRedstoneControlled.RedstoneState;

public interface ISyncSubject {
	public int getFieldCount();
	public int getField(int id);
	public void setField(int id, int value);
}
