package jaminv.advancedmachines.machine.expansion.energy;

import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockBase;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ModelBakeryEnergy implements ModelBakery {

	@Override
	public TextureAtlasSprite getParticleTexture(IBlockState state) {
		return MachineHelper.getParticleTexture("expansion", state);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return (new QuadBuilderLayeredBlock(
			BlockHelper.getExtendedFacing(state),
			new LayeredTextureMultiblockBase(state, "expansion"),
			new LayeredTextureMultiblockBase(state, "energy"))
		).build();
	}

}
