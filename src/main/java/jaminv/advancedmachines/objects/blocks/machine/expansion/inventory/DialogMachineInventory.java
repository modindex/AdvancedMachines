package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.objects.blocks.inventory.ContainerInventory;
import jaminv.advancedmachines.objects.blocks.inventory.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.control.DialogTextBox;
import jaminv.advancedmachines.util.dialog.control.IDialogElement;
import jaminv.advancedmachines.util.dialog.control.IElementStateObserver;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.util.enums.EnumComponent;
import net.minecraft.client.resources.I18n;

public class DialogMachineInventory extends DialogBase implements IElementStateObserver<String> {
	
	
	TileEntityMachineInventory te;
	DialogTextBox priority;
	
	public static final ContainerLayout layout = new ContainerLayout()
		.addLayout(8, 38)
		.setInventoryLayout(8, 103)
		.setHotbarLayout(8, 161);
	
	public DialogMachineInventory(ContainerInventory container, TileEntityMachineInventory te) {
		super(container, "textures/gui/machine_inventory.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogIOToggleButton(8, 23, 9, 9, te)); 
		
		priority = new DialogTextBox(EnumComponent.PRIORITY_MACHINE_INVENTORY.getId(), 136, 24, 36, 11, 4);
		priority.setPattern("[0-9]");
		priority.addObserver(this);
		priority.setText(String.valueOf(te.getPriority()));
		this.addElement(priority);

		this.addText(8, 6, 162, "dialog.machine_inventory.title", Color.DIALOG_TEXT);
		this.addText(8, 93, "dialog.common.inventory", Color.DIALOG_TEXT);
		this.addText(92, 24, "dialog.common.priority", Color.DIALOG_TEXT);
	}

	@Override
	public void onStateChanged(IDialogElement element, String state) {
		try {
			te.setPriority(Integer.parseInt(state));
		} catch (NumberFormatException e) {
			te.setPriority(0);
		}
	}
	
	@Override
	public void initGui() {
		priority.setText(String.valueOf(te.getPriority()));
		super.initGui();
	}
	
	
}
