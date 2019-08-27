package jaminv.advancedmachines.proxy.handlers;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.init.ItemInit;
import jaminv.advancedmachines.util.interfaces.IHasOreDictionary;
import jaminv.advancedmachines.util.parser.DataParser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class OreDictionaryHandler {
	
	public static void registerOreDictionary() {
		/*for (Item item : ItemInit.ITEMS) {
			if (item instanceof IHasOreDictionary) {
				((IHasOreDictionary)item).registerOreDictionary();
			}
		}
		
		for (Block block : BlockInit.BLOCKS) {
			if (block instanceof IHasOreDictionary) {
				((IHasOreDictionary)block).registerOreDictionary();
			}
		}*/
		
		DataParser.parseFolder("data/ore_dictionary", new FileHandlerOreDictionary()); 
	}

}
