package jaminv.advancedmachines.objects.blocks.machine.expansion.priority;

import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipEnergy;
import jaminv.advancedmachines.util.dialog.DialogBase;

public class DialogPriority extends DialogBase {
	public DialogPriority() {
		super("textures/gui/priority.png", 24, 0, 155, 89);
		
		this.addElement(new DialogEnergyBar(te, 35, 20, 14, 50, 0, 0));
		this.addTooltip(new DialogTooltipEnergy(35, 20, 14, 50, te));
	
		this.addText(7, 9, 72, "dialog.energy.title", 0x404040);
	}
}
