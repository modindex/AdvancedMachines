package jaminv.advancedmachines.objects.blocks.machine.instance.grinder;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineParent;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineGrinder extends TileEntityMachineMultiblock {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override 
	public int getSecondaryCount() { return 1; }
	
	
	public TileEntityMachineGrinder() {
		super(GrinderManager.getRecipeManager());		
	}
	
	@Override
	public MachineParent getMachineType() {
		return MachineParent.GRINDER;
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, DialogMachineGrinder.layout, this, GrinderManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineGrinder(createContainer(inventory), this);
	}
}
