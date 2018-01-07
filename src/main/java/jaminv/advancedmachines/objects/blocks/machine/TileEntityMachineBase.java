package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.managers.machine.RecipeInput;
import jaminv.advancedmachines.util.managers.machine.RecipeOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityMachineBase extends TileEntity implements ITickable {

	public abstract int getInputCount();
	public abstract int getOutputCount();
	public int getFirstInputSlot() { return 0; }
	public int getFirstOutputSlot() { return getInputCount(); }
	public int getInventorySize() { return getInputCount() + getOutputCount(); }
	
	protected ItemStackHandler inventory = new ItemStackHandler(getInputCount() + getOutputCount()) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityMachineBase.this.markDirty();
		}
	};
	
	private int tick;
	private RecipeInput[] prevInput = new RecipeInput[getInputCount()];
	
	@Override
	public void update() {
		tick++;
		if (tick < Config.tickUpdate) { return; }
		
		RecipeInput[] input = getInput();
		checkInventoryChanges(input);
		
		tick = 0;
		if (canProcess(input)) {
			process(input);
		} else {
			haltProcess();
		}
	}
	
	protected RecipeInput[] getInput() {
		RecipeInput[] input = new RecipeInput[getInputCount()];
		for (int i = 0; i < getInputCount(); i++) {
			input[i] = new RecipeInput(inventory.getStackInSlot(getFirstInputSlot() + 1));
		}
		return input;
	}
	
	private void checkInventoryChanges(RecipeInput[] input) {
		for (int i = 0; i < getInputCount(); i++) {
			if (prevInput[i] == null || input[i] == null) { haltProcess(); return; }
			if (prevInput[i] != input[i]) {
				haltProcess(); return;
			}
		}
	}
	
	protected boolean removeInput(RecipeInput input) {
		for (int i = getFirstInputSlot(); i < getInputCount() + getFirstInputSlot(); i++) {
			RecipeInput slot = new RecipeInput(inventory.getStackInSlot(i));
			if (slot.doesMatch(input)) {
				inventory.extractItem(i, input.getCount(), false);
				return true;
			}
		}
		return false;
	}
	
	protected boolean outputItem(RecipeOutput output, boolean simulate) {
		ItemStack item = output.toItemStack();
		for (int i = getFirstOutputSlot(); i < getOutputCount() + getFirstOutputSlot(); i++) {
			if (inventory.insertItem(i, item, true) == null) {
				if (!simulate) { inventory.insertItem(i, item, false); }
				return true;
			}
		}
		return false;
	}
	
	public abstract boolean isProcessing();
	public abstract boolean canProcess(RecipeInput[] input);
	protected abstract void process(RecipeInput[] input);
	protected abstract void haltProcess();
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

}
