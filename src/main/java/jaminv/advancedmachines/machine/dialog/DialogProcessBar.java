package jaminv.advancedmachines.machine.dialog;

import jaminv.advancedmachines.lib.dialog.control.DialogProgressBar;
import jaminv.advancedmachines.lib.machine.DoesProcess;

public class DialogProcessBar extends DialogProgressBar {

	protected final DoesProcess machine;

	public DialogProcessBar(DoesProcess machine, int x, int y, int w, int h) {
		super(x, y, w, h, ProgressAxis.HORIZONTAL);
		this.machine = machine;
	}	
	
	public DialogProcessBar(DoesProcess machine, int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, ProgressAxis.HORIZONTAL);
		this.machine = machine;
	}

	@Override
	protected float getPercent() {
		return machine.getProcessPercent();
	}

}
