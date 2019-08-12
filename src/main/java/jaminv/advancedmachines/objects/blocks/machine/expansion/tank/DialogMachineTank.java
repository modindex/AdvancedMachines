package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import jaminv.advancedmachines.lib.container.ContainerFluid;
import jaminv.advancedmachines.lib.container.ContainerLayout;
import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogFluid;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogTextBox;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.util.enums.IOState;
import net.minecraft.client.resources.I18n;

public class DialogMachineTank extends DialogBase {
	
	TileEntityMachineTank te;
	DialogTextBox priority;
	
	public static final ILayoutManager layout = new LayoutManager()
		.addLayout(new ContainerFluid.BucketInputLayout(44, 37))
		.addLayout(new ContainerFluid.BucketOutputLayout(116, 37))
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public DialogMachineTank(ContainerLayout container, TileEntityMachineTank te) {
		super(container, "textures/gui/tank.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogFluid(80, 21, 16, 48, te));
		this.addElement(new DialogIOToggleButton(8, 8, 9, 9, te));

		this.addText(8, 6, 162, "dialog.machine_tank.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

	}
}
