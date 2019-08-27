package jaminv.advancedmachines.objects.blocks;

import jaminv.advancedmachines.objects.variant.MaterialBase;
import jaminv.advancedmachines.objects.variant.MaterialBaseOre;
import net.minecraft.block.material.Material;

public abstract class BlockMaterialOre extends BlockMaterial {
	public BlockMaterialOre(String name, MaterialBase.MaterialType type, Material material, float hardness) {
		super(name, type, "ore", material, hardness);
		for (MaterialBase variant : MaterialBase.values(type)) {
			if (variant instanceof MaterialBaseOre) {
				setHarvestLevel("pickaxe", ((MaterialBaseOre)variant).getHarvestLevel(), getDefaultState().withProperty(VARIANT, variant));
			}
		}
	}	
}
