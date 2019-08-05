package jaminv.advancedmachines.objects.items;

import org.apache.commons.lang3.text.WordUtils;

import jaminv.advancedmachines.Main;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.objects.material.MaterialBase;
import jaminv.advancedmachines.util.interfaces.IEnumType;
import jaminv.advancedmachines.util.interfaces.IHasModel;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMaterialOre extends ItemMaterial implements IHasOreDictionary {

	protected String oredictprefix, oredictsuffix;

	public ItemMaterialOre(String name, MaterialBase[] types) {
		super(name, types);
		this.oredictprefix = name;
		this.oredictsuffix = "";
	}
	
	public ItemMaterialOre(String name, String oredictprefix, MaterialBase[] types) {
		this(name, types);
		this.oredictprefix = oredictprefix;
	}
	
	public ItemMaterialOre(String name, String oredictprefix, String oredictsuffix, MaterialBase[] types) {
		this(name, types);
		this.oredictprefix = oredictprefix;
		this.oredictsuffix = oredictsuffix;
	}
	
	

	@Override
	protected boolean doInclude(MaterialBase type) {
		return type.doInclude(oredictprefix);
	}

	@Override
	public void registerOreDictionary() {
		for (MaterialBase type : types) {
			if (type.doInclude(this.oredictprefix)) {
				ItemStack item = new ItemStack(this, 1, type.getMeta());
				OreDictionary.registerOre(this.oredictprefix + (WordUtils.capitalize(this.getSpecialName(item), '_')).replace("_", "") + WordUtils.capitalize(this.oredictsuffix), item);
			}
		}
	}

}
