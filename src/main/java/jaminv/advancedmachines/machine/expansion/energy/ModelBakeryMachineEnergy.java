package jaminv.advancedmachines.machine.expansion.energy;

import java.util.Collections;
import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblock;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ModelBakeryMachineEnergy implements ModelBakery {

	@Override
	public TextureAtlasSprite getParticleTexture(String variant) {
		return MachineHelper.getParticleTexture("expansion", variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return new QuadBuilderLayeredBlock(new LayeredTextureMultiblock(state, "expansion"))
			.withFace(BlockHelper.getExtendedFacing(state),	new LayeredTextureMultiblock(state, "energy"))
			.withTopBottom(new LayeredTextureMultiblock(state, "expansion").withSided(SidedTexture.TOP))
		.build();
	}

}
