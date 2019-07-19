package jaminv.advancedmachines.objects.blocks.machine.instance.purifier;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
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
	
	private final DialogMachinePurifier dialog = new DialogMachinePurifier(this);
	
	public static class GuiMachinePurifier extends GuiContainerObservable {
		public GuiMachinePurifier(ContainerMachine container, DialogMachineBase dialog) {
			super(container, dialog.getW(), dialog.getH());
			this.addObserver(dialog);
		}
	}
	
	public TileEntityMachinePurifier() {
		super(PurifierManager.getRecipeManager());		
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, this, dialog, PurifierManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachinePurifier(createContainer(inventory), dialog);
	}
}
