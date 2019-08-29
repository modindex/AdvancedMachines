package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

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
		return new QuadBuilderMultiblock(state, MultiblockTextureBase.MULTIPLY)
			.withFace(MachineHelper.getMachineFace(state))
			.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem(stack, MultiblockTextureBase.MULTIPLY).build();
	}
}
