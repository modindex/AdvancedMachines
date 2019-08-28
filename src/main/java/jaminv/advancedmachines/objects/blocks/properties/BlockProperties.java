package jaminv.advancedmachines.objects.blocks.properties;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class BlockProperties {
	
	public static final BlockProperties MACHINE = new BlockProperties().setCreativeTab(CreativeTabs.DECORATIONS).setHardness(3.5f);

	CreativeTabs tab;
	float hardness;
	
	public void apply(Block block) {
		block.setCreativeTab(tab);
		block.setHardness(hardness);
	}
	
	public BlockProperties setCreativeTab(CreativeTabs tab) {
		this.tab = tab; return this;
	}
	
	public BlockProperties setHardness(float hardness) {
		this.hardness = hardness; return this;
	}
}
