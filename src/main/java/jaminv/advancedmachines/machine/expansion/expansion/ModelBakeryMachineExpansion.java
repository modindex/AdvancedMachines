package jaminv.advancedmachines.machine.expansion.expansion;

import java.util.List;

import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.lib.render.ModelBakery;
import jaminv.advancedmachines.lib.render.quad.QuadBuilderLayeredBlock;
import jaminv.advancedmachines.machine.MachineHelper;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockBase;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBakeryMachineExpansion implements ModelBakery {
	
	protected static class LayeredTextureMultiblockMachineFace extends LayeredTextureMultiblockBase {

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
	public TextureAtlasSprite getParticleTexture(String variant) {
		return MachineHelper.getParticleTexture("expansion", variant);
	}

	@Override
	public List<BakedQuad> bakeModel(IBlockState state) {
		return (new QuadBuilderLayeredBlock( 
			BlockHelper.getExtendedFacing(state),
			new LayeredTextureMultiblockBase(state, "expansion"),
			new LayeredTextureMultiblockMachineFace(state)
		)).build();
	}

}
