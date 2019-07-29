package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import jaminv.advancedmachines.objects.blocks.fluid.ContainerFluid;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogFluid;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogTextBox;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.util.enums.IOState;
import net.minecraft.client.resources.I18n;

public class DialogMachineTank extends DialogBase {
	
	public static class DialogTooltipInput extends DialogTooltip {
		protected final DialogIOToggleButton button;
		public DialogTooltipInput(DialogIOToggleButton button) {
			super(8, 23, 9, 9, "");
			this.button = button;
		}
		
		@Override
		public String getText() {
			IOState state = button.getState();
			return I18n.format(state.getName());
		}
	}
	
	TileEntityMachineTank te;
	DialogTextBox priority;
	
	public static final ContainerLayout layout = new ContainerLayout()
		.addLayout(new ContainerFluid.BucketInputLayout(44, 37))
		.addLayout(new ContainerFluid.BucketOutputLayout(116, 37))
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public DialogMachineTank(ContainerInventory container, TileEntityMachineTank te) {
		super(container, "textures/gui/tank.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogFluid(80, 21, 16, 48, te));
		
		DialogIOToggleButton button = new DialogIOToggleButton(8, 8, 9, 9, te);
		this.addElement(button);
		this.addTooltip(new DialogTooltipInput(button));
		

		this.addText(8, 6, 162, "dialog.machine_inventory.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

	}
}
