package jaminv.advancedmachines.objects.blocks.machine.instance.furnace;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineMultiblock;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.FurnaceManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;

public class TileEntityMachineFurnace extends TileEntityMachineMultiblock {
	
	public TileEntityMachineFurnace() {
		super(FurnaceManager.getRecipeManager());
		inventory.addInputSlots(1);
		inventory.addOutputSlots(1);
	}

	
	@Override
	public MachineType getMachineType() {
		return MachineType.FURNACE;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(DialogMachineFurnace.layout, storage, playerInventory, this.getSyncManager());
	}
	
	@Override
	public GuiContainer createGui(IInventory playerInventory) {
		return new DialogMachineFurnace(createContainer(playerInventory), this);
	}
}
