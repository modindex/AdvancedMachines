package jaminv.advancedmachines.machine.instance.grinder;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.HotbarLayout;
import jaminv.advancedmachines.lib.container.layout.ItemLayoutGrid.InventoryLayout;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.grinder.GrinderManager;
import net.minecraft.inventory.IInventory;

public class TileMachineGrinder extends TileMachineMultiblock {
	
	public static final JeiLayoutManager layout = new JeiLayoutManager()
		.setItemInputLayout(GrinderManager.getRecipeManager(), 53, 26)
		.setItemOutputLayout(107, 26)
		.setItemSecondaryLayout(107, 52)
		.setInventoryLayout(new InventoryLayout(8, 84))
		.setHotbarLayout(new HotbarLayout(8, 142));	
	
	public TileMachineGrinder() {
		super(GrinderManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
		inventory.addSecondarySlots(1);
	}
	
	@Override
	public MachineType getMachineType() {
		return MachineType.GRINDER;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(layout, storage, playerInventory, this.getSyncManager());
	}
}
