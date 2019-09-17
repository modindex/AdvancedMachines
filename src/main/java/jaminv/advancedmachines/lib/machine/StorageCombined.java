package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.FluidHandlerAdditional;
import jaminv.advancedmachines.lib.fluid.FluidHandler;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.lib.recipe.RecipeManager;

public interface StorageCombined extends ItemHandlerSeparated, FluidHandler, IEnergyStorageAdvanced {
	public ItemHandlerSeparated getInventory();
	public FluidHandler getInputTanks();
	public FluidHandler getOutputTanks();
	public IEnergyStorageInternal getEnergy();
}