package jaminv.advancedmachines.init.init;

import jaminv.advancedmachines.util.recipe.AlloyManager;
import jaminv.advancedmachines.util.recipe.FurnaceManager;
import jaminv.advancedmachines.util.recipe.grinder.GrinderManager;
import jaminv.advancedmachines.util.recipe.injector.InjectorManager;
import jaminv.advancedmachines.util.recipe.melter.MelterManager;
import jaminv.advancedmachines.util.recipe.press.PressManager;
import jaminv.advancedmachines.util.recipe.purifier.PurifierManager;
import jaminv.advancedmachines.util.recipe.stabilizer.StabilizerManager;

public class RecipeInit {

	public static void init() {
		FurnaceManager.init();
		PurifierManager.init();
		GrinderManager.init();
		AlloyManager.init();
		PressManager.init();
		MelterManager.init();
		StabilizerManager.init();
		InjectorManager.init();
	}
}
