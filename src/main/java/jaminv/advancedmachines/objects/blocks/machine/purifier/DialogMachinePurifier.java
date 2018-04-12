package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipEnergy;
import jaminv.advancedmachines.objects.blocks.machine.dialog.RedstoneToggleButton;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.DialogMachineMultiblock;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.DialogBase;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.dialog.struct.DialogTexture;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;

public class DialogMachinePurifier extends DialogMachineMultiblock {
	
	public DialogMachinePurifier() {
		super("textures/gui/purifier.png", 24, 0, 176, 195);
		
		this.addLayout(new InputLayout(PurifierManager.getRecipeManager(), 53, 23));
		this.addLayout(new OutputLayout(107, 23));			// Output
		this.addLayout(new OutputLayout(35, 55, 1, 6)); 	// Secondary
		
		this.setInventoryLayout(new InventoryLayout(8, 84));
		this.setHotbarLayout(new HotbarLayout(8, 142));
		
		this.addText(8, 8, 160, "dialog.purifier.title", Color.DIALOG_TEXT);
		this.addText(8, 73, "dialog.common.inventory", Color.DIALOG_TEXT);
	}
	
	public DialogMachinePurifier(TileEntityMachinePurifier te) {
		this();
		this.addElement(new DialogProcessBar(te, 74, 23, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te, 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((IRedstoneControlled)te));
		
		this.addTooltip(new TooltipMultiblock(158, 7, 11, 11, te));
		
		this.addText(new DialogMultiblockQuantity(te, 74, 19, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te, 73, 18, 26, 26, Color.WHITE));
	}	
	
	@Override
	public DialogArea getJeiTexture() {
		return new DialogArea(31, 18, 146, 54);
	}
	
	@Override
	public DialogArea getJeiTarget() {
		return new DialogArea(153, 38, 14, 14);
	}
}
