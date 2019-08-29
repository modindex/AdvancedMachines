package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.Texture;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMultiblockMachine implements ModelBakery {
	
	VariantExpansion variant;
	public ModelBakeryMultiblockMachine(VariantExpansion variant) {
		this.variant = variant;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return MultiblockTextureBase.MULTIPLY.getParticleTexture(variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
		
		Texture face = new Texture(RawTextures.get(ext.getValue(Properties.MACHINE_TYPE).getName(),
				ext.getValue(Properties.ACTIVE) ? "active" : "inactive"));
		
		return new QuadBuilderMultiblock(state, MultiblockTextureBase.MULTIPLY).withFace(face).build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem(stack, MultiblockTextureBase.MULTIPLY).build();
	}
}
