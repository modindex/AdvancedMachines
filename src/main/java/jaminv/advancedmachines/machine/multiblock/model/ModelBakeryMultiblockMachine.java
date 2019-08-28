package jaminv.advancedmachines.machine.multiblock.model;

import java.util.List;

import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMultiblockMachine implements ModelBakery {
	
	String variant;
	public ModelBakeryMultiblockMachine(String variant) {
		this.variant = variant;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return MachineHelper.getParticleTexture("expansion", this.variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		IExtendedBlockState ext = (IExtendedBlockState)state;
		
		return new QuadBuilderLayeredBlock(new LayeredTextureMultiblock(state, "expansion"))
			.withFace(BlockHelper.getExtendedFacing(state), 
				new LayeredTextureMultiblockMachine(state, ext.getValue(Properties.MACHINE_TYPE).getName()).withBase("expansion"))
			.withTopBottom(new LayeredTextureMultiblock(state, "expansion").withSided(SidedTexture.TOP))
		.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem("expansion", stack).build();
	}
}
