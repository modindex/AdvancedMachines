package jaminv.advancedmachines.machine.instance.alloy;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.AlloyManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineAlloy extends TileEntityMachineMultiblock {
	
	public TileEntityMachineAlloy() {
		super(AlloyManager.getRecipeManager());
		inventory.addInputSlots(3);
		inventory.addOutputSlots(1);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.ALLOY;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(DialogMachineAlloy.layout, storage, playerInventory, this.getSyncManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory playerInventory) {
		return new DialogMachineAlloy(createContainer(playerInventory), this);
	}
}
