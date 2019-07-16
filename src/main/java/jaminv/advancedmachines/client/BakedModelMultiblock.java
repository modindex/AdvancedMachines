package jaminv.advancedmachines.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import jaminv.advancedmachines.util.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
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
	
	public static final ModelResourceLocation BAKED_MODEL_MULTIBLOCK = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock");	

	private VertexFormat format;
	private TextureAtlasSprite sprite;
	private TextureAtlasSprite down;
		
	public BakedModelMultiblock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		this.format = format;
		down = bakedTextureGetter.apply(new ResourceLocation(Reference.MODID, "blocks/machine/border/bottom"));
		//sprite = bakedTextureGetter.apply(new ResourceLocation(Reference.MODID, "blocks/machine/expansion/basic/none"));
	}
	
	private final Map<IBlockState, List<BakedQuad>> cache = new HashMap<IBlockState, List<BakedQuad>>();
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> cached = cache.get(state);
		if (cached != null) { return cached; }
		
		List<BakedQuad> quads = new ArrayList<>();
		
		float a = 1f / 16f;
		float b = 15f / 16f;
		
		quads.add(createQuad(new Vec3d(0, 0, 0), new Vec3d(0, 1, 0), new Vec3d(1, 1, 0), new Vec3d(1, 0, 0), down));
		quads.add(createQuad(new Vec3d(0, 0, 0), new Vec3d(0, 0, 1), new Vec3d(0, 1, 1), new Vec3d(0, 1, 0), down));
		quads.add(createQuad(new Vec3d(1, 0, 0), new Vec3d(1, 1, 0), new Vec3d(1, 1, 1), new Vec3d(1, 0, 1), down));
		quads.add(createQuad(new Vec3d(0, 0, 1), new Vec3d(1, 0, 1), new Vec3d(1, 1, 1), new Vec3d(0, 1, 1), down));
		
/*		quads.add(createQuad(new Vec3d(a, a, a), new Vec3d(a, b, a), new Vec3d(b, b, a), new Vec3d(b, a, a), sprite));
		quads.add(createQuad(new Vec3d(a, a, a), new Vec3d(a, a, b), new Vec3d(a, b, b), new Vec3d(a, b, a), sprite));
		quads.add(createQuad(new Vec3d(a, a, a), new Vec3d(b, a, a), new Vec3d(b, a, b), new Vec3d(a, a, b), sprite));
		quads.add(createQuad(new Vec3d(b, a, a), new Vec3d(b, b, a), new Vec3d(b, b, b), new Vec3d(b, a, b), sprite));
		quads.add(createQuad(new Vec3d(b, b, a), new Vec3d(a, b, a), new Vec3d(a, b, b), new Vec3d(b, b, b), sprite));
		quads.add(createQuad(new Vec3d(a, a, b), new Vec3d(b, a, b), new Vec3d(b, b, b), new Vec3d(a, b, b), sprite)); */
		
		cache.put(state, quads);
		return quads;
	}
	
    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(tex);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 16);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 16);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 16, 0);
        return builder.build();
    }
    
    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
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
                        u = sprite.getInterpolatedU(u);
                        v = sprite.getInterpolatedV(v);
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
		// TODO Auto-generated method stub
		return null;
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
