package jaminv.advancedmachines.machine.expansion.tank;

import jaminv.advancedmachines.lib.dialog.Color;
import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogTextBox;
import jaminv.advancedmachines.lib.dialog.control.enums.IOState;
import jaminv.advancedmachines.lib.dialog.fluid.DialogBucketToggle;
import jaminv.advancedmachines.machine.dialog.DialogFluid;
import jaminv.advancedmachines.machine.dialog.DialogIOToggle;
import net.minecraft.inventory.Container;

public class DialogMachineTank extends Dialog {
	
	TileMachineTank te;
	DialogTextBox priority;
	
	public DialogMachineTank(Container container, TileMachineTank te) {
		super(container, "textures/gui/tank.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogFluid(80, 21, 16, 48, te.getTank()));
		this.addElement(new DialogIOToggle(8, 8, 9, 9, te));
		
		this.addElement(new DialogBucketToggle(141, 62, 7, 9, te.getBucketToggle())
				.addTexture(IOState.INPUT, 200, 81)
				.addTexture(IOState.OUTPUT, 207, 81));

		this.addText(8, 6, 162, "dialog.machine_tank.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

	}
}
