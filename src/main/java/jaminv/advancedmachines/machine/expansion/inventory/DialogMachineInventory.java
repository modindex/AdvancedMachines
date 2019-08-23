package jaminv.advancedmachines.machine.expansion.inventory;

import jaminv.advancedmachines.lib.container.layout.ILayoutManager;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid;
import jaminv.advancedmachines.lib.container.layout.LayoutManager;
import jaminv.advancedmachines.lib.dialog.Dialog;
import jaminv.advancedmachines.lib.dialog.control.DialogTextBox;
import jaminv.advancedmachines.lib.dialog.control.IDialogElement;
import jaminv.advancedmachines.lib.dialog.control.IElementStateObserver;
import jaminv.advancedmachines.machine.dialog.DialogIOToggle;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.enums.EnumComponent;
import net.minecraft.inventory.Container;

public class DialogMachineInventory extends Dialog implements IElementStateObserver<String> {
	
	
	TileEntityMachineInventory te;
	DialogTextBox priority;
	
	public static final ILayoutManager layout = new LayoutManager()
		.addLayout(new ItemLayoutGrid.InventoryLayout(8, 38))
		.setInventoryLayout(8, 103)
		.setHotbarLayout(8, 161);
	
	public DialogMachineInventory(Container container, TileEntityMachineInventory te) {
		super(container, "textures/gui/machine_inventory.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addElement(new DialogIOToggle(8, 23, 9, 9, te)); 
		
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
