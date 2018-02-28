package jaminv.advancedmachines.objects.blocks.machine.dialog;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import net.minecraft.client.resources.I18n;

public class DialogTooltipEnergy extends DialogTooltip {
	protected final TileEntityMachineBase te;
	
	public DialogTooltipEnergy(int x, int y, int w, int h, TileEntityMachineBase te) {
		super(x, y, w, h, "");
		this.te = te;
	}

	@Override
	public String getText() {
		return I18n.format("dialog.common.energy", te.getEnergyStored(), te.getMaxEnergyStored());
	}
}
