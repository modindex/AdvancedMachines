package jaminv.advancedmachines.objects.blocks;

import jaminv.advancedmachines.lib.util.blocks.BlockProperties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPropertiesMod {
	
	public static final BlockProperties MACHINE = new BlockProperties.Builder(Material.IRON)
		.setSoundType(SoundType.METAL).setCreativeTab(CreativeTabs.DECORATIONS)
		.setHardness(3.5f).setHarvestLevel("pickaxe", 1).build();
	
	public static final BlockProperties GLASS = new BlockProperties.Builder(Material.GLASS)
		.setSoundType(SoundType.GLASS).setCreativeTab(CreativeTabs.DECORATIONS)
		.setHardness(2.0f).setHarvestLevel("pickaxe", 0).build();
}
