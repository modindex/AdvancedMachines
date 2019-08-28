package jaminv.advancedmachines.machine.expansion;

import java.util.List;

import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.machine.multiblock.model.MultiblockTextureBase;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblock;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblockItem;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class ModelBakeryMachineExpansion implements ModelBakery {
	
	protected MultiblockTextureBase base;
	protected VariantExpansion variant;
	
	public ModelBakeryMachineExpansion(MultiblockTextureBase base, VariantExpansion variant) {
		this.base = base;
		this.variant = variant;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base.getParticleTexture(variant);
	}
	
	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return new QuadBuilderMultiblock(state, base).build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem(stack, base).build();
	}
}
