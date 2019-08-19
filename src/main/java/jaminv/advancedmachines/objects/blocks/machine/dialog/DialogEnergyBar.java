package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.util.dialog.control.DialogProgressBar;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.energy.IEnergyStorage;

public class DialogEnergyBar extends DialogProgressBar {

	protected final IEnergyStorage energy;
	
	public DialogEnergyBar(IEnergyStorage energy, int x, int y, int w, int h) {
		super(x, y, w, h, ProgressAxis.VERTICAL);
		this.energy = energy;
	}
	
	public DialogEnergyBar(IEnergyStorage energy, int x, int y, int w, int h, int u, int v) {
		super(x, y, w, h, u, v, ProgressAxis.VERTICAL);
		this.energy = energy;
	}

	@Override
	protected float getPercent() {
		return energy.getEnergyStored() / (float)energy.getMaxEnergyStored();
	}
	
	@Override
	public String getTooltip(int mouseX, int mouseY) {
		return I18n.format("dialog.common.energy", energy.getEnergyStored(), energy.getMaxEnergyStored());
	}	
}
