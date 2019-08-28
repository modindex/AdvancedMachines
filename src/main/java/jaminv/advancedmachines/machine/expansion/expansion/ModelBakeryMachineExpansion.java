package jaminv.advancedmachines.machine.expansion.expansion;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.machine.multiblock.face.SidedTexture;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblock;
import jaminv.advancedmachines.machine.multiblock.model.QuadBuilderMultiblockItem;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMachineExpansion implements ModelBakery {
	
	protected String variant;
	
	public ModelBakeryMachineExpansion(String variant) {
		this.variant = variant;
	}

	protected static class LayeredTextureMultiblockMachineFace extends LayeredTextureMultiblock {

		public LayeredTextureMultiblockMachineFace(IBlockState state) {	super(state, "expansion"); }

		@Override
		protected TextureAtlasSprite getBaseTexture(String variant) {
			IExtendedBlockState ext = (IExtendedBlockState)getState();
			MachineType type = ext.getValue(Properties.MACHINE_TYPE);
			MachineFace face = ext.getValue(Properties.MACHINE_FACE);
			boolean active = ext.getValue(Properties.ACTIVE);
			
			if (face == MachineFace.NONE) {
				return RawTextures.get(getBase(), variant, "base");
			} else {
				return RawTextures.get(type.getName(), active ? "active" : "inactive", variant, face.getName());
			}
		}
	}		

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return MachineHelper.getParticleTexture("expansion", variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return new QuadBuilderLayeredBlock(new LayeredTextureMultiblock(state, "expansion"))
			.withFace(BlockHelper.getExtendedFacing(state),	new LayeredTextureMultiblockMachineFace(state))
			.withTopBottom(new LayeredTextureMultiblock(state, "expansion").withSided(SidedTexture.TOP))
		.build();
	}

	@Override
	public List<BakedQuad> bakeItemModel(ItemStack stack) {
		return new QuadBuilderMultiblockItem("expansion", stack).build();
	}
}
