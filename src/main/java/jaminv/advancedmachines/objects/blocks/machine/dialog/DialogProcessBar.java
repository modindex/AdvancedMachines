package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.dialog.control.DialogProgressBar;
import jaminv.advancedmachines.util.dialog.control.DialogProgressBar.ProgressAxis;

public class DialogProcessBar extends DialogProgressBar {

	protected final TileEntityMachineBase te;

	public DialogProcessBar(TileEntityMachineBase te, int x, int y, int w, int h) {
		super(x, y, w, h, ProgressAxis.HORIZONTAL);
		this.te = te;
	}	
	
	public DialogProcessBar(TileEntityMachineBase te, int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, ProgressAxis.HORIZONTAL);
		this.te = te;
	}

	@Override
	protected float getPercent() {
		return te.getProcessPercent();
	}

}
