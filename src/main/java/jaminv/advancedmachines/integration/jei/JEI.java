package jaminv.advancedmachines.integration.jei;

import jaminv.advancedmachines.ModReference;
import jaminv.advancedmachines.init.init.BlockInit;
import jaminv.advancedmachines.lib.dialog.container.EmptyContainer;
import jaminv.advancedmachines.lib.jei.IJeiDialog;
import jaminv.advancedmachines.machine.instance.alloy.AlloyManager;
import jaminv.advancedmachines.machine.instance.alloy.DialogMachineAlloy;
import jaminv.advancedmachines.machine.instance.alloy.TileMachineAlloy.ContainerAlloy;
import jaminv.advancedmachines.machine.instance.furnace.DialogMachineFurnace;
import jaminv.advancedmachines.machine.instance.furnace.FurnaceManager;
import jaminv.advancedmachines.machine.instance.furnace.TileMachineFurnace.ContainerFurnace;
import jaminv.advancedmachines.machine.instance.grinder.DialogMachineGrinder;
import jaminv.advancedmachines.machine.instance.grinder.GrinderManager;
import jaminv.advancedmachines.machine.instance.grinder.TileMachineGrinder.ContainerGrinder;
import jaminv.advancedmachines.machine.instance.injector.DialogMachineInjector;
import jaminv.advancedmachines.machine.instance.injector.InjectorManager;
import jaminv.advancedmachines.machine.instance.injector.TileMachineInjector.ContainerInjector;
import jaminv.advancedmachines.machine.instance.melter.DialogMachineMelter;
import jaminv.advancedmachines.machine.instance.melter.MelterManager;
import jaminv.advancedmachines.machine.instance.melter.TileMachineMelter.ContainerMelter;
import jaminv.advancedmachines.machine.instance.press.DialogMachinePress;
import jaminv.advancedmachines.machine.instance.press.PressManager;
import jaminv.advancedmachines.machine.instance.press.TileMachinePress.ContainerPress;
import jaminv.advancedmachines.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.machine.instance.purifier.PurifierManager;
import jaminv.advancedmachines.machine.instance.purifier.TileMachinePurifier.ContainerPurifier;
import jaminv.advancedmachines.machine.instance.stabilizer.DialogMachineStabilizer;
import jaminv.advancedmachines.machine.instance.stabilizer.StabilizerManager;
import jaminv.advancedmachines.machine.instance.stabilizer.TileMachineStabilizer.ContainerStabilizer;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@JEIPlugin
@SideOnly(Side.CLIENT)
public class JEI implements IModPlugin {
	
	public static class RecipeUids {
		public static final String FURNACE = ModReference.MODID + ".furnace";
		public static final String GRINDER = ModReference.MODID + ".grinder";
		public static final String PURIFIER = ModReference.MODID + ".purifier";
		public static final String ALLOY = ModReference.MODID + ".alloy";
		public static final String PRESS = ModReference.MODID + ".press";
		public static final String MELTER = ModReference.MODID + ".melter";
		public static final String STABILIZER = ModReference.MODID + ".stabilizer";
		public static final String INJECTOR = ModReference.MODID + ".injector";
	}	
	
	IJeiDialog dialogFurnace = new DialogMachineFurnace(new EmptyContainer());
	IJeiDialog dialogPurifier = new DialogMachinePurifier(new EmptyContainer());
	IJeiDialog dialogGrinder = new DialogMachineGrinder(new EmptyContainer());
	IJeiDialog dialogAlloy = new DialogMachineAlloy(new EmptyContainer());
	IJeiDialog dialogPress = new DialogMachinePress(new EmptyContainer());
	IJeiDialog dialogMelter = new DialogMachineMelter(new EmptyContainer());
	IJeiDialog dialogStabilizer = new DialogMachineStabilizer(new EmptyContainer());
	IJeiDialog dialogInjector = new DialogMachineInjector(new EmptyContainer());
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(
			new RecipeCategory(guiHelper, dialogFurnace, RecipeUids.FURNACE, "dialog.furnace.title")
			, new RecipeCategory(guiHelper, dialogPurifier, RecipeUids.PURIFIER, "dialog.purifier.title")
			, new RecipeCategory(guiHelper, dialogGrinder, RecipeUids.GRINDER, "dialog.grinder.title")
			, new RecipeCategory(guiHelper, dialogAlloy, RecipeUids.ALLOY, "dialog.alloy.title")
			, new RecipeCategory(guiHelper, dialogPress, RecipeUids.PRESS, "dialog.press.title")
			, new RecipeCategory(guiHelper, dialogMelter, RecipeUids.MELTER, "dialog.melter.title")
			, new RecipeCategory(guiHelper, dialogStabilizer, RecipeUids.STABILIZER, "dialog.stabilizer.title")
			, new RecipeCategory(guiHelper, dialogInjector, RecipeUids.INJECTOR, "dialog.injector.title")
		);
	}

	@Override
	public void register(IModRegistry registry) {
		RecipeCategory.initialize(registry, RecipeUids.FURNACE, FurnaceManager.getRecipeList(), dialogFurnace, DialogMachineFurnace.class, ContainerFurnace.class, BlockInit.FURNACE);
		RecipeCategory.initialize(registry, RecipeUids.PURIFIER, PurifierManager.getRecipeList(), dialogPurifier, DialogMachinePurifier.class, ContainerPurifier.class, BlockInit.PURIFIER);
		RecipeCategory.initialize(registry, RecipeUids.GRINDER, GrinderManager.getRecipeList(), dialogGrinder, DialogMachineGrinder.class, ContainerGrinder.class, BlockInit.GRINDER);
		RecipeCategory.initialize(registry, RecipeUids.ALLOY, AlloyManager.getRecipeList(), dialogAlloy, DialogMachineAlloy.class, ContainerAlloy.class, BlockInit.ALLOY);
		RecipeCategory.initialize(registry, RecipeUids.PRESS, PressManager.getRecipeList(), dialogPress, DialogMachinePress.class, ContainerPress.class, BlockInit.PRESS);
		RecipeCategory.initialize(registry, RecipeUids.MELTER, MelterManager.getRecipeList(), dialogMelter, DialogMachineMelter.class, ContainerMelter.class, BlockInit.MELTER);
		RecipeCategory.initialize(registry, RecipeUids.STABILIZER, StabilizerManager.getRecipeList(), dialogStabilizer, DialogMachineStabilizer.class, ContainerStabilizer.class, BlockInit.STABILIZER);
		RecipeCategory.initialize(registry, RecipeUids.INJECTOR, InjectorManager.getRecipeList(), dialogInjector, DialogMachineInjector.class, ContainerInjector.class, BlockInit.INJECTOR);
	}	
}
