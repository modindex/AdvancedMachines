package jaminv.advancedmachines.objects.blocks;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.objects.blocks.item.ItemBlockVariants;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.EnumHandler;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BlockMaterial extends Block implements IHasModel, IMetaName, IHasOreDictionary {
	
	public static final PropertyEnum<EnumHandler.EnumMaterial> VARIANT = PropertyEnum.<EnumHandler.EnumMaterial>create("variant", EnumHandler.EnumMaterial.class);
	
	private String name;
	private String oredictprefix;
	
	public BlockMaterial(String name, Material material, float hardness) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumHandler.EnumMaterial.TITANIUM));
		setHardness(hardness);
		
		this.name = name;
		this.oredictprefix = name;
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
	}
	
	public BlockMaterial(String name, String oredictprefix, Material material, float hardness) {
		this(name, material, hardness);
		this.oredictprefix = oredictprefix;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return ((EnumHandler.EnumMaterial)state.getValue(VARIANT)).getMeta();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumHandler.EnumMaterial)state.getValue(VARIANT)).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, EnumHandler.EnumMaterial.byMetadata(meta));
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (EnumHandler.EnumMaterial variant : EnumHandler.EnumMaterial.values()) {
			if (Config.doInclude(variant.getName())) {
				items.add(new ItemStack(this, 1, variant.getMeta()));
			}
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public String getSpecialName(ItemStack stack) {
		return EnumHandler.EnumMaterial.values()[stack.getItemDamage()].getName();
	}
	
	@Override
	public void registerModels() {
		for (int i = 0; i < EnumHandler.EnumMaterial.values().length; i++) {
			String name = EnumHandler.EnumMaterial.values()[i].getName();
			if (Config.doInclude(name)) {
				Main.proxy.registerVariantRenderer(Item.getItemFromBlock(this), i, this.name + "_" + name, "inventory");
			}
		}
	}

	@Override
	public void registerOreDictionary() {
		for (EnumHandler.EnumMaterial variant : EnumHandler.EnumMaterial.values()) {
			if (Config.doInclude(variant.getName())) {
				ItemStack item = new ItemStack(this, 1, variant.getMeta());
				OreDictionary.registerOre(this.oredictprefix + WordUtils.capitalize(this.getSpecialName(item)), item);
			}
		}
	}	
}
