package jaminv.advancedmachines.machine.expansion.tank;

import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderFluid;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;

public class ModelBakeryMachineTank implements ModelBakery {

	@Override
	public TextureAtlasSprite getParticleTexture(String variant) {
		return MachineHelper.getParticleTexture("expansion", variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		List<BakedQuad> ret = new LinkedList<BakedQuad>();
		
		IExtendedBlockState ext = (IExtendedBlockState)state;
		FluidStack fluid = ext.getValue(Properties.FLUID);
		int capacity = ext.getValue(Properties.CAPACITY);
		
		MultiblockBorders borders = new MultiblockBorders((IExtendedBlockState)state);
		float xmin = 0f, xmax = 0f, ymin = 0f, ymax = 0f, zmin = 0f, zmax = 0f;
		float offset = 0.01f;
		if (borders.getTop() == MultiblockBorderType.SOLID) { ymax = offset; }
		if (borders.getBottom() == MultiblockBorderType.SOLID) { ymin = offset; }
		if (borders.getNorth() == MultiblockBorderType.SOLID) { zmin = offset; }
		if (borders.getSouth() == MultiblockBorderType.SOLID) { zmax = offset; }
		if (borders.getWest() == MultiblockBorderType.SOLID) { xmin = offset; }
		if (borders.getEast() == MultiblockBorderType.SOLID) { xmax = offset; }
		
		LayeredTexture side = new LayeredTextureMultiblockTransparent(state, "tank");
		LayeredTexture top = new LayeredTextureMultiblockTransparent(state, "tank").withSided(SidedTexture.TOP);
		
		ret.addAll(new QuadBuilderLayeredBlock(side).withTopBottom(top).build());
		ret.addAll(new QuadBuilderLayeredBlock(side).withTopBottom(top).offset(xmin, xmax, ymin, ymax, zmin, zmax).invert().build());

		if (fluid != null && capacity > 0) {
			ret.addAll(new QuadBuilderFluid(fluid, fluid.amount / (float)capacity).offset(0.02f,  0.02f, 0.02f).build()); 
		}		
		return ret;
	}

}
