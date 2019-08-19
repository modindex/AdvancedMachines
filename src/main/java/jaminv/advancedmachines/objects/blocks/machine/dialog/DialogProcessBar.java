package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.lib.machine.IMachineProcess;
import jaminv.advancedmachines.util.dialog.control.DialogProgressBar;

public class DialogProcessBar extends DialogProgressBar {

	protected final IMachineProcess machine;

	public DialogProcessBar(IMachineProcess machine, int x, int y, int w, int h) {
		super(x, y, w, h, ProgressAxis.HORIZONTAL);
		this.machine = machine;
	}	
	
	public DialogProcessBar(IMachineProcess machine, int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, ProgressAxis.HORIZONTAL);
		this.machine = machine;
	}

	@Override
	protected float getPercent() {
		return machine.getProcessPercent();
	}

}
