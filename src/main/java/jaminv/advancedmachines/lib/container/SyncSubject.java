package jaminv.advancedmachines.lib.container;

import jaminv.advancedmachines.lib.machine.RedstoneControlled.RedstoneState;

public interface SyncSubject {
	public int getFieldCount();
	public int getField(int id);
	public void setField(int id, int value);
}
