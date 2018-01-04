package jaminv.advancedmachines.objects.blocks;

import jaminv.advancedmachines.util.handlers.EnumHandler;
import net.minecraft.block.material.Material;

public class BlockMaterialOre extends BlockMaterial {
	public BlockMaterialOre(String name, Material material, float hardness) {
		super(name, "ore", material, hardness);
		for (EnumHandler.EnumMaterial variant : EnumHandler.EnumMaterial.values()) {
			setHarvestLevel("pickaxe", variant.getHarvestLevel(), getDefaultState().withProperty(VARIANT, variant));
		}
	}	
}
