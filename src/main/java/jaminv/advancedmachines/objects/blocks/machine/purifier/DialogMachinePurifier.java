package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.HotbarLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.InventoryLayout;
import jaminv.advancedmachines.util.dialog.DialogBase;

public class DialogMachinePurifier extends DialogMachineBase {
	
	public DialogMachinePurifier() {
		super("textures/gui/purifier.png", 24, 0, 176, 195);
		setProcessTexture(74, 23, 24, 17, 200, 50);
		setEnergyTexture(9, 20, 14, 50, 200, 0);
	}
	
	@Override
	public ContainerLayout getInputLayout() {
		return new ContainerLayout(53, 23);
	}
	@Override
	public ContainerLayout getOutputLayout() {
		return new ContainerLayout(107, 23);
	}
	@Override
	public ContainerLayout getSecondaryLayout() {
		return new ContainerLayout(35, 55, 1, 6);
	}
	@Override
	protected ContainerLayout getInventoryLayout() {
		return new InventoryLayout(8, 84);
	}
	@Override
	protected ContainerLayout getHotbarLayout() {
		return new HotbarLayout(8, 142);
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
