package jaminv.advancedmachines.objects.blocks.machine.instance.furnace;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.recipe.machine.FurnaceManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineFurnace extends TileEntityMachineMultiblock {

	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override 
	public int getSecondaryCount() { return 0; }
	
	private final DialogMachineFurnace dialog = new DialogMachineFurnace(this);
	
	public static class GuiMachineFurnace extends GuiContainerObservable {
		public GuiMachineFurnace(ContainerMachine container, DialogMachineBase dialog) {
			super(container, dialog.getW(), dialog.getH());
			this.addObserver(dialog);
		}
	}
	
	public TileEntityMachineFurnace() {
		super(FurnaceManager.getRecipeManager());		
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, this, dialog, FurnaceManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachineFurnace(createContainer(inventory), dialog);
	}
}
