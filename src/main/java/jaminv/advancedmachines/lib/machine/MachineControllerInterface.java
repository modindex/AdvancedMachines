package jaminv.advancedmachines.lib.machine;

import javax.annotation.Nullable;

import jaminv.advancedmachines.lib.energy.EnergyStorage;
import jaminv.advancedmachines.lib.fluid.FluidHandler;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;
import jaminv.advancedmachines.lib.recipe.RecipeManager;

/**
 * This interface is largely to prevent objects from being tightly coupled to the machine controller.
 * The machine controller is quite complex and it's unlikely that I'll have a need to create another
 * implementation of this interface. Instead, this interface is used to define how external objects
 * can interact with the controller. This is largely for observers and other objects that are passed
 * the controller via parameters. The creator of the controller can (and should) call tick(), but
 * observers should not be able.  
 */
public interface MachineControllerInterface extends DoesProcess {
	public interface SubController {
		public void setController(@Nullable MachineControllerInterface controller);
		public default boolean preProcess(MachineControllerInterface controller) { return false; }
		public default boolean postProcess(MachineControllerInterface controller) { return false; }
		public int getPriority();
	}	
	
	public void wake();
	
	/**
	 * Sort SubControllers
	 * 
	 * A subcontroller should call this when it's priority is modified.
	 */
	public void sortSubControllers();
	
	public ItemHandlerSeparated getInventory();
	public FluidHandler getFluidTank();
	public EnergyStorage getEnergy();
	public RecipeManager getRecipeManager();
	
	public MachineTile getMachine();
}
