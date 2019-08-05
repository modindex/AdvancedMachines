package jaminv.advancedmachines.init;

import jaminv.advancedmachines.util.recipe.machine.AlloyManager;
import jaminv.advancedmachines.util.recipe.machine.FurnaceManager;
import jaminv.advancedmachines.util.recipe.machine.grinder.GrinderManager;
import jaminv.advancedmachines.util.recipe.machine.melter.MelterManager;
import jaminv.advancedmachines.util.recipe.machine.purifier.PurifierManager;
import scala.tools.nsc.ast.parser.Parsers.Parser;

public class RecipeInit {
	public static void init() {
		FurnaceManager.init();
		PurifierManager.init();
		GrinderManager.init();
		AlloyManager.init();
		MelterManager.init();
	}
}
