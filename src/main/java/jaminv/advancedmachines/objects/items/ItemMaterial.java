package jaminv.advancedmachines.objects.items;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.util.interfaces.IEnumType;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMaterial extends Item implements IHasModel, IMetaName, IHasOreDictionary {

	protected String name, oredictprefix, oredictsuffix;
	protected IEnumType[] types;

	public ItemMaterial(String name, IEnumType[] types) {
		setHasSubtypes(true);
		setMaxDamage(0);

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		this.name = name;
		this.oredictprefix = name;
		this.oredictsuffix = "";
		this.types = types;
		
		ItemInit.ITEMS.add(this);
	}
	
	public ItemMaterial(String name, String oredictprefix, String oredictsuffix, IEnumType[] types) {
		this(name, types);
		this.oredictprefix = oredictprefix;
		this.oredictsuffix = oredictsuffix;
	}

	@Override
	public void registerModels() {
		for (IEnumType type : types) {
			if (type.doInclude()) {
				Main.proxy.registerVariantRenderer(this, type.getMeta(), this.name + "_" + type.getName(), "inventory");
			}
		}
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (IEnumType type : types) {
			if (type.doInclude()) {
				items.add(new ItemStack(this, 1, type.getMeta()));
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
		for (IEnumType type : types) {
			if (type.doInclude()) {
				ItemStack item = new ItemStack(this, 1, type.getMeta());
				OreDictionary.registerOre(this.oredictprefix + WordUtils.capitalize(this.getSpecialName(item)) + WordUtils.capitalize(this.oredictsuffix), item);
			}
		}
	}

	@Override
	public String getSpecialName(ItemStack stack) {
		return types[stack.getItemDamage()].getName();
	}
}
