package jaminv.advancedmachines.lib.machine;

import javax.annotation.Nullable;

import jaminv.advancedmachines.lib.energy.IEnergyStorageInternal;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * This interface is largely to prevent objects from being tightly coupled to the machine controller.
 * The machine controller is quite complex and it's unlikely that I'll have a need to create another
 * implementation of this interface. Instead, this interface is used to define how external objects
 * can interact with the controller. This is largely for observers and other objects that are passed
 * the controller via parameters. The creator of the controller can (and should) call tick(), but
 * observers should not be able.  
 */
public interface IMachineController extends IMachineProcess {
	public interface ISubController {
		public void setController(@Nullable IMachineController controller);
		public default boolean preProcess(IMachineController controller) { return false; }
		public default boolean postProcess(IMachineController controller) { return false; }
		public int getPriority();
	}	
	
	public void wake();
	
	/**
	 * Sort SubControllers
	 * 
	 * A subcontroller should call this when it's priority is modified.
	 */
	public void sortSubControllers();
	
	public IItemHandlerMachine getInventory();
	public IFluidHandler getFluidTank();
	public IEnergyStorageInternal getEnergy();
	public IRecipeManager getRecipeManager();
}
