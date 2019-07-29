package jaminv.advancedmachines.objects.blocks.machine.instance.furnace;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineParent;
import jaminv.advancedmachines.util.recipe.machine.FurnaceManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineFurnace extends TileEntityMachineMultiblock {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override 
	public int getSecondaryCount() { return 0; }
	
	
	public TileEntityMachineFurnace() {
		super(FurnaceManager.getRecipeManager());		
	}
	
	@Override
	public MachineParent getMachineType() {
		// TODO Auto-generated method stub
		return MachineParent.FURNACE;
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, DialogMachineFurnace.layout, this, FurnaceManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new DialogMachineFurnace(createContainer(inventory), this);
	}
}
