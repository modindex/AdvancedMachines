package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.property.IExtendedBlockState;

public class QuadBuilderMultiblock implements QuadBuilder {
	
	protected IExtendedBlockState state;
	protected MultiblockTextureBase base;
	protected TextureAtlasSprite face = null;
	
	public QuadBuilderMultiblock(IBlockState state, MultiblockTextureBase base) {
		this.state = (IExtendedBlockState)state;
		this.base = base;
	}
	
	public QuadBuilderMultiblock withFace(TextureAtlasSprite face) {
		this.face = face;
		return this;
	}
	
	
	@Override
	public List<BakedQuad> build() {
		LayeredTextureMultiblock texture = new LayeredTextureMultiblock(state, base);
		QuadBuilderLayeredBlock builder = new QuadBuilderLayeredBlock(texture).withTopBottom(texture.copy().withSided(TextureSide.TOP));
		if (face != null) { builder.withFace(MachineHelper.getExtendedFacing(state), LayeredTexture.of(face)); }
		return builder.build();
	}
}
