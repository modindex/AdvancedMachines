package jaminv.advancedmachines.objects.items;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.objects.variant.MaterialBase;
import jaminv.advancedmachines.util.interfaces.IEnumType;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMaterial extends Item implements IHasModel, IMetaName {

	protected String name;
	protected MaterialBase[] types;

	public ItemMaterial(String name, MaterialBase[] types) {
		setHasSubtypes(true);
		setMaxDamage(0);

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		this.name = name;
		this.types = types;
		
		ItemInit.ITEMS.add(this);
	}
	
	protected boolean doInclude(MaterialBase type) {
		return type.doInclude("");
	}
	
	@Override
	public void registerModels() {
		for (MaterialBase type : types) {
			if (doInclude(type)) {
				Main.proxy.registerVariantRenderer(this, type.getMeta(), this.name + "_" + type.getName(), "inventory");
			}
		}
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (MaterialBase type : types) {
			if (doInclude(type)) {
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
	public String getSpecialName(ItemStack stack) {
		return types[stack.getItemDamage()].getName();
	}
}
