package jaminv.advancedmachines.machine;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.energy.EnergyStorageAdvanced;
import jaminv.advancedmachines.objects.variant.VariantExpansion;

public class MachineEnergyStorage extends EnergyStorageAdvanced {

	public MachineEnergyStorage() {
		super(ModConfig.general.defaultMachineEnergyCapacity * VariantExpansion.maxMultiplier,
			ModConfig.general.defaultMachineEnergyTransfer * VariantExpansion.maxMultiplier);		
	}

	public void setMaterial(VariantExpansion material) {
		setEnergyCapacity(ModConfig.general.defaultMachineEnergyCapacity * material.getMultiplier());
		setMaxTransfer(ModConfig.general.defaultMachineEnergyTransfer * material.getMultiplier());
	}
}
