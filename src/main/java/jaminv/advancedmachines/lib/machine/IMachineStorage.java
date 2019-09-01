package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerInternal;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.recipe.RecipeManager;

public interface IMachineStorage extends IItemHandlerMachine, IFluidHandlerInternal, IEnergyStorageAdvanced {
	public IItemHandlerMachine getInventory();
	public IFluidHandlerInternal getInputTanks();
	public IFluidHandlerInternal getOutputTanks();
	public IEnergyStorageInternal getEnergy();
}