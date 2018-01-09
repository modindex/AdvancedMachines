package jaminv.advancedmachines.objects.blocks.machine;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.RecipeOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityMachineBase extends TileEntity implements ITickable {

	public abstract int getInputCount();
	public abstract int getOutputCount();
	public abstract int getSecondaryCount();
	public int getFirstInputSlot() { return 0; }
	public int getFirstOutputSlot() { return getInputCount(); }
	public int getFirstSecondarySlot() { return getInputCount() + getOutputCount(); }
	public int getInventorySize() { return getInputCount() + getOutputCount() + getSecondaryCount(); }
	
	protected ItemStackHandler inventory;
			
	protected class ItemStackHandlerBase extends ItemStackHandler {
		public ItemStackHandlerBase(int size) {
			super(size);
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityMachineBase.this.markDirty();
		}
		
	    @Override
	    @Nonnull
	    public ItemStack getStackInSlot(int slot)
	    {
	    	return super.getStackInSlot(slot);
	    }		
	};
	
	public TileEntityMachineBase() {
		super();
		this.inventory = new ItemStackHandlerBase(getInventorySize());
	}
	
	private int tick;
	private RecipeInput[] prevInput = new RecipeInput[getInputCount()];
		
	@Override
	public void update() {
		tick++;
		if (tick < Config.tickUpdate) { return; }
		
		Main.logger.log(Level.DEBUG, "tick");
		
		RecipeInput[] input = getInput();
		checkInventoryChanges(input);
		
		tick = 0;
		if (canProcess(input)) {
			Main.logger.log(Level.DEBUG, "canProcess");
			process(input);
		} else {
			haltProcess();
		}
	}
	
	protected RecipeInput[] getInput() {
		RecipeInput[] input = new RecipeInput[getInputCount()];
		for (int i = 0; i < getInputCount(); i++) {
			input[i] = new RecipeInput(inventory.getStackInSlot(getFirstInputSlot() + i));
		}
		return input;
	}
	
	private void checkInventoryChanges(RecipeInput[] input) {
		for (int i = 0; i < getInputCount(); i++) {
			if (prevInput[i] == null || input[i] == null) {
				Main.logger.log(Level.DEBUG, "Inventory changed 1");
				prevInput = input;
				haltProcess(); return; 
			}
			if (!prevInput[i].equals(input[i])) {
				Main.logger.log(Level.DEBUG, "Inventory changed 2");
				prevInput = input;
				haltProcess(); return;
			}
		}
		prevInput = input;
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
			if (inventory.insertItem(i, item, true).isEmpty()) {
				if (!simulate) { inventory.insertItem(i, item, false); }
				return true;
			}
		}
		return false;
	}
	
	protected void outputSecondary(NonNullList<RecipeOutput> secondary) {		
		for (RecipeOutput output : secondary) {
			if (world.rand.nextInt(100) > output.getChance()) { continue; }
			ItemStack item = output.toItemStack();
			for(int slot = getFirstSecondarySlot(); slot < getSecondaryCount() + getFirstSecondarySlot(); slot++) {
				if (inventory.insertItem(slot, item, true).isEmpty()) {
					inventory.insertItem(slot, item, false);
					break;
				}
			}
		}
	}
	
	public abstract boolean isProcessing();
	public abstract boolean canProcess(RecipeInput[] input);
	protected abstract void process(RecipeInput[] input);
	protected abstract void haltProcess();
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

}
