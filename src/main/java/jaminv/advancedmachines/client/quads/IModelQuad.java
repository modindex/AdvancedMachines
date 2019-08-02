package jaminv.advancedmachines.client.quads;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;

public interface IModelQuad {
	public List<BakedQuad> getQuads();
}
