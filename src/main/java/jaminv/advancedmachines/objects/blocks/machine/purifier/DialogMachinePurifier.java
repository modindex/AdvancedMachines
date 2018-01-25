package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.multiblock.DialogMachineMultiblock;
import jaminv.advancedmachines.util.dialog.DialogBase;

public class DialogMachinePurifier extends DialogMachineMultiblock {
	
	public DialogMachinePurifier() {
		super("textures/gui/purifier.png", 24, 0, 176, 195);
		
		this.addLayout(53, 23);			// Input
		this.addLayout(107, 23);		// Output
		this.addLayout(35, 55, 1, 6); 	// Secondary
		
		this.setInventoryLayout(new InventoryLayout(8, 84));
		this.setHotbarLayout(new HotbarLayout(8, 142));

		this.setProcessTexture(74, 23, 24, 17, 200, 50);
		this.setEnergyTexture(9, 20, 14, 50, 200, 0);
	}
	
	public DialogMachinePurifier(TileEntityMachinePurifier te) {
		this();
		
		this.addTooltip(new TooltipEnergy(9, 20, 14, 50, te));
		this.addTooltip(new TooltipMultiblock(158, 7, 11, 11, te));
	}	
	
	@Override
	public Texture getJeiTexture() {
		return new Texture(31, 18, 162, 54);
	}
	
	@Override
	public Target getJeiTarget() {
		return new DialogBase.Target(74, 23, 24, 17);
	}
}
