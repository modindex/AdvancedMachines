package jaminv.advancedmachines.lib.container.layout.impl;

import jaminv.advancedmachines.lib.container.ContainerMachine;
import jaminv.advancedmachines.lib.container.layout.Layout;
import jaminv.advancedmachines.lib.recipe.IRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class InputLayout extends Layout {
	IRecipeManager recipe;
	
	public static class SlotInput extends SlotItemHandler {
		IRecipeManager recipe;
		public SlotInput(IRecipeManager recipe, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
			this.recipe = recipe;
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return true; //return recipe.isItemValid(stack, null, null);
		}
	}	
	
	public InputLayout(IRecipeManager recipe, int xpos, int ypos, int xspacing, int yspacing, int rows, int cols) {
		super(xpos, ypos, xspacing, yspacing, rows, cols);
		this.recipe = recipe;
	}
	public InputLayout(IRecipeManager recipe, int xpos, int ypos, int rows, int cols) {
		super(xpos, ypos, rows, cols);
		this.recipe = recipe;
	}
	public InputLayout(IRecipeManager recipe, int xpos, int ypos) {
		super(xpos, ypos);
		this.recipe = recipe;
	}
	
	public SlotItemHandler createSlot(IItemHandler itemHandler, int slotIndex, int x, int y) {
		return new SlotItemHandler(itemHandler, slotIndex, x, y);
	}
}
	

	
