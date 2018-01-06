package jaminv.advancedmachines.objects.blocks.machine.purifier;

import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.managers.machine.PurifierManager;
import jaminv.advancedmachines.util.managers.machine.RecipeInput;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMachinePurifier extends TileEntityMachineBase implements ITickable {
	public static final int INPUT = 1;
	public static final int OUTPUT = 1;
	public static final int SIZE = INPUT + OUTPUT;
		
	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityMachinePurifier.this.markDirty();
		}
	};
	
	private int processTimeRemaining = -1;
	private RecipeInput lastInput = RecipeInput.EMPTY;
	
	@Override
	public boolean canProcess() {
		if (PurifierManager.getRecipe(itemStackHandler.getStackInSlot(0)) != null) {
			return true;
		}
		return false;
	}

	@Override
	protected void process() {
		// Just in case
		if (processTimeRemaining < 0) { haltProcess(); return; }
		
		RecipeInput input = new RecipeInput(itemStackHandler.getStackInSlot(0));
		if (isProcessing()) {
			if (input != prevInput) { haltProcess(); }
		}
	}
	
	@Override
	protected void haltProcess() {
		processTimeRemaining = -1;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items",  itemStackHandler.serializeNBT());
		return compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		return super.getCapability(capability, facing);
	}	
}
