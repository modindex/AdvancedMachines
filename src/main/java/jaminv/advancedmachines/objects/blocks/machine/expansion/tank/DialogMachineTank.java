package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import jaminv.advancedmachines.objects.blocks.fluid.ContainerFluid;
import jaminv.advancedmachines.objects.blocks.inventory.DialogInventory;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogFluid;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogIOToggleButton;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.control.DialogTextBox;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton;
import jaminv.advancedmachines.util.dialog.control.DialogToggleButton.IEnumIterable;
import jaminv.advancedmachines.util.dialog.control.IDialogElement;
import jaminv.advancedmachines.util.dialog.control.IElementStateObserver;
import jaminv.advancedmachines.util.dialog.struct.DialogTooltip;
import jaminv.advancedmachines.util.enums.EnumComponent;
import jaminv.advancedmachines.util.enums.IOState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DialogMachineTank extends DialogInventory {
	
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
	
	public DialogMachineTank(TileEntityMachineTank te) {
		super("textures/gui/tank.png", 24, 0, 176, 185);
		this.te = te;
		
		this.addLayout(new ContainerFluid.BucketInputLayout(44, 37));
		this.addLayout(new ContainerFluid.BucketOutputLayout(116, 37));
		
		this.addElement(new DialogFluid(80, 21, 16, 48, te));
		
		DialogIOToggleButton button = new DialogIOToggleButton(8, 8, 9, 9, te);
		this.addElement(button);
		this.addTooltip(new DialogTooltipInput(button));
		
		this.setInventoryLayout(new InventoryLayout(8, 84));
		this.setHotbarLayout(new HotbarLayout(8, 142));

		this.addText(8, 6, 162, "dialog.machine_inventory.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);

	}
}
