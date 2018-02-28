package jaminv.advancedmachines.objects.blocks.machine.alloy;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogEnergyBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogProcessBar;
import jaminv.advancedmachines.objects.blocks.machine.dialog.DialogTooltipEnergy;
import jaminv.advancedmachines.util.dialog.struct.DialogArea;

public class DialogMachineAlloy extends DialogMachineBase {
	
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
		
		this.addTooltip(new DialogTooltipEnergy(9, 20, 14, 50, te));
	}
	
	@Override
	public DialogArea getJeiTexture() {
		return new DialogArea(31, 18, 162, 54);
	}
	
	@Override
	public DialogArea getJeiTarget() {
		return new DialogArea(80, 37, 16, 16);
	}	
}
