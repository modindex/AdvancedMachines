package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.IMachineEnergy;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.dialog.control.DialogProgressBar;
import net.minecraft.client.resources.I18n;

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
	
	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format("dialog.common.energy", te.getEnergyStored(), te.getMaxEnergyStored());
	}	
}
