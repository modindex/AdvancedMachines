package jaminv.advancedmachines.init.init;

import jaminv.advancedmachines.machine.instance.alloy.AlloyManager;
import jaminv.advancedmachines.machine.instance.furnace.FurnaceManager;
import jaminv.advancedmachines.machine.instance.grinder.GrinderManager;
import jaminv.advancedmachines.machine.instance.injector.InjectorManager;
import jaminv.advancedmachines.machine.instance.melter.MelterManager;
import jaminv.advancedmachines.machine.instance.press.PressManager;
import jaminv.advancedmachines.machine.instance.purifier.PurifierManager;
import jaminv.advancedmachines.machine.instance.stabilizer.StabilizerManager;

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
