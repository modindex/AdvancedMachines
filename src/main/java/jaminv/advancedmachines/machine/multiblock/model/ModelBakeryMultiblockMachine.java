package jaminv.advancedmachines.machine.multiblock.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.client.textureset.TextureSets;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.BakedModelImpl;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.QuadBuilder;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderBlock;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockBase;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMultiblockMachine implements ModelBakery {

	@Override
	public TextureAtlasSprite getParticleTexture(String variant) {
		return MachineHelper.getParticleTexture("expansion", variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
		
		return new QuadBuilderLayeredBlock(
			BlockHelper.getExtendedFacing(state),
			new LayeredTextureMultiblockBase(state, "expansion"),
			new LayeredTextureMultiblockMachine(state, ext.getValue(Properties.MACHINE_TYPE).getName()))
		.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderBlock.Unit(RawTextures.get("expansion.basic.all")).build();
	}
}
