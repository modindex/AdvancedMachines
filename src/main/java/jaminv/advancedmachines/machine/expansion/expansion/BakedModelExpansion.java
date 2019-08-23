package jaminv.advancedmachines.machine.expansion.expansion;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import jaminv.advancedmachines.client.BakedModelBase;
import jaminv.advancedmachines.client.RawTextures;
import jaminv.advancedmachines.client.quads.IModelQuad;
import jaminv.advancedmachines.client.quads.ModelQuadBlock;
import jaminv.advancedmachines.client.quads.ModelQuadLayeredBlock;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.machine.multiblock.model.LayeredTextureMultiblockBase;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BakedModelExpansion extends BakedModelBase {
	
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

	public BakedModelExpansion(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return getTexture("expansion.basic.all");
	}

	@Override
	public List<IModelQuad> render(VertexFormat format, IBlockState state, EnumFacing side, long rand) {
		return Collections.singletonList(new ModelQuadLayeredBlock(format, 
			BlockHelper.getExtendedFacing(state),
			new LayeredTextureMultiblockBase(state, "expansion"),
			new LayeredTextureMultiblockMachineFace(state)
		));
	}

}
