package jaminv.advancedmachines.util.models;

import java.io.IOException;

import org.apache.logging.log4j.Level;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.util.material.MaterialBase;

public class ModelRegistry {
	public static void build() {
		/*
		try {
			BlockstateMaterial productivity = new BlockstateMaterial("machine_productivity", "machine/productivity/", MaterialBase.MaterialType.EXPANSION);
			productivity.make();
			
			BlockstateMachine grinder = new BlockstateMachine("machine_grinder", "machine/expansion/", "machine/instance/grinder/inactive/", "machine/instance/grinder/active/", MaterialBase.MaterialType.EXPANSION);
			grinder.make();

			BlockstateMachine purifier = new BlockstateMachine("machine_purifier", "machine/expansion/", "machine/instance/purifier/inactive/", "machine/instance/purifier/active/", MaterialBase.MaterialType.EXPANSION);
			purifier.make();
			
			BlockstateMachine alloy = new BlockstateMachine("machine_alloy", "machine/expansion/", "machine/instance/alloy/inactive/", "machine/instance/alloy/active/", MaterialBase.MaterialType.EXPANSION);
			alloy.make();
			
			BlockstateInventory inventory = new BlockstateInventory("machine_inventory", "machine/expansion/", "machine/inventory/input/", "machine/inventory/output/", MaterialBase.MaterialType.EXPANSION);
			inventory.make();

			BlockstateMaterialFace energy = new BlockstateMaterialFace("machine_energy", "machine/expansion/", "machine/energy/", MaterialBase.MaterialType.EXPANSION);
			energy.make();
			
			BlockstateMachine redstone = new BlockstateMachine("machine_redstone", "machine/expansion/", "machine/redstone/inactive/", "machine/redstone/active/", MaterialBase.MaterialType.EXPANSION);
			redstone.make();

			BlockstateMachine furnace = new BlockstateMachine("machine_furnace", "machine/expansion/", "machine/instance/furnace/inactive/", "machine/instance/furnace/active/", MaterialBase.MaterialType.EXPANSION);
			furnace.make();

			BlockstateMaterial expansion = new BlockstateMaterial("machine_expansion", "machine/expansion/", MaterialBase.MaterialType.EXPANSION);
			expansion.make();

			BlockstateMaterial speed = new BlockstateMaterial("machine_speed", "machine/speed/", MaterialBase.MaterialType.EXPANSION);
			speed.make();
		} catch (IOException e) {
			Main.logger.log(Level.ERROR, "Error creating model file", e.toString());
			e.printStackTrace();
		}
		*/
	}
}
