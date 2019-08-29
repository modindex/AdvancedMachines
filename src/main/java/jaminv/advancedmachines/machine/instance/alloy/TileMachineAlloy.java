package jaminv.advancedmachines.machine.instance.alloy;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.AlloyManager;
import net.minecraft.inventory.IInventory;

public class TileMachineAlloy extends TileMachineMultiblock {
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
			.setItemInputLayout(AlloyManager.getRecipeManager(), 35, 37, 1, 3) 
			.setItemOutputLayout(125, 37)		
			.setInventoryLayout(8, 84)
			.setHotbarLayout(8, 142);	
	
	public TileMachineAlloy() {
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
		return new ContainerMachine(layout, storage, playerInventory, this.getSyncManager());
	}
}
