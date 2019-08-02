package jaminv.advancedmachines.objects.blocks.machine.instance.purifier;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachinePurifier extends TileEntityMachineMultiblock {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override 
	public int getSecondaryCount() { return 6; }
	
	public TileEntityMachinePurifier() {
		super(PurifierManager.getRecipeManager());		
	}
	
	@Override
	public MachineType getMachineType() {
		return MachineType.PURIFIER;
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, DialogMachinePurifier.layout, this, PurifierManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachinePurifier(createContainer(inventory), this);
	}
}
