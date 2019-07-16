package jaminv.advancedmachines.objects.blocks.machine.expansion.energy;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipEnergy;
import jaminv.advancedmachines.objects.blocks.machine.instance.alloy.TileEntityMachineAlloy;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton.IEnumIterable;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import net.minecraft.client.resources.I18n;

public class DialogMachineEnergy extends DialogBase {
	public DialogMachineEnergy(TileEntityMachineEnergy te) {
		super("textures/gui/machine_energy.png", 24, 0, 86, 77);
		
		this.addElement(new DialogEnergyBar(te, 35, 20, 14, 50, 0, 0));
		this.addTooltip(new DialogTooltipEnergy(35, 20, 14, 50, te));

		this.addText(7, 9, 72, "dialog.energy.title", 0x404040);
	}
}
