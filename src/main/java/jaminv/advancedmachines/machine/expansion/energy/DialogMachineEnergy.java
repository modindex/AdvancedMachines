package jaminv.advancedmachines.machine.expansion.energy;

import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.machine.dialog.DialogEnergyBar;
import net.minecraft.inventory.Container;

public class DialogMachineEnergy extends Dialog {
	public DialogMachineEnergy(Container container, TileMachineEnergy te) {
		super(container, "textures/gui/machine_energy.png", 24, 0, 86, 77);
		
		this.addElement(new DialogEnergyBar(te.getEnergy(), 35, 20, 14, 50, 0, 0));

		this.addText(7, 9, 72, "dialog.energy.title", 0x404040);
	}
}
