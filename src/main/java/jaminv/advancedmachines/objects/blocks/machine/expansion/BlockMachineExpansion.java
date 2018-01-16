package jaminv.advancedmachines.objects.blocks.machine.expansion;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.items.material.MaterialBase.MaterialType;
import jaminv.advancedmachines.objects.items.material.MaterialBase;
import jaminv.advancedmachines.objects.items.material.PropertyMaterial;
import net.minecraft.block.material.Material;

public class BlockMachineExpansion extends BlockMaterial {

	public BlockMachineExpansion(String name) {
		super(name, MaterialBase.MaterialType.EXPANSION, null, Material.IRON, 5.0f);
	}

	@Override
	protected PropertyMaterial getVariant() {
		return PropertyMaterial.create("variant", MaterialBase.MaterialType.EXPANSION);
	}

}
