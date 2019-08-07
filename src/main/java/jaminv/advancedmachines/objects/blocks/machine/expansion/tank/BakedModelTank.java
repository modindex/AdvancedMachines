package jaminv.advancedmachines.objects.blocks.machine.expansion.tank;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import jaminv.advancedmachines.client.BakedModelBase;
import jaminv.advancedmachines.client.quads.IModelQuad;
import jaminv.advancedmachines.client.quads.ModelQuadFluid;
import jaminv.advancedmachines.client.quads.ModelQuadLayeredBlock;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.model.LayeredTextureMultiblockTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;

public class BakedModelTank extends BakedModelBase {

	public BakedModelTank(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return getTexture("expansion.basic.all");
	}

	@Override
	public List<IModelQuad> render(VertexFormat format, IBlockState state, EnumFacing side, long rand) {
		List<IModelQuad> ret = new LinkedList<IModelQuad>();
		
		IExtendedBlockState ext = (IExtendedBlockState)state;
		FluidStack fluid = ext.getValue(Properties.FLUID);
		int capacity = ext.getValue(Properties.CAPACITY);
		
		ret.add(new ModelQuadLayeredBlock(format, new LayeredTextureMultiblockTransparent(state, "tank")));
		ret.add(new ModelQuadLayeredBlock(format, new LayeredTextureMultiblockTransparent(state, "tank")).offset(0.0001f, 0.0001f, 0.0001f).invert());

		if (fluid != null && capacity > 0) {
			ret.add(new ModelQuadFluid(format, fluid, fluid.amount / (float)capacity).offset(0.01f,  0.01f, 0.01f)); 
		}	
		

		return ret;
	}

}
