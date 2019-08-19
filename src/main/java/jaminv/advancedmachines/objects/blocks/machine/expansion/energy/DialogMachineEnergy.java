package jaminv.advancedmachines.objects.blocks.machine.expansion.energy;

import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.util.dialog.DialogBase;
import net.minecraft.inventory.Container;

public class DialogMachineEnergy extends DialogBase {
	public DialogMachineEnergy(Container container, TileEntityMachineEnergy te) {
		super(container, "textures/gui/machine_energy.png", 24, 0, 86, 77);
		
		this.addElement(new DialogEnergyBar(te.getEnergy(), 35, 20, 14, 50, 0, 0));

		this.addText(7, 9, 72, "dialog.energy.title", 0x404040);
	}
}
