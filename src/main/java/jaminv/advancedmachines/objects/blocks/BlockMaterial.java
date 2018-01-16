package jaminv.advancedmachines.objects.blocks;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.objects.blocks.item.ItemBlockVariants;
import jaminv.advancedmachines.objects.items.material.MaterialBase;
import jaminv.advancedmachines.objects.items.material.PropertyMaterial;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class BlockMaterial extends Block implements IHasModel, IMetaName, IHasOreDictionary {
	
	public PropertyMaterial VARIANT;
	protected MaterialBase.MaterialType type;
	
	protected abstract PropertyMaterial getVariant();
	
	private String name;
	private String oredictprefix;
	
	public BlockMaterial(String name, MaterialBase.MaterialType type, Material material, float hardness) {
		super(material);
		this.type = type;

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, MaterialBase.MaterialRegistry.lookupMeta(type, 0)));
		setHardness(hardness);
		
		this.name = name;
		this.oredictprefix = name;
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
	}
	
	public BlockMaterial(String name, MaterialBase.MaterialType type, String oredictprefix, Material material, float hardness) {
		this(name, type, material, hardness);
		this.oredictprefix = oredictprefix;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return ((MaterialBase)state.getValue(VARIANT)).getMeta();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((MaterialBase)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, MaterialBase.MaterialRegistry.lookupMeta(type, meta));
	}
	 
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (MaterialBase variant : MaterialBase.values(type)) {
			if (variant.doInclude(oredictprefix)) {
				items.add(new ItemStack(this, 1, variant.getMeta()));
			}
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		VARIANT = this.getVariant();
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public String getSpecialName(ItemStack stack) {
		return MaterialBase.byMetadata(type, stack.getItemDamage()).getName();
	}
	
	@Override
	public void registerModels() {
		for (MaterialBase variant : MaterialBase.values(type)) {
			String name = variant.getName();
			if (variant.doInclude(oredictprefix)) {
				Main.proxy.registerVariantRenderer(Item.getItemFromBlock(this), variant.getMeta(), this.name + "_" + name, "inventory");
			}
		}
	}

	@Override
	public void registerOreDictionary() {
		if (oredictprefix == null) { return; }
		for (MaterialBase variant : MaterialBase.values(type)) {
			if (variant.doInclude(oredictprefix)) {
				ItemStack item = new ItemStack(this, 1, variant.getMeta());
				OreDictionary.registerOre(this.oredictprefix + WordUtils.capitalize(this.getSpecialName(item)), item);
			}
		}
	}	
}
