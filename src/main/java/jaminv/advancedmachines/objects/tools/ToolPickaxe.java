package jaminv.advancedmachines.objects.tools;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;

/** For some reason, the constructors for ItemAxe and ItemPickaxe are protected. */
public class ToolPickaxe extends ItemPickaxe {

	public ToolPickaxe(ToolMaterial material) {
		super(material);
		setCreativeTab(CreativeTabs.TOOLS);
	}
}
