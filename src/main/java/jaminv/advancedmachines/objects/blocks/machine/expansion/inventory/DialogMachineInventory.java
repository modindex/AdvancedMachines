package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.control.DialogTextBox;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton.IEnumIterable;
import jaminv.advancedmachines.util.dialog.control.IDialogElement;
import jaminv.advancedmachines.util.dialog.control.IElementStateObserver;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.util.enums.EnumComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class DialogMachineInventory extends DialogInventory implements IElementStateObserver<String> {
	
	public static class DialogTooltipInput extends DialogTooltip {
		protected final DialogIOToggleButton button;
		public DialogTooltipInput(DialogIOToggleButton button) {
			super(8, 23, 9, 9, "");
			this.button = button;
		}
		
		@Override
		public String getText() {
			return I18n.format(button.getState().getName());
		}
	}
	
	TileEntityMachineInventory te;
	DialogTextBox priority;
	
	public DialogMachineInventory(TileEntityMachineInventory te) {
		super("textures/gui/machine_inventory.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addLayout(new InventoryLayout(8, 38));
		
		DialogIOToggleButton button = new DialogIOToggleButton(8, 23, 9, 9, te); 
		this.addElement(button);
		this.addTooltip(new DialogTooltipInput(button));
		
		this.setInventoryLayout(new InventoryLayout(8, 103));
		this.setHotbarLayout(new HotbarLayout(8, 161));
		
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
	public void init(GuiScreen gui, FontRenderer font, int guiLeft, int guiTop) {
		priority.setText(String.valueOf(te.getPriority()));
		super.init(gui, font, guiLeft, guiTop);
	}
}
