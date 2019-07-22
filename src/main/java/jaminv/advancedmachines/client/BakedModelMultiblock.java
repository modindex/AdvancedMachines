package jaminv.advancedmachines.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.ICanHaveMachineFace;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.material.MaterialBase;
import jaminv.advancedmachines.util.parser.DataParserException;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class BakedModelMultiblock implements IBakedModel {
	
	protected class Border {
		String border = "";
		Border() {}
		
		protected Border append(String append, boolean dir) {
			if (!dir) { return this; }
			if (border != "") { border += "_"; }
			border += append;
			return this;
		}
		
		protected String getBorder() {
			if (border.equals("")) { return "none"; }
			if (border.equals("top_bottom_left_right")) { return "all"; }
			return border;
		}
	}
	
	public static final ModelResourceLocation BAKED_MODEL_MULTIBLOCK = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock");	

	private VertexFormat format;
	private Map<String, TextureAtlasSprite> sprites = new HashMap<String, TextureAtlasSprite>();
	
	public BakedModelMultiblock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		this.format = format;
		
		for (Map.Entry<String, ResourceLocation> entry : MultiblockTextures.resources.entrySet()) {
			sprites.put(entry.getKey(), bakedTextureGetter.apply(entry.getValue()));
		}	
	}
	
	private final Map<IBlockState, List<BakedQuad>> cache = new HashMap<IBlockState, List<BakedQuad>>();
	
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		//List<BakedQuad> cached = cache.get(state);
		//if (cached != null) { return cached; }
		
		List<BakedQuad> quads = new ArrayList<>();
		
		float a = 0f;
		float b = 1f;
		
		boolean bottom = state.getValue(BlockMachineExpansionBase.BORDER_BOTTOM);
		boolean top = state.getValue(BlockMachineExpansionBase.BORDER_TOP);
		boolean north = state.getValue(BlockMachineExpansionBase.BORDER_NORTH);
		boolean south = state.getValue(BlockMachineExpansionBase.BORDER_SOUTH);
		boolean east = state.getValue(BlockMachineExpansionBase.BORDER_EAST);
		boolean west = state.getValue(BlockMachineExpansionBase.BORDER_WEST);
		MaterialBase variant = state.getValue(BlockMaterial.EXPANSION_VARIANT);
		EnumFacing facing = state.getValue(BlockMachineExpansion.FACING);

		String baseresource = "expansion." + variant.getName() + ".";
		String tex;
		
		String border_north = new Border().append("top", top).append("bottom", bottom).append("left", east).append("right", west).getBorder();
		if ((tex = checkFace(state, EnumFacing.NORTH, border_north)) == null) { tex = baseresource + border_north; }
		quads.add(createQuad(new Vec3d(b, b, a), new Vec3d(b, a, a), new Vec3d(a, a, a), new Vec3d(a, b, a), sprites.get(tex)));
		
		String border_west = new Border().append("top", north).append("bottom", south).append("left", bottom).append("right", top).getBorder();
		quads.add(createQuad(new Vec3d(a, a, a), new Vec3d(a, a, b), new Vec3d(a, b, b), new Vec3d(a, b, a), sprites.get(baseresource + border_west)));

		String border_bottom = new Border().append("top", west).append("bottom", east).append("left", north).append("right", south).getBorder();
		quads.add(createQuad(new Vec3d(a, a, a), new Vec3d(b, a, a), new Vec3d(b, a, b), new Vec3d(a, a, b), sprites.get(baseresource + border_bottom)));
		
		String border_east = new Border().append("top", bottom).append("bottom", top).append("left", north).append("right", south).getBorder();
		quads.add(createQuad(new Vec3d(b, a, a), new Vec3d(b, b, a), new Vec3d(b, b, b), new Vec3d(b, a, b), sprites.get(baseresource + border_east)));
		
		String border_top = new Border().append("top", east).append("bottom", west).append("left", north).append("right", south).getBorder();
		quads.add(createQuad(new Vec3d(b, b, a), new Vec3d(a, b, a), new Vec3d(a, b, b), new Vec3d(b, b, b), sprites.get(baseresource + border_top)));

		String border_south = new Border().append("top", west).append("bottom", east).append("left", bottom).append("right", top).getBorder();
		quads.add(createQuad(new Vec3d(a, a, b), new Vec3d(b, a, b), new Vec3d(b, b, b), new Vec3d(a, b, b), sprites.get(baseresource + border_south)));
		
		cache.put(state, quads);
		return quads;
	}
	
    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex, float u, float v) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(tex);
        putVertex(builder, normal, tex, v1.x, v1.y, v1.z, u, v);
        putVertex(builder, normal, tex, v2.x, v2.y, v2.z, u, v + 16);
        putVertex(builder, normal, tex, v3.x, v3.y, v3.z, u + 16, v + 16);
        putVertex(builder, normal, tex, v4.x, v4.y, v4.z, u + 16, v);
        return builder.build();
    }
    
    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex) {
    	return createQuad(v1, v2, v3, v4, tex, 0, 0);
    }
    
    protected String checkFace(IBlockState state, EnumFacing side, String border) {
    	String face = state.getValue(ICanHaveMachineFace.MACHINE_FACE).getName();
    	if (face == "none") { return null; }
    	
    	return "furnace.inactive.basic." + face;
    }
    
    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, TextureAtlasSprite tex, double x, double y, double z, float u, float v) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = tex.getInterpolatedU(u);
                        v = tex.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }	

	@Override
	public boolean isAmbientOcclusion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGui3d() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return sprites.get("expansion.basic.all");
	}

	@Override
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList(Lists.<ItemOverride> newArrayList()) {
		@Override
		public IBakedModel handleItemState(IBakedModel model, ItemStack stack, World world, EntityLivingBase entity) {
			return BakedModelMultiblock.this;
		}
	};

}
