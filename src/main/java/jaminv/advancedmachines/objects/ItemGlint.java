package jaminv.advancedmachines.objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGlint extends Item {

	public static interface CanGlint {
		public boolean hasGlint();
	}
	
	CanGlint variant;
	
	public ItemGlint(CanGlint variant) {
		this.variant = variant;
	}
	
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return variant.hasGlint();
    }
}
