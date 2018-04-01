package jaminv.advancedmachines.objects.blocks.machine.expansion.inventory;

import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
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
	
	public static enum IOState implements IEnumIterable<IOState> {
		INPUT(true, "dialog.common.input"), OUTPUT(false, "dialog.common.output");
		private static IOState[] vals = values();
		
		private final boolean input;
		private final String name;
		private IOState(boolean input, String name) {
			this.input = input;
			this.name = name;
		}
		
		public boolean getState() { return input; }
		public String getName() { return name; }
		
		@Override
		public IOState next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}
	
	public static class IOToggleButton extends DialogToggleButton<IOState> {
		protected final TileEntityMachineInventory te;
		public IOToggleButton(TileEntityMachineInventory te) {
			// This is created before the NBT data for the tile entity is loaded, so the default is largely irrevelent here and is set later.
			super(8, 23, 9, 9, IOState.INPUT);
			this.te = te;
			this.addTexture(IOState.INPUT, 200, 0);
			this.addTexture(IOState.OUTPUT, 200, 9);
		}
		
		@Override
		protected void onStateChanged(IOState newstate) {
			te.setInputState(newstate.getState());
		}
		
		@Override
		public void draw(GuiScreen screen, FontRenderer font, int drawX, int drawY) {
			this.state = te.getInputState() ? IOState.INPUT : IOState.OUTPUT;
			super.draw(screen, font, drawX, drawY);
		}
	}
	
	public static class DialogTooltipInput extends DialogTooltip {
		protected final IOToggleButton button;
		public DialogTooltipInput(IOToggleButton button) {
			super(8, 23, 9, 9, "");
			this.button = button;
		}
		
		@Override
		public String getText() {
			return I18n.format(button.getState().getName());
		}
	}
	
	TileEntityMachineInventory te;
	
	public DialogMachineInventory(TileEntityMachineInventory te) {
		super("textures/gui/machine_inventory.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addLayout(new InventoryLayout(8, 38));
		
		IOToggleButton button = new IOToggleButton(te);
		this.addElement(button);
		this.addTooltip(new DialogTooltipInput(button));
		
		this.setInventoryLayout(new InventoryLayout(8, 103));
		this.setHotbarLayout(new HotbarLayout(8, 161));
		
		DialogTextBox pri = new DialogTextBox(EnumComponent.PRIORITY_MACHINE_INVENTORY.getId(), 136, 24, 36, 11, 4);
		pri.addObserver(this);
		this.addElement(pri);

		this.addText(8, 6, 162, "tile.machine_inventory.name", Color.DIALOG_TEXT);
		this.addText(8, 93, "dialog.common.inventory", Color.DIALOG_TEXT);
		this.addText(92, 24, "dialog.common.priority", Color.DIALOG_TEXT);
	}

	@Override
	public void onStateChanged(IDialogElement element, String state) {
		te.setPriority(Integer.parseInt(state));
	}
}
