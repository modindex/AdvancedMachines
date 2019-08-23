package jaminv.advancedmachines.lib.jei;

import java.util.List;

import jaminv.advancedmachines.lib.container.layout.IFluidLayout;
import jaminv.advancedmachines.lib.container.layout.IItemLayout;
import jaminv.advancedmachines.lib.util.coord.CoordOffset;
import jaminv.advancedmachines.lib.util.coord.Offset;
import jaminv.advancedmachines.lib.util.coord.Pos;
import jaminv.advancedmachines.util.ModConfig;
import jaminv.advancedmachines.util.Reference;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class JeiRecipeCategory implements IRecipeCategory<JeiRecipeWrapper> {
	
	protected final IJeiDialog dialog;
	
	public JeiRecipeCategory(IJeiDialog dialog) {
		this.dialog = dialog;
	}

	protected IDrawableStatic background;
	protected IDrawableStatic icon;
	protected String localizedName;
	
	@Override
	public String getTitle() {
		return localizedName;
	}
	
	@Override
	public String getModName() {
		return Reference.NAME;
	}	
	
	@Override
	public IDrawable getBackground() {
		return background;
	}	
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JeiRecipeWrapper recipe, IIngredients ingredients) {

		List<List<ItemStack>> inputItems = ingredients.getInputs(ItemStack.class);
		List<List<FluidStack>> inputFluids = ingredients.getInputs(FluidStack.class);
		List<List<ItemStack>> outputItems = ingredients.getOutputs(ItemStack.class);
		List<List<FluidStack>> outputFluids = ingredients.getOutputs(FluidStack.class);
		
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		int i = this.displayRecipeSection(guiItemStacks, dialog.getLayout().getItemInputLayout(), inputItems, 0, 0, true);
		int o = this.displayRecipeSection(guiItemStacks, dialog.getLayout().getItemOutputLayout(), outputItems, i, 0, false);
		this.displayRecipeSection(guiItemStacks, dialog.getLayout().getItemSecondaryLayout(), outputItems, i + o, o, false);
		
		int fi = this.displayRecipeFluidSection(guiFluidStacks, dialog.getLayout().getFluidInputLayout(), inputFluids, 0, 0, true);
		int fo = this.displayRecipeFluidSection(guiFluidStacks, dialog.getLayout().getFluidOutputLayout(), outputFluids, fi, 0, false);
		this.displayRecipeFluidSection(guiFluidStacks, dialog.getLayout().getFluidSecondaryLayout(), outputFluids, fi + fo, fo, false);
		
		int offset = recipe.getInputCount() + recipe.getOutputCount();
		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex >= offset) {
				tooltip.add(I18n.format("dialog.common.chance", recipe.getSecondaryChance(slotIndex - offset)));
			}
			if (slotIndex == recipe.getInputCount()) {
				if (ModConfig.recipe.showRecipeIds)  {
					tooltip.add(I18n.format("dialog.jei.recipe_id", recipe.getRecipeId()));
				}
			}
		});
	}
	
	protected int displayRecipeSection(IGuiItemStackGroup guiItemStacks, IItemLayout layout, List<List<ItemStack>> items, int guiOffset, int itemOffset, boolean input) {
		if (layout == null) { return 0; }
		
		for (int i = 0; i < layout.getCount(); i++) {
			if (items.size() <= i + itemOffset) { break; }
			Pos pos = layout.getPosition(i).add(dialog.getJeiOffset()).add(new CoordOffset(-1, -1)); 
			guiItemStacks.init(i + guiOffset, input, pos.getX(), pos.getY());
			guiItemStacks.set(i + guiOffset, items.get(i + itemOffset));
		}
		
		return layout.getCount();
	}
	
	protected int displayRecipeFluidSection(IGuiFluidStackGroup guiFluidStacks, List<IFluidLayout> layouts, List<List<FluidStack>> fluids, int guiOffset, int itemOffset, boolean input) {
		if (layouts == null) { return 0; }
		Offset off = dialog.getJeiOffset();
		
		int i = 0;
		for (IFluidLayout layout : layouts) {
			if (fluids.size() <= i + itemOffset) { break; }
			guiFluidStacks.init(i + guiOffset, input, 
				layout.getXPos() + off.getX(), layout.getYPos() + off.getY(), 
				layout.getWidth(), layout.getHeight(), 
				2000, false, null);
			guiFluidStacks.set(i + guiOffset, fluids.get(i));
		}
		
		return 0;
	}
	
	//protected void DisplayRecipeFluidSection(IGuiFluidStackGroup guiFluidStacks, T recipe, )
}
