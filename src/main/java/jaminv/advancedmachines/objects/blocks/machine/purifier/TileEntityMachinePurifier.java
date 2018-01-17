package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.GuiMachine;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachinePurifier extends TileEntityMachineMultiblock {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override
	public int getSecondaryCount() { return 6; }
	
	private final DialogMachinePurifier dialog = new DialogMachinePurifier();
	
	public static class GuiMachinePurifier extends GuiMachine {
		public GuiMachinePurifier(TileEntityMachineBase tileEntity, ContainerMachine container,
				DialogMachineBase dialog) {
			super(tileEntity, container, dialog);
		}
	}
	
	public TileEntityMachinePurifier() {
		super(PurifierManager.getRecipeManager());		
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, this, PurifierManager.getRecipeManager(), dialog);
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachinePurifier(this, createContainer(inventory), dialog);
	}
}
