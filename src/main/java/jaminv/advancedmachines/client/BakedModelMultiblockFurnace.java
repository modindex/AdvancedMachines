package jaminv.advancedmachines.client;

import java.util.function.Function;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.BlockMachineBase;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.material.MaterialBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedModelMultiblockFurnace extends BakedModelMultiblock {

	public static final ModelResourceLocation RESOURCELOCATION = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblockfurnace");		
	
	public BakedModelMultiblockFurnace(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}

	@Override
	protected String checkFace(IBlockState state, EnumFacing side, Border border) {
		String face;
		if ((face = super.checkFace(state, side, border)) != null) { return face; }
		
		EnumFacing facing = state.getValue(BlockMachineBase.FACING);
		MaterialBase variant = state.getValue(BlockMaterial.EXPANSION_VARIANT);
		boolean active = state.getValue(BlockMachineBase.ACTIVE);
		if (facing == side) { return "furnace." + (active ? "active" : "inactive") + "." + variant + "." + border.getBorder(); }
		return null;
	}
}
