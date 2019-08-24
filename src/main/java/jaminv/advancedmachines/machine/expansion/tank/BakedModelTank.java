package jaminv.advancedmachines.machine.expansion.tank;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.BakedModelImpl;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderFluid;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;

public class BakedModelTank extends BakedModelImpl {

	public BakedModelTank(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return getTexture("expansion.basic.all");
	}

	@Override
	public List<QuadBuilder> render(VertexFormat format, IBlockState state, EnumFacing side, long rand) {
		List<QuadBuilder> ret = new LinkedList<QuadBuilder>();
		
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
		
		ret.add(new QuadBuilderLayeredBlock(format, new LayeredTextureMultiblockTransparent(state, "tank")));
		ret.add(new QuadBuilderLayeredBlock(format, new LayeredTextureMultiblockTransparent(state, "tank")).offset(xmin, xmax, ymin, ymax, zmin, zmax).invert());

		if (fluid != null && capacity > 0) {
			ret.add(new QuadBuilderFluid(format, fluid, fluid.amount / (float)capacity).offset(0.02f,  0.02f, 0.02f)); 
		}		

		return ret;
	}

}
