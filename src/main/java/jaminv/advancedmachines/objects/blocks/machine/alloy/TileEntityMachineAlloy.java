package jaminv.advancedmachines.objects.blocks.machine.alloy;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.GuiMachine;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineAlloy extends TileEntityMachineBase {
	@Override
	public int getInputCount() { return 3; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override
	public int getSecondaryCount() { return 9; }
	
	private final DialogMachineAlloy dialog = new DialogMachineAlloy(this);
	
	public TileEntityMachineAlloy() {
		super(AlloyManager.getRecipeManager());		
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, this, AlloyManager.getRecipeManager(), dialog);
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachine(this, createContainer(inventory), dialog);
	}
}
