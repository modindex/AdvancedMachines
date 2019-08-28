package jaminv.advancedmachines.machine.expansion;

import java.util.List;

import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;

public class ModelBakeryExpansionType implements ModelBakery {
	
	protected String variant;
	
	public ModelBakeryExpansionType(String variant) {
		this.variant = variant;
	}

	protected String getBaseTexture() { return "expansion"; }

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return (new QuadBuilderLayeredBlock(new LayeredTextureMultiblock(state, getBaseTexture()))).build();
	}
}
