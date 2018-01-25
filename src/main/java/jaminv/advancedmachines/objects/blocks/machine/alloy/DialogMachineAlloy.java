package jaminv.advancedmachines.objects.blocks.machine.alloy;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.util.dialog.DialogBase;

public class DialogMachineAlloy extends DialogMachineBase {
	
	public DialogMachineAlloy() {
		super("textures/gui/alloy.png", 24, 0, 176, 166);

		this.addLayout(35, 37, 1, 3);		// Input
		this.addLayout(125, 37);			// Output		
		
		this.setProcessTexture(92, 37, 24, 17, 200, 50);
		this.setEnergyTexture(9, 20, 14, 50, 200, 0);
		
		this.setInventoryLayout(new InventoryLayout(8, 84));
		this.setHotbarLayout(new HotbarLayout(8, 142));
		
		this.addText(8, 8, 160, "dialog.alloy.title", 0x404040);
		this.addText(8, 73, "dialog.common.inventory", 0x404040);
	}
	
	public DialogMachineAlloy(TileEntityMachineAlloy te) {
		this();
		
		this.addTooltip(new TooltipEnergy(9, 20, 14, 50, te));
	}
	
	@Override
	public Texture getJeiTexture() {
		return new Texture(31, 18, 162, 54);
	}
	
	@Override
	public Target getJeiTarget() {
		return new DialogBase.Target(80, 37, 16, 16);
	}	
}
