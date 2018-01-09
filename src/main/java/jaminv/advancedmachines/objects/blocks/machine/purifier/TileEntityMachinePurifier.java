package jaminv.advancedmachines.objects.blocks.machine.purifier;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.machine.TileEntityMachineBase;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.recipe.RecipeInput;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager.PurifierRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityMachinePurifier extends TileEntityMachineBase {
	@Override
	public int getInputCount() { return 1; }
	
	@Override
	public int getOutputCount() { return 1;	}
	
	@Override
	public int getSecondaryCount() { return 9; }
	
	private int processTimeRemaining = -1;
	private int totalProcessTime = 0;
	
	public float getProcessPercent() {
		if (totalProcessTime <= 0 || processTimeRemaining <= 0) { return 0.0f; }
		return ((float)totalProcessTime - processTimeRemaining + Config.tickUpdate) / totalProcessTime;
	}
	
	private RecipeInput lastInput = new RecipeInput();
	
	@Override
	public boolean isProcessing() {
		return processTimeRemaining > 0;
	}
	
	@Override
	public boolean canProcess(RecipeInput[] input) {
		PurifierRecipe recipe = PurifierManager.getRecipeManager().getRecipeMatch(input);
		if (recipe == null) { return false; }

		return outputItem(recipe.getOutput(0), true);
	}

	@Override
	protected void process(RecipeInput[] input) {
		if (!isProcessing()) {
			processTimeRemaining = Config.processTimeBasic;
			totalProcessTime = Config.processTimeBasic;
			return;
		}

		processTimeRemaining -= Config.tickUpdate;
		if (processTimeRemaining <= 0 && !world.isRemote) {
			PurifierRecipe recipe = PurifierManager.getRecipeManager().getRecipe(input);
			
			if (!removeInput(recipe.getInput(0))) {
				// Some kind of strange error
				Main.logger.log(Level.ERROR,  "error.machine.process.cannot_input");
				haltProcess();
				return;
			}
			
			if (!outputItem(recipe.getOutput(0), false)) {
				// Some kind of strange error
				Main.logger.log(Level.ERROR, "error.machine.process.cannot_output");
				haltProcess();
				return;
			}
			
			outputSecondary(recipe.getSecondary());
			
			processTimeRemaining = 0;
			return;
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
			inventory.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items",  inventory.serializeNBT());
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}	
}
