package jaminv.advancedmachines.machine.expansion;

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

public abstract class BakedModelExpansionBase extends BakedModelBase {
	
	public BakedModelExpansionBase(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
	
	protected String getBaseTexture() { return "expansion"; }

	@Override
	public List<IModelQuad> render(VertexFormat format, IBlockState state, EnumFacing side, long rand) {
		return Collections.singletonList(new ModelQuadLayeredBlock(format, 
			new LayeredTextureMultiblockBase(state, getBaseTexture())
		));
	}

}
