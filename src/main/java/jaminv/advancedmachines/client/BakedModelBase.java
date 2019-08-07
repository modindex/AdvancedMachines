package jaminv.advancedmachines.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Lists;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.client.quads.IModelQuad;
import jaminv.advancedmachines.init.property.Properties;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.IMachineFaceTE;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineType;
import jaminv.advancedmachines.objects.material.MaterialBase;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.helper.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;

public abstract class BakedModelBase implements IBakedModel {
	
	private VertexFormat format;
	
	public BakedModelBase(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		this.format = format;
	}
	
	private static final Map<String, List<BakedQuad>> cache = new HashMap<String, List<BakedQuad>>();
	
	protected TextureAtlasSprite getTexture(String resourcelocation) {
		return RawTextures.get(resourcelocation);
	}
	
	public ToStringHelper addCacheKeyProperties(IBlockState state, ToStringHelper helper) { return helper; }
	
	public String buildCacheKey(IBlockState state) {
		ToStringHelper helper = MoreObjects.toStringHelper(this);

		helper.add("meta", state.getBlock().getMetaFromState(state)); 
		for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
			helper.add(entry.getKey().getName(), entry.getValue().toString());
		}
		
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState ext = (IExtendedBlockState)state;
			for (Entry<IUnlistedProperty<?>, Optional<?>> entry: ext.getUnlistedProperties().entrySet()) {
				if (!entry.getValue().isPresent()) {
					helper.add(entry.getKey().getName(), "<NULL>");
				} else {
					IUnlistedProperty prop = entry.getKey();
					helper.add(prop.getName(), prop.valueToString(ext.getValue(prop)));
				}
			}
		}
		
		helper = addCacheKeyProperties(state, helper);

		return helper.toString();
	}	
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		String cachekey = buildCacheKey(state);
		List<BakedQuad> cached = cache.get(cachekey);
		if (cached != null) { return cached; }
		
		List<BakedQuad> quads = new ArrayList<>();
		
		float a = 0f;
		float b = 1f;
		
		List<IModelQuad> model = render(format, state, side, rand);
		for (IModelQuad quad : model) {
			quads.addAll(quad.getQuads());
		}
		
		cache.put(cachekey, quads);
		return quads;		
	}
	
	public abstract List<IModelQuad> render(VertexFormat format, IBlockState state, EnumFacing side, long rand);
	
    public static BakedQuad createQuad(VertexFormat format, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex, int umin, int umax, int vmin, int vmax, boolean inverted) {
        Vec3d normal = v3.subtract(v2).crossProduct(v1.subtract(v2)).normalize();

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(tex);
        if (!inverted) {
	        putVertex(format, builder, normal, tex, v1.x, v1.y, v1.z, umin, vmin);
	        putVertex(format, builder, normal, tex, v2.x, v2.y, v2.z, umin, vmax);
	        putVertex(format, builder, normal, tex, v3.x, v3.y, v3.z, umax, vmax);
	        putVertex(format, builder, normal, tex, v4.x, v4.y, v4.z, umax, umin);
        } else {
	        putVertex(format, builder, normal, tex, v1.x, v1.y, v1.z, umin, vmin);
	        putVertex(format, builder, normal, tex, v4.x, v4.y, v4.z, umax, vmin);
	        putVertex(format, builder, normal, tex, v3.x, v3.y, v3.z, umax, vmax);
	        putVertex(format, builder, normal, tex, v2.x, v2.y, v2.z, umin, vmax);
        }
        return builder.build();
    }
    
    public static BakedQuad createQuad(VertexFormat format, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex, boolean inverted) {
    	return createQuad(format, v1, v2, v3, v4, tex, 0, 16, 0, 16, inverted);
    }

    public static BakedQuad createQuad(VertexFormat format, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite tex) {
    	return createQuad(format, v1, v2, v3, v4, tex, 0, 16, 0, 16, false);
    }
    
    public static void putVertex(VertexFormat format, UnpackedBakedQuad.Builder builder, Vec3d normal, TextureAtlasSprite tex, double x, double y, double z, float u, float v) {
 	
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
	public ItemOverrideList getOverrides() {
		return itemHandler;
	}

	private final ItemOverrideList itemHandler = new ItemOverrideList(Lists.<ItemOverride> newArrayList()) {
		@Override
		public IBakedModel handleItemState(IBakedModel model, ItemStack stack, World world, EntityLivingBase entity) {
			return BakedModelBase.this;
		}
	};

}
