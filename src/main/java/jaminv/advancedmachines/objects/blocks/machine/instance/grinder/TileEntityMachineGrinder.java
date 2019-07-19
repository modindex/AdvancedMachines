package jaminv.advancedmachines.objects.blocks.machine.instance.grinder;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineGrinder extends TileEntityMachineMultiblock {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override 
	public int getSecondaryCount() { return 1; }
	
	private final DialogMachineGrinder dialog = new DialogMachineGrinder(this);
	
	public static class GuiMachineGrinder extends GuiContainerObservable {
		public GuiMachineGrinder(ContainerMachine container, DialogMachineBase dialog) {
			super(container, dialog.getW(), dialog.getH());
			this.addObserver(dialog);
		}
	}
	
	public TileEntityMachineGrinder() {
		super(GrinderManager.getRecipeManager());		
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, this, dialog, GrinderManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachineGrinder(createContainer(inventory), dialog);
	}
}
