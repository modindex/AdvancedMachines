package jaminv.advancedmachines.objects.blocks.machine.instance.alloy;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogMultiblockQuantity;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipEnergy;
import jaminv.advancedmachines.objects.blocks.machine.dialog.RedstoneToggleButton;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.DialogMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.DialogMachineMultiblock.TooltipMultiblock;
import jaminv.advancedmachines.util.Color;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;
import jaminv.advancedmachines.util.interfaces.IRedstoneControlled;

public class DialogMachineAlloy extends DialogMachineMultiblock {
	
	public DialogMachineAlloy() {
		super("textures/gui/alloy.png", 24, 0, 176, 166);

		this.addLayout(35, 37, 1, 3);		// Input 
		this.addLayout(125, 37);			// Output		
		
		this.setInventoryLayout(new InventoryLayout(8, 84));
		this.setHotbarLayout(new HotbarLayout(8, 142));
		
		this.addText(8, 8, 160, "dialog.alloy.title", 0x404040);
		this.addText(8, 73, "dialog.common.inventory", 0x404040);
	}
	
	public DialogMachineAlloy(TileEntityMachineAlloy te) {
		this();

		this.addElement(new DialogProcessBar(te, 92, 37, 24, 17, 200, 50));
		this.addElement(new DialogEnergyBar(te, 9, 20, 14, 50, 200, 0));
		this.addElement(new RedstoneToggleButton((IRedstoneControlled)te));
		
		this.addTooltip(new TooltipMultiblock(158, 7, 11, 11, te));		
		
		this.addTooltip(new DialogTooltipEnergy(9, 20, 14, 50, te));
		
		this.addText(new DialogMultiblockQuantity(te, 92, 33, 26, 26, Color.DIALOG_TEXT));
		this.addText(new DialogMultiblockQuantity(te, 91, 32, 26, 26, Color.WHITE));		
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
