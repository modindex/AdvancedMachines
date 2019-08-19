package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.IEnergyStorageAdvanced;
import jaminv.advancedmachines.lib.fluid.IFluidHandlerMachine;
import jaminv.advancedmachines.lib.inventory.IItemHandlerMachine;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IMachineStorage extends IItemHandlerMachine, IFluidHandlerMachine, IEnergyStorageAdvanced {}