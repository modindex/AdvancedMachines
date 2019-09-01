package jaminv.advancedmachines.integration.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import jaminv.advancedmachines.lib.container.layout.IJeiLayoutManager;
import jaminv.advancedmachines.lib.jei.IJeiDialog;
import jaminv.advancedmachines.lib.jei.JeiRecipeCategory;
import jaminv.advancedmachines.lib.jei.JeiRecipeWrapper;
import jaminv.advancedmachines.lib.recipe.RecipeJei;
import jaminv.advancedmachines.lib.util.coord.Rect;
import jaminv.advancedmachines.objects.variant.VariantExpansion;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class RecipeCategory extends JeiRecipeCategory {
	
	public String recipeUid;

	public RecipeCategory(IGuiHelper guiHelper, IJeiDialog dialog, String recipeUid, String dialogTitle) {
		super(dialog);
		this.recipeUid = recipeUid;
		Rect texture = dialog.getJeiTexture();
		this.background = guiHelper.createDrawable(dialog.getJeiBackground(),
			texture.getX(), texture.getY(), texture.getW(), texture.getH());
		this.localizedName = I18n.format(dialogTitle); 
	}

	@Nonnull
	@Override
	public String getUid() {
		return recipeUid;
	}
	
	public static void initialize(IModRegistry registry, String recipeUid, List<? extends RecipeJei> recipeList, IJeiDialog dialog, Class<? extends GuiContainer> dialogClass, Class containerClass, Map<VariantExpansion,? extends Block> blocks) {
		RecipeCategory.addRecipes(registry, recipeUid, dialog, recipeList);
		RecipeCategory.createRecipeCatalyst(registry, recipeUid, blocks);
		RecipeCategory.createRecipeClickArea(registry, recipeUid, dialog, dialogClass);
		RecipeCategory.createRecipeTransferHandler(registry, recipeUid, containerClass, dialog.getLayout());		
	}
	
	public static void createRecipeCatalyst(IModRegistry registry, String recipeUid, Map<VariantExpansion,? extends Block> blocks) {
		registry.addRecipeCatalyst(new ItemStack(blocks.get(VariantExpansion.BASIC), 1), recipeUid);
		registry.addRecipeCatalyst(new ItemStack(blocks.get(VariantExpansion.COMPRESSED), 1), recipeUid);
		registry.addRecipeCatalyst(new ItemStack(blocks.get(VariantExpansion.QUAD), 1), recipeUid);
	}
	
	public static void createRecipeClickArea(IModRegistry registry, String recipeUid, IJeiDialog dialog, Class<? extends GuiContainer> dialogClass) {
		Rect jeiTarget = dialog.getJeiTarget();
		registry.addRecipeClickArea(dialogClass,
			jeiTarget.getX(), jeiTarget.getY(),
			jeiTarget.getW(), jeiTarget.getH(),
			recipeUid
		);		
	}
	
	public static void addRecipes(IModRegistry registry, String recipeUid, IJeiDialog dialog, List<? extends RecipeJei> recipeList) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipes(getRecipes(guiHelper, recipeList, dialog), recipeUid);
	}
	
	public static List<JeiRecipeWrapper> getRecipes(IGuiHelper guiHelper, List<? extends RecipeJei> recipeList, IJeiDialog dialog) {
		List<JeiRecipeWrapper> recipes = new ArrayList<>();
		
		for (RecipeJei recipe : recipeList) {
			recipes.add(new JeiRecipeWrapper(guiHelper, recipe, dialog));
		}
		
		return recipes;
	}

	public static void createRecipeTransferHandler(IModRegistry registry, String recipeUid, Class containerClass, IJeiLayoutManager layout) {
		if (layout.getItemInputLayout() == null) { return; }
		
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(containerClass, recipeUid, 0, layout.getItemInputLayout().getCount(), layout.getInventorySlots(), layout.getPlayerSlots());
	}
}
