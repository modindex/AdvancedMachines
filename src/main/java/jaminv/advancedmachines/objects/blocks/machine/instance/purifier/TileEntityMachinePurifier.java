package jaminv.advancedmachines.objects.blocks.machine.instance.purifier;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.purifier.PurifierManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachinePurifier extends TileEntityMachineMultiblock {

	public TileEntityMachinePurifier() {
		super(PurifierManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
		inventory.addSecondarySlots(6);
	}
	
	@Override
	public MachineType getMachineType() {
		return MachineType.PURIFIER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(DialogMachinePurifier.layout, storage, playerInventory, this.getSyncManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachinePurifier(createContainer(inventory), this);
	}
}
