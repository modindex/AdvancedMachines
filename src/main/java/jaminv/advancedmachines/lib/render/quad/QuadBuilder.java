package jaminv.advancedmachines.lib.render.quad;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;

public interface QuadBuilder {
	public List<BakedQuad> build();
}
