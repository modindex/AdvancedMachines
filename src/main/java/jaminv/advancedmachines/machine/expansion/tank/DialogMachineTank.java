package jaminv.advancedmachines.machine.expansion.tank;

import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketLayout;
import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogTextBox;
import jaminv.advancedmachines.machine.dialog.DialogFluid;
import jaminv.advancedmachines.machine.dialog.DialogIOToggle;
import jaminv.advancedmachines.util.Color;
import net.minecraft.inventory.Container;

public class DialogMachineTank extends Dialog {
	
	TileMachineTank te;
	DialogTextBox priority;
	
	public static final ILayoutManager layout = new LayoutManager()
		.addLayout(new BucketLayout(44, 37))
		.addLayout(new BucketLayout(116, 37))
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public DialogMachineTank(Container container, TileMachineTank te) {
		super(container, "textures/gui/tank.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogFluid(80, 21, 16, 48, te.getTank()));
		this.addElement(new DialogIOToggle(8, 8, 9, 9, te));

		this.addText(8, 6, 162, "dialog.machine_tank.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

	}
}
