package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.HotbarLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.InventoryLayout;

public class DialogMachinePurifier extends DialogMachineBase {
	
	public DialogMachinePurifier(TileEntityMachinePurifier te) {
		super("textures/gui/purifier.png", 24, 0, 176, 195, te);
		setProcessTexture(80, 37, 16, 16, 200, 50);
		setEnergyTexture(9, 20, 14, 50, 200, 0);	
		
		
	}
	
	@Override
	protected ContainerLayout getInputLayout() {
		return new ContainerLayout(53, 37);
	}
	@Override
	protected ContainerLayout getOutputLayout() {
		return new ContainerLayout(107, 37);
	}
	@Override
	protected ContainerLayout getSecondaryLayout() {
		return new ContainerLayout(8, 84, 1, 9);
	}
	@Override
	protected ContainerLayout getInventoryLayout() {
		return new InventoryLayout(8, 113);
	}
	@Override
	protected ContainerLayout getHotbarLayout() {
		return new HotbarLayout(8, 171);
	}	
}
