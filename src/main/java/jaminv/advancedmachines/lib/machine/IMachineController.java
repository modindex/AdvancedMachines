package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerInternal;
import jaminv.advancedmachines.lib.inventory.IItemHandlerInternal;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;

/**
 * This interface is largely to prevent objects from being tightly coupled to the machine controller.
 * The machine controller is quite complex and it's unlikely that I'll have a need to create another
 * implementation of this interface. Instead, this interface is used to define how external objects
 * can interact with the controller. This is largely for observers and other objects that are passed
 * the controller via parameters. The creator of the controller can (and should) call tick(), but
 * observers should not be able.  
 */
public interface IMachineController {
	public interface IObserver {
		public default boolean preProcess(IMachineController controller) { return false; }
		public default boolean postProcess(IMachineController controller) { return false; }
	}	
	
	public float getProcessPercent();
	public boolean isProcessing();
	public int getQtyProcessing();
	
	public IItemHandlerInternal getInventory();
	public IFluidHandlerInternal getFluidTank();
	public IEnergyStorageInternal getEnergy();
	public IRecipeManager getRecipeManager();
}
