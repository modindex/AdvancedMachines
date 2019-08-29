package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.lib.render.quad.LayeredTextureList;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.machine.MachineHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.common.property.IExtendedBlockState;

public class QuadBuilderMultiblock implements QuadBuilder {
	
	protected IExtendedBlockState state;
	protected MultiblockTextureBase base;
	protected Texture face = null;
	
	public QuadBuilderMultiblock(IBlockState state, MultiblockTextureBase base) {
		this.state = (IExtendedBlockState)state;
		this.base = base;
	}
	
	public QuadBuilderMultiblock withFace(Texture face) {
		this.face = face;
		return this;
	}
	
	
	@Override
	public List<BakedQuad> build() {
		LayeredTextureMultiblock texture = new LayeredTextureMultiblock(state, base);
		QuadBuilderLayeredBlock builder = new QuadBuilderLayeredBlock(texture).withTopBottom(texture.copy().withSided(TextureSide.TOP));
		if (face != null) { builder.withFace(MachineHelper.getExtendedFacing(state), new LayeredTextureList(face)); }
		return builder.build();
	}
}
