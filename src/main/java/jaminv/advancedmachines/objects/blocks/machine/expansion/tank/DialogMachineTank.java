package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.container.layout.impl.BucketInputLayout;
import jaminv.advancedmachines.lib.container.layout.impl.BucketOutputLayout;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogFluid;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogTextBox;
import net.minecraft.inventory.Container;

public class DialogMachineTank extends DialogBase {
	
	TileEntityMachineTank te;
	DialogTextBox priority;
	
	public static final ILayoutManager layout = new LayoutManager()
		.addLayout(new BucketInputLayout(44, 37))
		.addLayout(new BucketOutputLayout(116, 37))
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public DialogMachineTank(Container container, TileEntityMachineTank te) {
		super(container, "textures/gui/tank.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogFluid(80, 21, 16, 48, te.getTank()));
		this.addElement(new DialogIOToggleButton(8, 8, 9, 9, te));

		this.addText(8, 6, 162, "dialog.machine_tank.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

	}
}
