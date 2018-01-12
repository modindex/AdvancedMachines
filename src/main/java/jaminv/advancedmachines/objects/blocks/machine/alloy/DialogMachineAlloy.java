package jaminv.advancedmachines.objects.blocks.machine.alloy;

import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.ContainerLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.HotbarLayout;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase.InventoryLayout;

public class DialogMachineAlloy extends DialogMachineBase {
	
	public DialogMachineAlloy() {
		super("textures/gui/alloy.png", 24, 0, 176, 166);
		setProcessTexture(98, 37, 16, 16, 200, 50);
		setEnergyTexture(9, 20, 14, 50, 200, 0);
		
		addText(8, 8, 160, "dialog.alloy.title", 0x404040);
		addText(8, 73, "dialog.common.inventory", 0x404040);
	}
	
	@Override
	protected ContainerLayout getInputLayout() {
		return new ContainerLayout(35, 37, 1, 3);
	}
	@Override
	protected ContainerLayout getOutputLayout() {
		return new ContainerLayout(125, 37);
	}
	@Override
	protected ContainerLayout getSecondaryLayout() {
		return new ContainerLayout(0, 0, 0, 0);
	}
	@Override
	protected ContainerLayout getInventoryLayout() {
		return new InventoryLayout(8, 84);
	}
	@Override
	protected ContainerLayout getHotbarLayout() {
		return new HotbarLayout(8, 142);
	}	
}
