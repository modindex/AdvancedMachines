package jaminv.advancedmachines.objects.blocks.item;

import jaminv.advancedmachines.util.interfaces.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

// TODO: Ick. Just Ick. Get rid of this or encapsulate it better.
// TODO: There is nothing forcing Block to implement IMetaName (ick again), but it's cast to it without checking.
public class ItemBlockVariants extends ItemBlock {

	public ItemBlockVariants(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + ((IMetaName)this.block).getSpecialName(stack);
	}
}
