package jaminv.advancedmachines.objects.blocks.machine;

import jaminv.advancedmachines.lib.energy.EnergyStorageAdvanced;
import jaminv.advancedmachines.objects.material.MaterialExpansion;
import jaminv.advancedmachines.util.ModConfig;

public class MachineEnergyStorage extends EnergyStorageAdvanced {

	public MachineEnergyStorage() {
		super(ModConfig.general.defaultMachineEnergyCapacity * MaterialExpansion.maxMultiplier,
			ModConfig.general.defaultMachineEnergyTransfer * MaterialExpansion.maxMultiplier);		
	}

	public void setMaterial(MaterialExpansion material) {
		setEnergyCapacity(ModConfig.general.defaultMachineEnergyCapacity * material.getMultiplier());
		setMaxTransfer(ModConfig.general.defaultMachineEnergyTransfer * material.getMultiplier());
	}
}
