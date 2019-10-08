package jaminv.advancedmachines.objects.blocks.tank;

import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.fluid.FluidTankDefault;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.Cuboid;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderBlock;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderFluid;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.render.RawTextures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;

public class ModelBakeryTank implements ModelBakery {

	protected List<BakedQuad> bake(FluidStack fluid, int capacity) {
		List<BakedQuad> ret = new LinkedList<BakedQuad>();

		Texture side = RawTextures.get("tank.side");
		Texture top = RawTextures.get("tank.top");
		
		Cuboid tank = Cuboid.UNIT.offset(2/16f, 0, 2/16f);
		
		ret.addAll(new QuadBuilderBlock(side).withTopBottom(top).withCuboid(tank).build());
		ret.addAll(new QuadBuilderBlock(side).withTopBottom(top).withCuboid(tank.offset(0.01f, 0.01f, 0.01f)).invert().build());

		if (fluid != null && capacity > 0) {
			QuadBuilderFluid quad = new QuadBuilderFluid(fluid, fluid.amount / (float)capacity);
			quad.withCuboid(quad.getCuboid().offset(2/16f + 0.02f, 0.02f, 2/16f + 0.02f));
			ret.addAll(quad.build()); 
		}		
		return ret;
	}
	
	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
		FluidStack fluid = ext.getValue(Properties.FLUID);
		int capacity = ext.getValue(Properties.CAPACITY);
				
		return bake(fluid, capacity);
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		FluidStack fluid = null;
		int capacity = 0;
		
		if (stack.hasTagCompound()) {
			FluidTankDefault tank = new FluidTankDefault(
					ModConfig.general.defaultMachineFluidCapacity,
					ModConfig.general.defaultMachineFluidTransfer);
			tank.deserializeNBT(stack.getTagCompound().getCompoundTag("tank"));
			
			fluid = tank.getFluid();
			capacity = tank.getCapacity();
		}
		
		return bake(fluid, capacity);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return RawTextures.get("tank.top").getSprite();
	}
}
