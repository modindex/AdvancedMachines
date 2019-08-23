package jaminv.advancedmachines.integration.jei;

import jaminv.advancedmachines.init.BlockInit;
import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.dialog.container.EmptyContainer;
import jaminv.advancedmachines.lib.jei.IJeiDialog;
import jaminv.advancedmachines.machine.instance.alloy.DialogMachineAlloy;
import jaminv.advancedmachines.machine.instance.furnace.DialogMachineFurnace;
import jaminv.advancedmachines.machine.instance.grinder.DialogMachineGrinder;
import jaminv.advancedmachines.machine.instance.injector.DialogMachineInjector;
import jaminv.advancedmachines.machine.instance.melter.DialogMachineMelter;
import jaminv.advancedmachines.machine.instance.purifier.DialogMachinePurifier;
import jaminv.advancedmachines.machine.instance.stabilizer.DialogMachineStabilizer;
import jaminv.advancedmachines.util.Reference;
import jaminv.advancedmachines.util.recipe.AlloyManager;
import jaminv.advancedmachines.util.recipe.FurnaceManager;
import jaminv.advancedmachines.util.recipe.grinder.GrinderManager;
import jaminv.advancedmachines.util.recipe.injector.InjectorManager;
import jaminv.advancedmachines.util.recipe.melter.MelterManager;
import jaminv.advancedmachines.util.recipe.purifier.PurifierManager;
import jaminv.advancedmachines.util.recipe.stabilizer.StabilizerManager;
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
		public static final String FURNACE = Reference.MODID + ".furnace";
		public static final String GRINDER = Reference.MODID + ".grinder";
		public static final String PURIFIER = Reference.MODID + ".purifier";
		public static final String ALLOY = Reference.MODID + ".alloy";
		public static final String MELTER = Reference.MODID + ".melter";
		public static final String STABILIZER = Reference.MODID + ".stabilizer";
		public static final String INJECTOR = Reference.MODID + ".injector";
	}	
	
	IJeiDialog dialogFurnace = new DialogMachineFurnace(new EmptyContainer());
	IJeiDialog dialogPurifier = new DialogMachinePurifier(new EmptyContainer());
	IJeiDialog dialogGrinder = new DialogMachineGrinder(new EmptyContainer());
	IJeiDialog dialogAlloy = new DialogMachineAlloy(new EmptyContainer());
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
			, new RecipeCategory(guiHelper, dialogMelter, RecipeUids.MELTER, "dialog.melter.title")
			, new RecipeCategory(guiHelper, dialogStabilizer, RecipeUids.STABILIZER, "dialog.stabilizer.title")
			, new RecipeCategory(guiHelper, dialogInjector, RecipeUids.INJECTOR, "dialog.injector.title")
		);
	}

	@Override
	public void register(IModRegistry registry) {
		RecipeCategory.initialize(registry, RecipeUids.FURNACE, FurnaceManager.getRecipeList(), dialogFurnace, DialogMachineFurnace.class, ContainerMachine.class, BlockInit.MACHINE_FURNACE);
		RecipeCategory.initialize(registry, RecipeUids.PURIFIER, PurifierManager.getRecipeList(), dialogPurifier, DialogMachinePurifier.class, ContainerMachine.class, BlockInit.MACHINE_PURIFIER);
		RecipeCategory.initialize(registry, RecipeUids.GRINDER, GrinderManager.getRecipeList(), dialogGrinder, DialogMachineGrinder.class, ContainerMachine.class, BlockInit.MACHINE_GRINDER);
		RecipeCategory.initialize(registry, RecipeUids.ALLOY, AlloyManager.getRecipeList(), dialogAlloy, DialogMachineAlloy.class, ContainerMachine.class, BlockInit.MACHINE_ALLOY);
		RecipeCategory.initialize(registry, RecipeUids.MELTER, MelterManager.getRecipeList(), dialogMelter, DialogMachineMelter.class, ContainerMachine.class, BlockInit.MACHINE_MELTER);
		RecipeCategory.initialize(registry, RecipeUids.STABILIZER, StabilizerManager.getRecipeList(), dialogStabilizer, DialogMachineStabilizer.class, ContainerMachine.class, BlockInit.MACHINE_STABILIZER);
		RecipeCategory.initialize(registry, RecipeUids.INJECTOR, InjectorManager.getRecipeList(), dialogInjector, DialogMachineInjector.class, ContainerMachine.class, BlockInit.MACHINE_INJECTOR);
	}	
}
