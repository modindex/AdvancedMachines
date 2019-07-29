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

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.objects.blocks.BlockMaterial;
import jaminv.advancedmachines.objects.blocks.machine.expansion.BlockMachineExpansionBase;
import jaminv.advancedmachines.objects.blocks.machine.expansion.expansion.BlockMachineExpansion;
import jaminv.advancedmachines.objects.blocks.machine.expansion.inventory.BlockMachineInventory;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.MultiblockBorders;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.ICanHaveMachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineFace;
import jaminv.advancedmachines.objects.blocks.machine.multiblock.face.MachineParent;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.helper.BlockHelper;
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
		
		private Border append(String append, boolean dir) {
			if (!dir) { return this; }
			if (border != "") { border += "_"; }
			border += append;
			return this;
		}
		
		public Border appendTop(boolean dir) { return append("top", dir); }
		public Border appendBottom(boolean dir) { return append("bottom", dir); }
		public Border appendLeft(boolean dir) { return append("left", dir); }
		public Border appendRight(boolean dir) { return append("right", dir); }
		
		public String getBorder() {
			if (border.equals("")) { return "none"; }
			if (border.equals("top_bottom_left_right")) { return "all"; }
			return border;
		}
		
		public String getMachineFaceTexture(MachineFace face) {
			boolean f = (face == MachineFace.F2x2);
			String root = (f ? "f2x2p" : "f3x3p");
			
			switch(border) {
			case "top_left": 
				return root + "00";
			case "top":
				return root + "10";
			case "top_right":
				return root + (f ? "10" : "20");
			case "left":
				return root + "01";
			case "":
				return root + "11";
			case "right":
				return root + (f ? "00" : "21");
			case "bottom_left":
				return root + (f ? "01" : "02");
			case "bottom":
				return root + (f ? "00" : "12");
			case "bottom_right":
				return root + (f ? "11" : "22");
			default:
				return root + "00";			
			}
		}
	}
	
	public static final ModelResourceLocation BASE = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock");
	public static final ModelResourceLocation FURNACE = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_furnace");
	public static final ModelResourceLocation ALLOY = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_alloy");
	public static final ModelResourceLocation PURIFIER = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_purifier");
	public static final ModelResourceLocation GRINDER = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_grinder");
	public static final ModelResourceLocation INVENTORY = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_inventory");
	public static final ModelResourceLocation REDSTONE = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_redstone");
	public static final ModelResourceLocation ENERGY = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_energy");
	public static final ModelResourceLocation SPEED = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_speed");
	public static final ModelResourceLocation PRODUCTIVITY = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_productivity");
	public static final ModelResourceLocation TANK = new ModelResourceLocation(Reference.MODID + ":bakedmodelmultiblock_tank");
	
	private VertexFormat format;
	private Map<String, TextureAtlasSprite> sprites = new HashMap<String, TextureAtlasSprite>();
	protected String basetexture, facetexture, machinetype;
	
	public BakedModelMultiblock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, String basetexture, String facetexture) {
		this.format = format;
		this.basetexture = basetexture;
		this.facetexture = facetexture;
		
		Main.logger.info("Loading sprites");
		for (Map.Entry<String, ResourceLocation> entry : MultiblockTextures.resources.entrySet()) {
			sprites.put(entry.getKey(), bakedTextureGetter.apply(entry.getValue()));
		}	
		Main.logger.info("Completed - Loading sprites");
	}
	
	private final Map<IBlockState, List<BakedQuad>> cache = new HashMap<IBlockState, List<BakedQuad>>();
	
	protected TextureAtlasSprite getSprite(String resourcelocation, String direction) {
		TextureAtlasSprite ret = sprites.get(resourcelocation);
		if (ret == null) { 
			ret = sprites.get("no_texture");
			Main.logger.error("Error loading sprite: '" + resourcelocation + "', direction: " + direction);
		}
		return ret;
	}
	
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> cached = cache.get(state);
		if (cached != null) { return cached; }
		
		List<BakedQuad> quads = new ArrayList<>();
		
		float a = 0f;
		float b = 1f;
		
		MultiblockBorders border = new MultiblockBorders(state);
		MaterialBase variant = state.getValue(BlockMaterial.EXPANSION_VARIANT);

		String baseresource = this.basetexture + "." + variant.getName() + ".";
		String tex;
		
		Border bnorth = new Border().appendTop(border.getTop()).appendBottom(border.getBottom()).appendLeft(border.getEast()).appendRight(border.getWest());
		if ((tex = checkFace(state, EnumFacing.NORTH, bnorth)) == null) { tex = baseresource + bnorth.getBorder(); }
		quads.add(createQuad(new Vec3d(b, b, a), new Vec3d(b, a, a), new Vec3d(a, a, a), new Vec3d(a, b, a), getSprite(tex, "north")));
		
		Border bsouth = new Border().appendTop(border.getTop()).appendBottom(border.getBottom()).appendLeft(border.getWest()).appendRight(border.getEast());
		if ((tex = checkFace(state, EnumFacing.SOUTH, bsouth)) == null) { tex = baseresource + bsouth.getBorder(); }
		quads.add(createQuad(new Vec3d(a, b, b), new Vec3d(a, a, b), new Vec3d(b, a, b), new Vec3d(b, b, b), getSprite(tex, "south")));

		Border bwest = new Border().appendTop(border.getTop()).appendBottom(border.getBottom()).appendLeft(border.getNorth()).appendRight(border.getSouth());
		if ((tex = checkFace(state, EnumFacing.WEST, bwest)) == null) { tex = baseresource + bwest.getBorder(); }
		quads.add(createQuad(new Vec3d(a, b, a), new Vec3d(a, a, a), new Vec3d(a, a, b), new Vec3d(a, b, b), getSprite(tex, "west")));

		Border bbottom = new Border().appendTop(border.getWest()).appendBottom(border.getEast()).appendLeft(border.getNorth()).appendRight(border.getSouth());
		if ((tex = checkFace(state, EnumFacing.DOWN, bbottom)) == null) { tex = baseresource + bbottom.getBorder(); }
		quads.add(createQuad(new Vec3d(a, a, a), new Vec3d(b, a, a), new Vec3d(b, a, b), new Vec3d(a, a, b), getSprite(tex, "down")));
		
		Border beast = new Border().appendTop(border.getTop()).appendBottom(border.getBottom()).appendLeft(border.getSouth()).appendRight(border.getNorth());
		if ((tex = checkFace(state, EnumFacing.EAST, beast)) == null) { tex = baseresource + beast.getBorder(); }
		quads.add(createQuad(new Vec3d(b, b, b), new Vec3d(b, a, b), new Vec3d(b, a, a), new Vec3d(b, b, a), getSprite(tex, "east")));
		
		Border btop = new Border().appendTop(border.getEast()).appendBottom(border.getWest()).appendLeft(border.getNorth()).appendRight(border.getSouth());
		if ((tex = checkFace(state, EnumFacing.UP, btop)) == null) { tex = baseresource + btop.getBorder(); }
		quads.add(createQuad(new Vec3d(b, b, a), new Vec3d(a, b, a), new Vec3d(a, b, b), new Vec3d(b, b, b), getSprite(tex, "top")));
		
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
    
    protected String getMachineType() { return "furnace"; }
    
    protected String checkFace(IBlockState state, EnumFacing side, Border border) {
    	if (facetexture == null || !BlockHelper.hasProperty(state, BlockMachineExpansion.FACING)) { return null; }
    	if (state.getValue(BlockMachineExpansion.FACING) != side) { return null; }

    	String tex = border.getBorder();
		String machine = facetexture;
		if (BlockHelper.hasProperty(state, ICanHaveMachineFace.MACHINE_FACE)) {
        	MachineFace face = state.getValue(ICanHaveMachineFace.MACHINE_FACE);
    		if (face != MachineFace.NONE) {
    			tex = border.getMachineFaceTexture(face);

    			if (BlockHelper.hasProperty(state, BlockMachineExpansion.MACHINE_PARENT)) {
    				MachineParent parent = state.getValue(BlockMachineExpansion.MACHINE_PARENT);
    				machine = parent.getName();
    			}
    		} else if(facetexture.equals("expansion")) { return null; }
    	}

		String variant = state.getValue(BlockMaterial.EXPANSION_VARIANT).getName();

		String ret = machine + "." + checkProperties(state) + variant + "." + tex;
		Main.logger.info("Rendering texture: " + ret);
		return ret;
    }
    
    protected String checkProperties(IBlockState state) {
    	String ret = "";
		if (BlockHelper.hasProperty(state, BlockMachineExpansion.ACTIVE)) {
			ret += (state.getValue(BlockMachineExpansion.ACTIVE) ? "active." : "inactive.");
		}		
		if (BlockHelper.hasProperty(state, BlockMachineInventory.INPUT)) {
			ret += (state.getValue(BlockMachineInventory.INPUT) ? "input." : "output.");
		}
		return ret;
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
