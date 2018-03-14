package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.IMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.dialog.control.DialogProgressBar;

public class DialogEnergyBar extends DialogProgressBar {

	protected final IMachineEnergy te;
	
	public DialogEnergyBar(IMachineEnergy te, int x, int y, int w, int h) {
		super(x, y, w, h, ProgressAxis.VERTICAL);
		this.te = te;
	}
	
	public DialogEnergyBar(IMachineEnergy te, int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, ProgressAxis.VERTICAL);
		this.te = te;
	}

	@Override
	protected float getPercent() {
		return te.getEnergyPercent();
	}

}
