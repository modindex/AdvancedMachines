package jaminv.advancedmachines.machine.expansion.inventory;

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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class ModelBakeryInventory implements ModelBakery {
	
	protected static class LayeredTextureInventory extends LayeredTextureMultiblockBase {
		public LayeredTextureInventory(IBlockState state) {	super(state, "expansion"); }

		@Override
		protected TextureAtlasSprite getBaseTexture(String variant) {
			return RawTextures.get("inventory", getState().getValue(Properties.INPUT) ? "input" : "output", variant, "base");
		}
	}	

	@Override
	public TextureAtlasSprite getParticleTexture(IBlockState state) {
		return MachineHelper.getParticleTexture("expansion", state);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return (new QuadBuilderLayeredBlock(
			BlockHelper.getExtendedFacing(state),
			new LayeredTextureMultiblockBase(state, "expansion"),
			new LayeredTextureInventory(state))
		).build();
	}

}
