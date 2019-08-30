package jaminv.advancedmachines.lib.util.blocks;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

@Immutable
public final class BlockProperties {
	
	/** BlockProperties.ORE does not set the harvest level */
	public static final BlockProperties ORE = new Builder(Material.ROCK).setSoundType(SoundType.STONE)
		.setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(3.0f)
		.build();
	
	public static final BlockProperties STORAGE = new Builder(Material.IRON).setSoundType(SoundType.METAL)
		.setCreativeTab(CreativeTabs.DECORATIONS).setHardness(5.0f).setHarvestLevel("pickaxe", 1)
		.build();
	
	/**
	 * A simple override of Block to provide in-line declaration of a block with properties.
	 * Made final to prevent overriding. Use composition for more complex blocks.
	 */
	public static final class Block extends net.minecraft.block.Block {
		public Block(BlockProperties props) {
			super(props.getMaterial());
			props.apply(this);
			if (props.getSoundType() != null) { setSoundType(props.getSoundType()); }
		}	
	}
	
	/**
	 * BlockProperties uses a Builder class to make properties immutable
	 */
	public static class Builder {
		BlockProperties props;
		
		public Builder(Material material) {
			this.props = new BlockProperties(material);
		}		
		
		public Builder setSoundType(SoundType soundType) {
			props.soundType = soundType; return this;
		}
		
		public Builder setCreativeTab(CreativeTabs tab) {
			props.creativeTab = tab; return this;
		}
		
		public Builder setHardness(float hardness) {
			props.hardness = hardness; return this;
		}
		
		public Builder setHarvestLevel(String toolClass, int level) {
			props.harvestLevel = Pair.of(toolClass, level); return this;
		}
		
		/**
		 * Build the BlockProperties Object
		 * The builder is invalidated after this operation. Any further operations will throw an exception.
		 */
		public BlockProperties build() {
			BlockProperties ret = props;
			props = null;
			return ret;
		}
	}

	protected Material material;
	protected SoundType soundType = null;
	protected CreativeTabs creativeTab = null;
	protected float hardness = -1.0f;
	protected Pair<String, Integer> harvestLevel = null;
	
	protected BlockProperties(Material material) {
		this.material = material;
	}
	
	public void apply(net.minecraft.block.Block block) {
		if (creativeTab != null) { block.setCreativeTab(creativeTab); }
		if (hardness != 1.0f) { block.setHardness(hardness); }
		if (harvestLevel != null) { block.setHarvestLevel(harvestLevel.getLeft(), harvestLevel.getRight()); }
	}	
	
	public Material getMaterial() {	return material; }
	public SoundType getSoundType() { return soundType;	}
	public CreativeTabs getCreativeTab() { return creativeTab; }
	public float getHardness() { return hardness; }
	
	protected BlockProperties copy() {
		BlockProperties ret = new BlockProperties(material);
		ret.soundType = soundType;
		ret.creativeTab = creativeTab;
		ret.hardness = hardness;
		ret.harvestLevel = harvestLevel;
		return ret;
	}
	
	/** Returns a copy */
	public BlockProperties withHarvestLevel(String toolClass, int level) {
		BlockProperties ret = copy();
		ret.harvestLevel = Pair.of(toolClass, level);
		return ret;
	}
}
