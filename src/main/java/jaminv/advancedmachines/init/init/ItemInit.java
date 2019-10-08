package jaminv.advancedmachines.init.init;

import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.lib.util.Variant;
import jaminv.advancedmachines.lib.util.registry.RegistryHelper;
import jaminv.advancedmachines.objects.ItemGlint;
import jaminv.advancedmachines.objects.tools.ToolAxe;
import jaminv.advancedmachines.objects.tools.ToolPickaxe;
import jaminv.advancedmachines.objects.variant.VariantAlloy;
import jaminv.advancedmachines.objects.variant.VariantCircuit;
import jaminv.advancedmachines.objects.variant.VariantDust;
import jaminv.advancedmachines.objects.variant.VariantGear;
import jaminv.advancedmachines.objects.variant.VariantIngredient;
import jaminv.advancedmachines.objects.variant.VariantPure;
import jaminv.advancedmachines.objects.variant.VariantResource;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemInit {
	
	public static final ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("tool_titanium", 2, 1561, 4.0f, 1.0f, 10);
	public static final ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("armor_titanium", ModReference.MODID + ":titanium", 33, new int[]{3,6,8,3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0f);

	protected static String getVariantName(String name, Variant variant) { return name + "_" + variant.getName(); }	
	
	public static void init() {
		for (VariantResource var : VariantResource.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("ingot", var));
		}
		for (VariantPure var : VariantPure.values()) {
			if (!var.hasIngot()) { continue; }
			RegistryHelper.addItem(new ItemGlint(var).setCreativeTab(CreativeTabs.MATERIALS), getVariantName("ingot_pure", var));
		}
		for (VariantAlloy var : VariantAlloy.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("alloy", var));
		}

		for (VariantDust var : VariantDust.values()) {
			RegistryHelper.addItem(new ItemGlint(var).setCreativeTab(CreativeTabs.MATERIALS), getVariantName("dust", var));
		}
		for (VariantPure var : VariantPure.values()) {
			RegistryHelper.addItem(new ItemGlint(var).setCreativeTab(CreativeTabs.MATERIALS), getVariantName("dust_pure", var));
		}
		for (VariantAlloy var : VariantAlloy.values()) {
			//RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("alloy_dust", var));
		}
		
		for (VariantGear var : VariantGear.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), getVariantName("gear", var));
		}
		for (VariantCircuit var : VariantCircuit.values()) {
			RegistryHelper.addItem(new ItemGlint(var).setCreativeTab(CreativeTabs.MATERIALS), getVariantName("circuit", var));
		}
		for (VariantIngredient var : VariantIngredient.values()) {
			RegistryHelper.addItem(new Item().setCreativeTab(CreativeTabs.MATERIALS), var.getName());
		}
		
		RegistryHelper.addItem(new ToolAxe(TOOL_TITANIUM, 8.0f, -3.2f).setCreativeTab(CreativeTabs.TOOLS), "axe_titanium");
		RegistryHelper.addItem(new ItemHoe(TOOL_TITANIUM).setCreativeTab(CreativeTabs.TOOLS), "hoe_titanium");
		RegistryHelper.addItem(new ToolPickaxe(TOOL_TITANIUM).setCreativeTab(CreativeTabs.TOOLS), "pickaxe_titanium");
		RegistryHelper.addItem(new ItemSpade(TOOL_TITANIUM).setCreativeTab(CreativeTabs.TOOLS), "shovel_titanium");
		RegistryHelper.addItem(new ItemSword(TOOL_TITANIUM).setCreativeTab(CreativeTabs.COMBAT), "sword_titanium");
		
		RegistryHelper.addItem(new ItemArmor(ARMOR_TITANIUM, 1, EntityEquipmentSlot.HEAD).setCreativeTab(CreativeTabs.COMBAT), "helmet_titanium");
		RegistryHelper.addItem(new ItemArmor(ARMOR_TITANIUM, 1, EntityEquipmentSlot.CHEST).setCreativeTab(CreativeTabs.COMBAT), "chestplate_titanium");
		RegistryHelper.addItem(new ItemArmor(ARMOR_TITANIUM, 1, EntityEquipmentSlot.LEGS).setCreativeTab(CreativeTabs.COMBAT), "leggings_titanium");
		RegistryHelper.addItem(new ItemArmor(ARMOR_TITANIUM, 1, EntityEquipmentSlot.FEET).setCreativeTab(CreativeTabs.COMBAT), "boots_titanium");				
	}	
}
