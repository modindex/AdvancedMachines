package jaminv.advancedmachines.objects.blocks.machine.instance.alloy;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineAlloy extends TileEntityMachineMultiblock {
	@Override
	public int getInputCount() { return 3; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override
	public int getSecondaryCount() { return 9; }
	
	
	public TileEntityMachineAlloy() {
		super(AlloyManager.getRecipeManager());		
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.ALLOY;
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, DialogMachineAlloy.layout, this, AlloyManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineAlloy(createContainer(inventory), this);
	}
}
