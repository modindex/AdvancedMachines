package jaminv.advancedmachines.lib.machine;

import jaminv.advancedmachines.lib.energy.EnergyStorage;
import jaminv.advancedmachines.lib.fluid.FluidHandler;
import jaminv.advancedmachines.lib.inventory.ItemHandlerSeparated;

public interface StorageCombined extends ItemHandlerSeparated, FluidHandler, EnergyStorage {}
