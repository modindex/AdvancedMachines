package jaminv.advancedmachines.objects.blocks.machine.instance.alloy;

import jaminv.advancedmachines.objects.blocks.machine.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.DialogMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.instance.purifier.TileEntityMachinePurifier.GuiMachinePurifier;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.TileEntityMachineMultiblock;
import jaminv.advancedmachines.util.dialog.gui.GuiContainerObservable;
import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineAlloy extends TileEntityMachineMultiblock {
	@Override
	public int getInputCount() { return 3; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override
	public int getSecondaryCount() { return 9; }
	
	private final DialogMachineAlloy dialog = new DialogMachineAlloy(this);
	
	public class GuiMachineAlloy extends GuiContainerObservable {
		public GuiMachineAlloy(ContainerMachine container, DialogMachineBase dialog) {
			super(container, dialog.getW(), dialog.getH());
			this.addObserver(dialog);
		}
	}
	
	public TileEntityMachineAlloy() {
		super(AlloyManager.getRecipeManager());		
	}

	@Override
	public ContainerMachine createContainer(IInventory inventory) {
		return new ContainerMachine(inventory, this, dialog, AlloyManager.getRecipeManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory inventory) {
		return new GuiMachineAlloy(createContainer(inventory), dialog);
	}
}
