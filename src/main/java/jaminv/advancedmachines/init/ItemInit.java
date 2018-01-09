package jaminv.advancedmachines.init;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.armor.ArmorBase;
import jaminv.advancedmachines.objects.items.ItemMaterial;
import jaminv.advancedmachines.objects.items.material.MaterialDust;
import jaminv.advancedmachines.objects.items.material.MaterialMod;
import jaminv.advancedmachines.objects.items.material.MaterialPure;
import jaminv.advancedmachines.objects.tools.ToolAxe;
import jaminv.advancedmachines.objects.tools.ToolHoe;
import jaminv.advancedmachines.objects.tools.ToolPickaxe;
import jaminv.advancedmachines.objects.tools.ToolShovel;
import jaminv.advancedmachines.objects.tools.ToolSword;
import jaminv.advancedmachines.util.Reference;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ItemInit {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	// Material
	public static final ToolMaterial TOOL_TITANIUM = EnumHelper.addToolMaterial("tool_titanium", 2, 1561, 4.0F, 1.0F, 10);
	public static final ArmorMaterial ARMOR_TITANIUM = EnumHelper.addArmorMaterial("armor_titanium", Reference.MODID + ":titanium", 33, new int[] {3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);
	
	// Items
	public static final Item INGOT = new ItemMaterial("ingot", MaterialMod.values());
	public static final Item DUST = new ItemMaterial("dust", MaterialDust.values());
	public static final Item INGOT_PURE = new ItemMaterial("ingot_pure", "ingot", "Pure", MaterialPure.values());
	public static final Item DUST_PURE = new ItemMaterial("dust_pure", "dust", "Pure", MaterialPure.values());
	
	// Tools
	public static final Item AXE_TITANIUM = new ToolAxe("axe_titanium", TOOL_TITANIUM, 8.0F, -3.2F);	
	public static final Item HOE_TITANIUM = new ToolHoe("hoe_titanium", TOOL_TITANIUM);	
	public static final Item PICKAXE_TITANIUM = new ToolPickaxe("pickaxe_titanium", TOOL_TITANIUM);	
	public static final Item SHOVEL_TITANIUM = new ToolShovel("shovel_titanium", TOOL_TITANIUM);	
	public static final Item SWORD_TITANIUM = new ToolSword("sword_titanium", TOOL_TITANIUM);
	
	// Armor
	public static final Item HELMET_TITANIUM = new ArmorBase("helmet_titanium", ARMOR_TITANIUM, 1, EntityEquipmentSlot.HEAD);	
	public static final Item CHESTPLATE_TITANIUM = new ArmorBase("chestplate_titanium", ARMOR_TITANIUM, 1, EntityEquipmentSlot.CHEST);	
	public static final Item LEGGINGS_TITANIUM = new ArmorBase("leggings_titanium", ARMOR_TITANIUM, 2, EntityEquipmentSlot.LEGS);	
	public static final Item BOOTS_TITANIUM = new ArmorBase("boots_titanium", ARMOR_TITANIUM, 1, EntityEquipmentSlot.FEET);
	
}
