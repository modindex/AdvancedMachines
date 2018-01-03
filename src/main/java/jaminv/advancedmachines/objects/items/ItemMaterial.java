package jaminv.advancedmachines.objects.items;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.util.Config;
import jaminv.advancedmachines.util.handlers.EnumHandler;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMaterial extends Item implements IHasModel, IMetaName, IHasOreDictionary {

	protected String name, oredictprefix;

	public ItemMaterial(String name) {
		setHasSubtypes(true);
		setMaxDamage(0);

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		this.name = name;
		this.oredictprefix = name;
		
		ItemInit.ITEMS.add(this);
	}
	
	public ItemMaterial(String name, String oredictprefix) {
		this(name);
		this.oredictprefix = oredictprefix;
	}

	@Override
	public void registerModels() {
		for (int i = 0; i < EnumHandler.EnumMaterial.values().length; i++) {
			String name = EnumHandler.EnumMaterial.values()[i].getName();
			if (Config.doInclude(name)) {
				Main.proxy.registerVariantRenderer(this, i, this.name + "_" + name, "inventory");
			}
		}
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (EnumHandler.EnumMaterial variant : EnumHandler.EnumMaterial.values()) {
			if (Config.doInclude(variant.getName())) {
				items.add(new ItemStack(this, 1, variant.getMeta()));
			}
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + ((IMetaName)this).getSpecialName(stack);
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

	@Override
	public String getSpecialName(ItemStack stack) {
		return EnumHandler.EnumMaterial.values()[stack.getItemDamage()].getName();
	}
}
