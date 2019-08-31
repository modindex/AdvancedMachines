package jaminv.advancedmachines.machine.instance.press;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.JeiLayoutManager;
import jaminv.advancedmachines.machine.TileMachineMultiblock;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.util.recipe.AlloyManager;
import jaminv.advancedmachines.util.recipe.press.PressManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class TileMachinePress extends TileMachineMultiblock {
	
	public static final JeiLayoutManager layout	= new JeiLayoutManager()
		.setItemInputLayout(PressManager.getRecipeManager(), 44, 19, 3, 1) 
		.setItemOutputLayout(98, 23)		
		.setInventoryLayout(8, 84)
		.setHotbarLayout(8, 142);
	
	public TileMachinePress() {
		super(AlloyManager.getRecipeManager());
		inventory.addInputSlots(3);
		inventory.addOutputSlots(1);
	}

	@Override
	public MachineType getMachineType() {
		return MachineType.ALLOY;
	}

	@Override
	public ContainerMachine createContainer(IInventory playerInventory) {
		return new ContainerMachine(layout, storage, playerInventory, this.getSyncManager());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return super.writeToNBT(compound);
	}
	
	
}
