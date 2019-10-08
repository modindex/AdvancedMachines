package jaminv.advancedmachines.machine.expansion.tank;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jaminv.advancedmachines.ModConfig;
import jaminv.advancedmachines.lib.fluid.FluidTankDefault;
import jaminv.advancedmachines.lib.render.quad.Cuboid;
import jaminv.advancedmachines.lib.render.quad.LayeredTexture;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderFluid;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.expansion.ModelBakeryMachineExpansion;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorderType;
import jaminv.advancedmachines.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockTransparent;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblockItem;
import jaminv.advancedmachines.machine.multiblock.model.TextureSide;
import jaminv.advancedmachines.objects.blocks.Properties;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;

public class ModelBakeryMachineTank extends ModelBakeryMachineExpansion {
	public ModelBakeryMachineTank(VariantExpansion variant) {
		super(MultiblockTextureBase.TANK, variant);
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
		
		LayeredTexture side = new LayeredTextureMultiblockTransparent(state, base);
		LayeredTexture top = new LayeredTextureMultiblockTransparent(state, base).withSided(TextureSide.TOP);
		
		ret.addAll(new QuadBuilderLayeredBlock(side).withTopBottom(top).build());
		ret.addAll(new QuadBuilderLayeredBlock(side).withTopBottom(top).withCuboid(Cuboid.UNIT.offset(xmin, ymin, zmin, xmax , ymax, zmax)).invert().build());

		if (fluid != null && capacity > 0) {
			QuadBuilderFluid quad = new QuadBuilderFluid(fluid, fluid.amount / (float)capacity);
			quad.withCuboid(quad.getCuboid().offset(0.02f, 0.02f, 0.02f));
			ret.addAll(quad.build()); 
		}		
		return ret;
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		List<BakedQuad> quads = new ArrayList<>();
		quads.addAll(new QuadBuilderMultiblockItem(stack, base).build());
		quads.addAll(new QuadBuilderMultiblockItem(stack, base).invert().build());
		
		if (stack.hasTagCompound()) {
			FluidTankDefault tank = new FluidTankDefault(
					ModConfig.general.defaultMachineFluidCapacity * variant.getMultiplier(),
					ModConfig.general.defaultMachineFluidTransfer * variant.getMultiplier());
			tank.deserializeNBT(stack.getTagCompound().getCompoundTag("tank"));
			if (tank.getFluid() != null && tank.getFluidAmount() > 0) {
				QuadBuilderFluid quad = new QuadBuilderFluid(tank.getFluid(),
						tank.getFluidAmount() / (float)tank.getCapacity());
				quad.withCuboid(quad.getCuboid().offset(0.02f, 0.02f, 0.02f));
				quads.addAll(quad.build()); 
			}
		}
		return quads;
	}
}
