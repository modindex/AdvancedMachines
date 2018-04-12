package jaminv.advancedmachines.init;

import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import jaminv.advancedmachines.util.recipe.machine.FurnaceManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;

public class RecipeInit {
	public static void init() {
		FurnaceManager.init();
		PurifierManager.init();
		AlloyManager.init();
	}
}
