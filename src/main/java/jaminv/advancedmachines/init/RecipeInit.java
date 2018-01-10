package jaminv.advancedmachines.init;

import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import jaminv.advancedmachines.util.recipe.machine.PurifierManager;

public class RecipeInit {
	public static void init() {
		PurifierManager.init();
		AlloyManager.init();
	}
}
