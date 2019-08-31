package jaminv.advancedmachines.objects.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

/** For some reason, the constructors for ItemAxe and ItemPickaxe are protected. */
public class ToolAxe extends ItemAxe {

	public ToolAxe(ToolMaterial material, float damage, float speed) {
		super(material, damage, speed);
		setCreativeTab(CreativeTabs.TOOLS);
	}
}
